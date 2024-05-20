package com.enshahar.operatorsAndScopes.fp


fun <Elem,Result> map(l: List<Elem>, block:(Elem)->Result): List<Result> = l.map(block)
fun <Elem,Result> fold(l: List<Elem>, acc: Result, block:(Result,Elem)->Result): Result = l.fold(acc,block)

fun main() {
    val x = listOf(1,2,3,4,5)

    val sumOfSquares = fold(map(x){it*it}, 0L) { acc, elem ->
        acc + elem.toLong()
    }

    println(sumOfSquares)
}

