package com.enshahar.SpringCampR2dbcExample.repository

import com.enshahar.SpringCampR2dbcExample.entity.UserGroup
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserGroupRepository: CoroutineCrudRepository<UserGroup,Long>