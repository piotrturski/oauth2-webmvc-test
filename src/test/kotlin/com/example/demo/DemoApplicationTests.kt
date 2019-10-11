package com.example.demo

import com.example.demo.pack.AuthorizationServerConf
import com.example.demo.pack.MyController
import com.example.demo.pack.Oauth2ResourceServerConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.sun.security.auth.UserPrincipal
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.security.Principal

@SpringBootTest
class DemoApplicationTests {

	@Test
	fun contextLoads() {
	}

}

@WebMvcTest(MyController::class)
class MockedTokenTest {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun `call get api using mocked token`() {

		mockMvc.perform(
				get("/api/a")
						.with(authentication(TestingAuthenticationToken(UserPrincipal("admin3"), 2)))
		)
				.andExpect(status().isOk)
				.andExpect(content().json(""""hello admin3""""))
	}

	@Test
	fun `call put api using mocked token`() {

		mockMvc.perform(
				put("/api/b")
						.with(authentication(TestingAuthenticationToken(UserPrincipal("admin3"), 2)))
		)
				.andExpect(status().isOk)
				.andExpect(content().json(""""hello admin3""""))
	}

}

@WebMvcTest(MyController::class)
@Import(AuthorizationServerConf::class, Oauth2ResourceServerConfig::class)
class RealTokenTest {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun `call api using real token`() {

		val params = LinkedMultiValueMap<String, String>().apply {
			add("grant_type", "password");
			add("client_id", "my-cliend-id");
			add("username", "admin");
			add("password", "admin");
		}

		val contentAsString = mockMvc.perform(post("/oauth/token")
				.params(params)
				.with(httpBasic("my-cliend-id", "frontendClientSecret"))
		)
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.token_type", `is`("bearer")))
				.andExpect(jsonPath("$.scope", `is`("read")))
				.andExpect(jsonPath("$.access_token", notNullValue()))
				.andExpect(jsonPath("$.refresh_token", notNullValue()))
				.andReturn().response.contentAsString


		val readValue = ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				. registerModule(KotlinModule())
				.readValue(contentAsString, Token::class.java)

		mockMvc.perform(get("/api/a")
				.header(AUTHORIZATION, "Bearer ${readValue.access_token}")
		)
				.andExpect(status().isOk)
				.andExpect(content().json(""""hello admin""""))
	}
}
		data class Token(val access_token: String)
