package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
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
class MTest {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Test
	fun t() {

		mockMvc.perform(get("/api/a"))
				.andExpect(status().isOk)
				.andExpect(content().json("2"))
	}

}