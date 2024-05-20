package com.enshahar.SpringCampR2dbcExample.entity

import org.springframework.data.annotation.Id

data class UserGroup(
    @Id
    val id:Long?,
    val userId:Long,
    val groupId:Long
)
