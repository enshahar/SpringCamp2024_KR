package com.enshahar.KotlinDslSupport.propertyDelegateProvider.propertyDelegate

import kotlin.reflect.KProperty

object LongDelegate {
    operator fun getValue(thisRef: Entity, property: KProperty<*>): Long =
        thisRef.values[property.name] as Long

    operator fun setValue(thisRef: Entity, property: KProperty<*>, value: Long) {
        thisRef.values[property.name] = value
    }
}

object StringDelegate {
    operator fun getValue(thisRef: Entity, property: KProperty<*>): String {
        return thisRef.values[property.name] as String
    }

    operator fun setValue(thisRef: Entity, property: KProperty<*>, value: String) {
        thisRef.values[property.name] = value
    }
}

class Entity {
    val values = mutableMapOf<String,Any>()

    var id by LongDelegate
    var firstName by StringDelegate
    var lastName by StringDelegate

    fun json() = values.entries.joinToString(
        prefix="{", postfix="}"
    ){ (k,v) -> "$k:$v" }
}

fun main() {
    val x = Entity()
    x.id = 10L
    x.firstName = "Hyunsok"
    x.lastName = "Hyunsok"

    println(x.json())
}
