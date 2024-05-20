package com.enshahar.SpringCampR2dbcExample

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringCampR2dbcExampleApplication

fun main(args: Array<String>) {
	runApplication<SpringCampR2dbcExampleApplication>(*args)

	runBlocking {
		async {

		}
	}
}
