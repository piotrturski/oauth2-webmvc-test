package com.example.demo

import com.example.demo.pack.AuthorizationServerConf
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@SpringBootTest
class DemoApplicationTests {

	@Test
	fun contextLoads() {
	}

}


@WebMvcTest
@Import(AuthorizationServerConf::class)
class MTest {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun t() {

		mockMvc.perform(get("/api/a")
//				.with(SecurityMockMvcRequestPostProcessors.jwt())
				)
				.andExpect(status().isOk)
				.andExpect(content().json("2"))
	}


	@Test
	fun check_token() {

		val params = LinkedMultiValueMap<String, String>().apply {
			add("grant_type", "password");
			add("client_id", "my-cliend-id");
			add("username", "admin");
			add("password", "admin");
		}

		mockMvc.perform(post("/oauth/token")
				.params(params)
				.with(httpBasic("my-cliend-id","frontendClientSecret"))
		)
				.andExpect(status().isOk)
				.andExpect(jsonPath("$.token_type", `is`("bearer")))
				.andExpect(jsonPath("$.scope", `is`("read")))
				.andExpect(jsonPath("$.access_token", notNullValue()))
				.andExpect(jsonPath("$.refresh_token", notNullValue()))
	}
}
