package com.enshahar.SpringCampR2dbcExample.repository

import com.enshahar.SpringCampR2dbcExample.entity.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepository: CoroutineCrudRepository<User,Long>