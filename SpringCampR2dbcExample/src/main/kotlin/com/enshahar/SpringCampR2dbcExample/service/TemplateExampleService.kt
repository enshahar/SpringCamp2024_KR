package com.enshahar.SpringCampR2dbcExample.service

import com.enshahar.SpringCampR2dbcExample.entity.User
import com.enshahar.SpringCampR2dbcExample.entity.Group
import com.enshahar.SpringCampR2dbcExample.exceptions.TemplateApiException
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service

@Service
class TemplateExampleService(val db:R2dbcEntityTemplate) {
    suspend fun allUsers():List<User> =
        try {
            db.select(User::class.java).all().collectList().awaitSingle()
        } catch(e:Throwable) {
            throw TemplateApiException("Exception in allUsers()",e)
        }

    suspend fun addUser(userName: String, email: String): User? =
        try {
            db.insert(User(null, userName, email)).awaitSingleOrNull()
        } catch(e:Throwable) {
            throw TemplateApiException("Exception in addUser()",e)
        }

    suspend fun allGroups():List<Group> =
        try {
            db.select(Group::class.java).all().collectList().awaitSingle()
        } catch(e:Throwable) {
            throw TemplateApiException("Exception in allGroups()",e)
        }

    suspend fun addGroup(name: String): Group? =
        try {
            db.insert(Group(null, name)).awaitSingleOrNull()
        } catch(e:Throwable) {
            throw TemplateApiException("Exception in addGroup()",e)
        }
}