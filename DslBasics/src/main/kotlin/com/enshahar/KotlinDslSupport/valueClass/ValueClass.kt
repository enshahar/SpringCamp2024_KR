package com.enshahar.KotlinDslSupport.valueClass

@JvmInline
value class FieldName(val s:String)

class DslContext {
    operator fun String.unaryPlus():FieldName = FieldName(this)

    operator fun FieldName.plus(other:FieldName): FieldName = TODO()
}

fun runDsl(block:DslContext.()->Unit) = DslContext().block()

fun main() {
    runDsl {
        (+"field1").plus(+"field2")
    }
}