package com.enshahar.KotlinDslSupport.operators

data class Rational(val nominator: Int, val denominator: Int = 1) {
    operator fun plus(r: Rational): Rational = TODO()
    operator fun minus(r: Rational): Rational = TODO()
    operator fun times(r: Rational): Rational = TODO()
    operator fun div(r: Rational): Rational  = TODO()
    infix fun power(x:Int): Rational = TODO()
}

fun main() {
    val r1 = Rational(10,53)
    val r2 = Rational(2,3)

    println(r1 + r2)
    println(r1 - r2)
    println(r1 * r2)
    println(r1 / r2)
    println(r1 power 10)
}
