package com.enshahar.SpringCampR2dbcExample.controller

import com.enshahar.SpringCampR2dbcExample.entity.Group
import com.enshahar.SpringCampR2dbcExample.entity.User
import com.enshahar.SpringCampR2dbcExample.entity.UserGroup
import com.enshahar.SpringCampR2dbcExample.service.TemplateExampleService
import com.enshahar.SpringCampR2dbcExample.service.TemplateTransactionExampleService
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
class TemplateExampleController(val service: TemplateExampleService, val trService: TemplateTransactionExampleService) {
    @GetMapping("/template/user/list")
    @ResponseBody
    suspend fun userList():List<User> = service.allUsers()

    @GetMapping("/template/user/add")
    @ResponseBody
    suspend fun userAdd(@RequestParam("userName")userName:String,@RequestParam("email")email:String):User? =
        service.addUser(userName, email)

    @GetMapping("/template/user/addWithGroup")
    @ResponseBody
    suspend fun userAddWithGroup(@RequestParam("userName")userName:String,@RequestParam("email")email:String):Triple<User?,Group?, UserGroup?> =
        trService.addUserAndGroup(userName, email)

    @GetMapping("/template/user/addWithGroup2")
    @ResponseBody
    suspend fun userAddWithGroup2(@RequestParam("userName")userName:String,@RequestParam("email")email:String): Mono<Triple<User?, Group?, UserGroup?>> =
        trService.addUserAndGroup2(userName, email)

    @GetMapping("/template/user/addWithGroup3")
    @ResponseBody
    suspend fun userAddWithGroup3(@RequestParam("userName")userName:String,@RequestParam("email")email:String): Mono<Triple<User?, Group?, UserGroup?>> =
        trService.addUserAndGroup3(userName, email)

    @GetMapping("/template/user/addWithGroup4")
    @ResponseBody
    suspend fun userAddWithGroup4(@RequestParam("userName")userName:String,@RequestParam("email")email:String): Mono<Triple<User?, Group?, UserGroup?>> =
        trService.addUserAndGroup4(userName, email)

    @GetMapping("/template/user/addWithGroup5")
    @ResponseBody
    suspend fun userAddWithGroup5(@RequestParam("userName")userName:String,@RequestParam("email")email:String): Triple<User?, Group?, UserGroup?> =
        trService.addUserAndGroup5(userName, email)

    @GetMapping("/template/group/list")
    @ResponseBody
    suspend fun groupList():List<Group> = service.allGroups()

    @GetMapping("/template/group/add")
    @ResponseBody
    suspend fun userAdd(@RequestParam("name")name:String):Group? =
        service.addGroup(name)
}