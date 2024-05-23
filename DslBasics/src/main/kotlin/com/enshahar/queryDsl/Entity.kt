package com.enshahar.queryDsl

import com.enshahar.queryDsl.Entity.Companion.toJson
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

typealias PropProvider<T> = PropertyDelegateProvider<Entity,ReadWriteProperty<Entity,T>>

abstract class Prop<T> {
    val delegator = PropProvider<T> { entity, prop ->
        println("provideDelegate: making ${prop.name} type ${prop.returnType}")
        ((entity?._props) ?:
        mutableMapOf<String,KProperty<*>>().also{
            entity._props=it})[prop.name] = prop
        ((entity?._fields) ?:
        mutableMapOf<String,Prop<*>>().also{
            entity._fields=it})[prop.name] = this
        entity as ReadWriteProperty<Entity,T>
    }
}

object LongProp:Prop<Long>()
object StringProp:Prop<String>()

abstract class Entity: ReadWriteProperty<Entity,Any> {
    var _values: MutableMap<String,Any>? = mutableMapOf()
    var _props: MutableMap<String,KProperty<*>>? = mutableMapOf()
    var _fields: MutableMap<String,Prop<*>>? = mutableMapOf()

    companion object {
        val long get() = long()
        val string get() = string()

        inline fun long():PropProvider<Long> = LongProp.delegator
        inline fun string():PropProvider<String> = StringProp.delegator

        fun Entity.toJson():String = _values?.entries?.joinToString(",","{","}"){(key,value) ->
            if(value is String)
                "'$key':'$value'"
            else
                "'$key':$value"
        } ?: "{}"
    }

    override operator fun getValue(thisRef: Entity, property: KProperty<*>): Any {
        println("getValue: ${property.name}")
        return _values?.get(property.name) ?: throw IllegalArgumentException("property does not exist: ${property.name}")
    }

    override operator fun setValue(thisRef: Entity, property: KProperty<*>, value: Any) {
        println("setValue: ${property.name} to $value")
        _values?.let {
            it[property.name] = value
        } ?: throw IllegalArgumentException("property does not exist: ${property.name}")
    }
}

class MyEntity: Entity() {
    var id by long
    var name by string
}

fun main() {
    val x = MyEntity();
    x.id = 10L;
    x.name = "Hyunsok Oh"
    println(x.toJson())
}