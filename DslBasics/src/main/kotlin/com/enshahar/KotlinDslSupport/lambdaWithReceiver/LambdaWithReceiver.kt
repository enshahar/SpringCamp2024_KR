package com.enshahar.KotlinDslSupport.lambdaWithReceiver

class GraphicsContext {
    data class Point(val x:Int, val y:Int)
    data class Color(val r:UByte, val g:UByte, val b:UByte)
    fun clear():GraphicsContext = TODO()
    fun lineTo(to: Point):GraphicsContext = TODO()
    fun moveTo(to: Point):GraphicsContext = TODO()
    fun fill(rgb: Color):GraphicsContext= TODO()
}

fun client1(gc:GraphicsContext) {
    gc.clear()
    gc.moveTo(GraphicsContext.Point(10, 10))
    gc.lineTo(GraphicsContext.Point(110, 110))
    gc.fill(GraphicsContext.Color(0xFFU, 0x0FU, 0x3FU))
}

fun client2(gc: GraphicsContext) {
    gc.clear()
      .moveTo(GraphicsContext.Point(10, 10))
      .lineTo(GraphicsContext.Point(110, 110))
      .fill(GraphicsContext.Color(0xFFU, 0x0FU, 0x3FU))
}

fun withGC(gc: GraphicsContext, block: GraphicsContext.()->Unit) {
    gc.block()
}

fun client3(gc: GraphicsContext) = withGC(gc) {
    clear()
    moveTo(GraphicsContext.Point(10, 10))
    lineTo(GraphicsContext.Point(110, 110))
    fill(GraphicsContext.Color(0xFFU, 0x0FU, 0x3FU))
}
