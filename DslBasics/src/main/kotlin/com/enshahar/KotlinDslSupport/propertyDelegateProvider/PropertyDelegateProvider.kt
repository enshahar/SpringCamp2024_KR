package com.enshahar.KotlinDslSupport.propertyDelegateProvider

import com.enshahar.queryDsl.Entity
import com.enshahar.queryDsl.Entity.Companion.toJson

class MyEntity: Entity() {
    var id by long
    var name by string
}

fun main() {
    val x = MyEntity()
    x.id = 10L
    x.name = "Hyunsok Oh"

    val y = MyEntity()

    println(x.toJson())
    println(y.toJson())
}