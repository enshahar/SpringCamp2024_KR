package com.enshahar.KotlinDslSupport.blockCall

fun applyTo(arg: Int, block: (Int)->Int):Int = block(arg)

fun main() {
    applyTo(2, { v -> v * v })
    applyTo(2) { v -> v * v }
}
