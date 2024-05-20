package com.enshahar.SpringCampR2dbcExample.service

import com.enshahar.SpringCampR2dbcExample.entity.Group
import com.enshahar.SpringCampR2dbcExample.entity.User
import com.enshahar.SpringCampR2dbcExample.entity.UserGroup
import com.enshahar.SpringCampR2dbcExample.exceptions.TemplateApiException
import com.enshahar.SpringCampR2dbcExample.repository.GroupRepository
import com.enshahar.SpringCampR2dbcExample.repository.UserGroupRepository
import com.enshahar.SpringCampR2dbcExample.repository.UserRepository
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow as createFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.ListCrudRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.reactive.executeAndAwait
import reactor.kotlin.core.publisher.toMono
import java.sql.SQLException
import org.springframework.r2dbc.core.flow

interface MyRepo: CrudRepository<User, Long>
interface MyRepo2: ListCrudRepository<User, Long>
interface MyRepo3: ReactiveCrudRepository<User, Long>
interface MyRepo4: R2dbcRepository<User, Long>
interface MyRepo5: ReactiveSortingRepository<User,Long>
interface MyRepo6: ReactiveQueryByExampleExecutor<User>
interface MyRepo7: CoroutineCrudRepository<User,Long>


@Service
class TransactionalOperatorExampleService(val userRepo: UserRepository,
                                          val groupRepo: GroupRepository,
                                          val UserGroupRepo: UserGroupRepository,
                                          val databaseClient: DatabaseClient,
                                          val transactionalOperator: TransactionalOperator) {
    suspend fun addUserAndGroup(userName: String, email: String): Triple<User?, Group?, UserGroup?> =
        transactionalOperator.executeAndAwait {
            val user = userRepo.save(User(null, userName, email))
            val group = groupRepo.save(Group(null, userName))
            val userGroup = UserGroupRepo.save(UserGroup(null, user.id!!, group.id!!))
            return@executeAndAwait Triple(user, group, userGroup)
        }

    suspend fun addUserAndGroup2(userName: String, email: String): Triple<Long?, Long?, Long?> {
        val flow = createFlow {
            val userId =
                databaseClient.sql("insert into users(user_name,email,created) values(:userName,:email,LOCALTIMESTAMP())")
                    .bind("userName", userName)
                    .bind("email", email)
                    .filter { stmt -> stmt.returnGeneratedValues("id") }
                    .map { row -> row.get("id", Long::class.java) }
                    .awaitSingleOrNull() ?: throw RuntimeException("exception while insert user")
            println("userId = ${userId}")
            val groupId = databaseClient.sql("insert into `groups`(name,created) values(:name,LOCALTIMESTAMP())")
                .bind("name", userName)
                .filter { stmt -> stmt.returnGeneratedValues("id") }
                .map { row -> row.get("id", Long::class.java) }
                .awaitSingleOrNull() ?: throw RuntimeException("exception while insert group")
            println("groupId = ${groupId}")
            val userGroupId = databaseClient.sql("insert into user_group(user_id,group_id) values(:userId,:groupId)")
                .bind("userId", userId)
                .bind("groupId", groupId)
                .filter { stmt -> stmt.returnGeneratedValues("id") }
                .map { row -> row.get("id", Long::class.java) }
                .awaitSingleOrNull() ?: throw RuntimeException("exception while insert usergroup")
            println("userGroupId= ${userGroupId}")
            emit(Triple(userId,groupId,userGroupId))
        }
        return transactionalOperator.transactional(flow.asFlux()).awaitSingle()
    }
    suspend fun addUserAndGroup3(userName: String, email: String): Triple<Long?, Long?, Long?> {
        val userId =
            databaseClient.sql("insert into users(user_name,email,created) values(:userName,:email,LOCALTIMESTAMP())")
                .bind("userName", userName)
                .bind("email", email)
                .filter { stmt -> stmt.returnGeneratedValues("id") }
                .map { row -> row.get("id", Long::class.java) ?: throw IllegalStateException("null id from inserted used id") }
                .flow().single()
        println("userId = ${userId}")
        val groupId = databaseClient.sql("insert into `groups`(name,created) values(:name,LOCALTIMESTAMP())")
            .bind("name", userName)
            .filter { stmt -> stmt.returnGeneratedValues("id") }
            .map { row -> row.get("id", Long::class.java)  ?: throw IllegalStateException("null id from inserted group id")}
            .flow().single()
        println("groupId = ${groupId}")
        val userGroupId = databaseClient.sql("insert into user_group(user_id,group_id) values(:userId,:groupId)")
            .bind("userId", userId)
            .bind("groupId", groupId)
            .filter { stmt -> stmt.returnGeneratedValues("id") }
            .map { row -> row.get("id", Long::class.java)  ?: throw IllegalStateException("null id from inserted usergroup id")}
            .flow().single()

        println("userGroupId= ${userGroupId}")

        return Triple(userId,groupId,userGroupId)
    }
    suspend fun addUserAndGroup4(userName: String, email: String): Triple<Long?, Long?, Long?> {
        val flow = databaseClient.sql("insert into users(user_name,email,created) values(:userName,:email,LOCALTIMESTAMP())")
            .bind("userName", userName)
            .bind("email", email)
            .filter { stmt -> stmt.returnGeneratedValues("id") }
            .map { row ->
                row.get("id", Long::class.java) ?: throw IllegalStateException("null id from inserted used id")
            }
            .flow().flatMapMerge { userId ->
                println("userId = ${userId}")
                databaseClient.sql("insert into `groups`(name,created) values(:name,LOCALTIMESTAMP())")
                    .bind("name", userName)
                    .filter { stmt -> stmt.returnGeneratedValues("id") }
                    .map { row ->
                        row.get("id", Long::class.java)
                            ?: throw IllegalStateException("null id from inserted group id")
                    }
                    .flow().map { userId to it }.flatMapMerge { (userId, groupId) ->
                        println("groupId = ${groupId}")
                        databaseClient.sql("insert into user_group(user_id,group_id) values(:userId,:groupId)")
                            .bind("userId", userId)
                            .bind("groupId", groupId)
                            .filter { stmt -> stmt.returnGeneratedValues("id") }
                            .map { row ->
                                row.get("id", Long::class.java)
                                    ?: throw IllegalStateException("null id from inserted usergroup id")
                            }
                            .flow().map {
                                println("userGroupId= ${it}")
                                Triple(userId, groupId, it)
                            }
                    }
            }
        return transactionalOperator.transactional(flow.asFlux()).awaitSingle()
    }
}