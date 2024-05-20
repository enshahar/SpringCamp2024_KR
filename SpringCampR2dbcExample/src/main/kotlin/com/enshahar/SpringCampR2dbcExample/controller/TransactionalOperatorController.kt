package com.enshahar.SpringCampR2dbcExample.controller

import com.enshahar.SpringCampR2dbcExample.entity.Group
import com.enshahar.SpringCampR2dbcExample.entity.User
import com.enshahar.SpringCampR2dbcExample.entity.UserGroup
import com.enshahar.SpringCampR2dbcExample.service.CrudRepositoryExampleService
import com.enshahar.SpringCampR2dbcExample.service.TemplateExampleService
import com.enshahar.SpringCampR2dbcExample.service.TemplateTransactionExampleService
import com.enshahar.SpringCampR2dbcExample.service.TransactionalOperatorExampleService
import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class TransactionalOperatorController(val service: CrudRepositoryExampleService, val trService: TransactionalOperatorExampleService) {
    @GetMapping("/operator/user/list")
    @ResponseBody
    suspend fun userList(): Flow<User> = service.allUsers()

    @GetMapping("/operator/user/add")
    @ResponseBody
    suspend fun userAdd(@RequestParam("userName")userName:String, @RequestParam("email")email:String): User? =
        service.addUser(userName, email)

    @GetMapping("/operator/user/addWithGroup")
    @ResponseBody
    suspend fun userAddWithGroup(@RequestParam("userName")userName:String, @RequestParam("email")email:String):Triple<User?, Group?, UserGroup?> =
        trService.addUserAndGroup(userName, email)

    @GetMapping("/operator/user/addWithGroup2")
    @ResponseBody
    suspend fun userAddWithGroup2(@RequestParam("userName")userName:String, @RequestParam("email")email:String): Triple<Long?, Long?, Long?> =
        trService.addUserAndGroup2(userName, email)

    @GetMapping("/operator/user/addWithGroup3")
    @ResponseBody
    suspend fun userAddWithGroup3(@RequestParam("userName")userName:String, @RequestParam("email")email:String): Triple<Long?, Long?, Long?> =
        trService.addUserAndGroup3(userName, email)

    @GetMapping("/operator/user/addWithGroup4")
    @ResponseBody
    suspend fun userAddWithGroup4(@RequestParam("userName")userName:String, @RequestParam("email")email:String): Triple<Long?, Long?, Long?> =
        trService.addUserAndGroup4(userName, email)

    @GetMapping("/operator/group/list")
    @ResponseBody
    suspend fun groupList():Flow<Group> = service.allGroups()

    @GetMapping("/operator/group/add")
    @ResponseBody
    suspend fun userAdd(@RequestParam("name")name:String): Group? =
        service.addGroup(name)
}