package com.enshahar.SpringCampR2dbcExample.repository

import com.enshahar.SpringCampR2dbcExample.entity.Group
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface GroupRepository: CoroutineCrudRepository<Group,Long>