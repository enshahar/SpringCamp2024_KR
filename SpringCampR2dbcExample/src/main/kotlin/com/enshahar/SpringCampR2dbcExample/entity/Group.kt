package com.enshahar.SpringCampR2dbcExample.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDateTime

@Table(value="`groups`")
data class Group(
    @Id
    val id: Long?,
    var name: String,
    val created: Instant = Instant.now()
)