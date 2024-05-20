package com.enshahar.SpringCampR2dbcExample.service

import com.enshahar.SpringCampR2dbcExample.entity.Group
import com.enshahar.SpringCampR2dbcExample.entity.User
import com.enshahar.SpringCampR2dbcExample.repository.GroupRepository
import com.enshahar.SpringCampR2dbcExample.repository.UserGroupRepository
import com.enshahar.SpringCampR2dbcExample.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Service

@Service
class CrudRepositoryExampleService(val userRepo: UserRepository, val groupRepo: GroupRepository, val UserGroupRepo: UserGroupRepository) {
    suspend fun allUsers(): Flow<User> = userRepo.findAll()

    suspend fun addUser(userName: String, email: String): User? {
        val user = User(null,userName,email)
        return try {
            userRepo.save(user)
        } catch(e:Throwable) {
            null
        }
    }

    fun allGroups(): Flow<Group> = groupRepo.findAll()
    suspend fun addGroup(name: String): Group? {
        val group = Group(null,name)
        return try {
            groupRepo.save(group)
        } catch(e:Throwable) {
            null
        }
    }

}