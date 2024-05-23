package com.enshahar.KotlinDslSupport.propertyDelegateProvider.propertyDelegateProvider

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object StringDelegateProvider {
    operator fun provideDelegate(thisRef: Entity, property: KProperty<*>) =
        object: ReadWriteProperty<Entity, String> {
            override fun getValue(thisRef: Entity, property: KProperty<*>): String =
                thisRef.values[property.name] as String

            override fun setValue(thisRef: Entity, property: KProperty<*>, value: String) {
                thisRef.values[property.name] = value
            }
        }.also {
            thisRef.fields[property.name] = property.returnType.toString()
        }
}

object LongDelegateProvider {
    operator fun provideDelegate(thisRef: Entity, property: KProperty<*>) =
        object: ReadWriteProperty<Entity, Long> {
            override fun getValue(thisRef: Entity, property: KProperty<*>): Long =
                thisRef.values[property.name] as Long

            override fun setValue(thisRef: Entity, property: KProperty<*>, value: Long) {
                thisRef.values[property.name] = value
            }
        }.also {
            thisRef.fields[property.name] = property.returnType.toString()
        }
}

class Entity {
    val values = mutableMapOf<String,Any>()
    val fields = mutableMapOf<String,String>()

    var id by LongDelegateProvider
    var firstName by StringDelegateProvider
    var lastName by StringDelegateProvider

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
    println(x.fields.entries.joinToString())
}
