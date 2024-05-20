package com.enshahar.SpringCampR2dbcExample.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDateTime

@Table(value="users")
data class User(
    @Id
    val id:Long?,
    var userName:String,
    var email:String,
    val created: Instant=Instant.now()
)