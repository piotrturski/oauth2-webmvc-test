package com.example.demo

import com.example.demo.pack.AuthorizationServerConf
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.authserver.OAuth2AuthorizationServerConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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

}
