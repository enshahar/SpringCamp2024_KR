package com.enshahar.SpringCampR2dbcExample.service

import com.enshahar.SpringCampR2dbcExample.entity.Group
import com.enshahar.SpringCampR2dbcExample.entity.User
import com.enshahar.SpringCampR2dbcExample.entity.UserGroup
import com.enshahar.SpringCampR2dbcExample.exceptions.TemplateApiException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionSynchronizationManager
import reactor.core.publisher.Mono

@Service
class TemplateTransactionExampleService(val db:R2dbcEntityTemplate) {
    @Transactional
    suspend fun addUserAndGroup(userName: String, email: String): Triple<User?, Group?, UserGroup?> {
        try {
            val user = db.insert(User(null, userName, email)).awaitSingleOrNull()
            val group = db.insert(Group(null, userName)).awaitSingleOrNull()

            val userAndGroup = if (user != null && group != null) {
                db.insert(UserGroup(null, user.id!!, group.id!!)).awaitSingleOrNull()
            } else {
                throw IllegalArgumentException("User or group is null. Cannot add usergroup")
            }

            return Triple(user, group, userAndGroup)
        } catch (e: Throwable) {
            throw TemplateApiException("Exception while addUserAndGroup()", e)
        }
    }

    @Transactional
    suspend fun addUserAndGroup3(userName: String, email: String): Mono<Triple<User?, Group?, UserGroup?>> {
        try {
            val user = db.insert(User(null, userName, email)).awaitSingleOrNull()
            val group = db.insert(Group(null, userName)).awaitSingleOrNull()

            val userAndGroup = if (user != null && group != null) {
                db.insert(UserGroup(null, user.id!!, group.id!!)).awaitSingleOrNull()
            } else {
                return Mono.error(IllegalArgumentException("User or group is null. Cannot add usergroup"))
            }
            return Mono.just(Triple(user, group, userAndGroup))
        } catch (e: Throwable) {
            return Mono.error(TemplateApiException("Exception while addUserAndGroup()", e))
        }
    }


    @Transactional
    suspend fun addUserAndGroup4(userName: String, email: String): Mono<Triple<User?, Group?, UserGroup?>> = Mono.fromFuture(
        coroutineScope {
            val deffered = async {
                try {
                    val user = db.insert(User(null, userName, email)).awaitSingleOrNull()
                    val group = db.insert(Group(null, userName)).awaitSingleOrNull()

                    val userAndGroup = if (user != null && group != null) {
                        db.insert(UserGroup(null, user.id!!, group.id!!)).awaitSingleOrNull()
                    } else {
                        throw IllegalArgumentException("User or group is null. Cannot add usergroup")
                    }
                    Triple(user, group, userAndGroup)
                } catch (e: Throwable) {
                    throw TemplateApiException("Exception while addUserAndGroup()", e)
                }
            }
            deffered.asCompletableFuture()
        })

    @Transactional
    suspend fun addUserAndGroup5(userName: String, email: String): Triple<User?, Group?, UserGroup?> =
        coroutineScope {
            val deffered = async {
                try {
                    val user = db.insert(User(null, userName, email)).awaitSingleOrNull()
                    val group = db.insert(Group(null, userName)).awaitSingleOrNull()

                    val userAndGroup = if (user != null && group != null) {
                        db.insert(UserGroup(null, user.id!!, group.id!!)).awaitSingleOrNull()
                    } else {
                        throw IllegalArgumentException("User or group is null. Cannot add usergroup")
                    }
                    Triple(user, group, userAndGroup)
                } catch (e: Throwable) {
                    throw TemplateApiException("Exception while addUserAndGroup()", e)
                }
            }
            deffered.await()
        }

    @Transactional
    suspend fun addUserAndGroup2(userName: String, email: String): Mono<Triple<User?, Group?, UserGroup?>> =
        try {
            db.insert(User(null, userName, email)).flatMap { user ->
                db.insert(Group(null, userName)).flatMap { group ->
                    db.insert(UserGroup(null, user.id!!, group.id!!)).map {userAndGroup ->
                        Triple(user, group, userAndGroup)
                    }
                }
            }
        } catch(e:Throwable) {
            throw TemplateApiException("Exception while addUserAndGroup()", e)
        }
}