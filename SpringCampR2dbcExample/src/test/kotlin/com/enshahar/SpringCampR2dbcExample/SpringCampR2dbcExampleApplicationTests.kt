package com.enshahar.SpringCampR2dbcExample

import org.junit.jupiter.api.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SpringCampR2dbcExampleApplicationTests(
	private val entityTemplate: R2dbcEntityTemplate
) {

	@Test
	fun contextLoads() {
	}

	@Test
	@Order(1)
	fun transactionSuccss() {
	}
}
