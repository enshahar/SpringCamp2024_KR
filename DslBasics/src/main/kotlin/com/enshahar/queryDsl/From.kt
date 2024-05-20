package com.enshahar.queryDsl

import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

@DslMarker
annotation class QueryDslMarker

@JvmInline
value class Alias<V:Entity>(val v:V)

inline fun <reified Table:Entity> from(table:Table, block:QueryDslContext<Table>.(Alias<Table>)->Unit) =
    Query<Table>(Table::class.simpleName ?: throw IllegalStateException("class no name"), Alias(table)).also {
        QueryDslContext<Table>(it).block(Alias(table))
    }

@QueryDslMarker
class QueryDslContext<Table:Entity>(val data:Query<Table>) {
    fun select(block: ProjectionContext<Table>.(Alias<Table>)->Unit) {
        ProjectionContext<Table>(data).block(data.table)
    }
    fun where(block: FilteringContext<Table>.(Alias<Table>)->SqlExpression) {
        data.filterExpr = FilteringContext<Table>(data).block(data.table)
    }
}

@QueryDslMarker
class ProjectionContext<Table:Entity>(val data:Query<Table>) {
    inline operator fun <reified T> KProperty1<Table,T>.unaryPlus() {
        data.addProjection(this)
    }
    inline fun <reified T> KProperty1<Table,T>.put() {
        data.addProjection(this)
    }
    inline infix fun <reified T> KProperty1<Table,T>.`as`(alias:String) {
        data.addProjection(this,alias)
    }
    inline operator fun Alias<Table>.invoke(block:Table.()->KProperty<*>) {
        data.addProjection(this.v.block())
    }
    inline operator fun Table.invoke(block:Table.()->KProperty<*>) {
        data.addProjection(this.block())
    }
}

@QueryDslMarker
class FilteringContext<Table:Entity>(val data:Query<Table>) {
    data class AliasField(val name: String)
    inline operator fun Alias<Table>.invoke(block: Table.() -> KProperty<*>) = AliasField(this.v.block().name)

    operator fun <T:Number> AliasField.plus(other: T) = ABop(SqlArithBop.PLUS, AField(this.name), AConst(other))
    operator fun <T:Number> T.plus(other: SqlArithExpression) = ABop(SqlArithBop.PLUS, AConst(this), other)
    operator fun AliasField.plus(other: SqlArithExpression) = ABop(SqlArithBop.PLUS, AField(this.name), other)
    operator fun AliasField.minus(other: SqlArithExpression) = ABop(SqlArithBop.MINUS, AField(this.name), other)
    operator fun SqlArithExpression.plus(other: AliasField) = ABop(SqlArithBop.PLUS, this, AField(other.name))
    operator fun SqlArithExpression.minus(other: AliasField) = ABop(SqlArithBop.MINUS, this, AField(other.name))
    operator fun SqlArithExpression.plus(other: SqlArithExpression) = ABop(SqlArithBop.PLUS, this, other)
    operator fun SqlArithExpression.minus(other: SqlArithExpression) = ABop(SqlArithBop.MINUS, this, other)

    infix fun AliasField.and(other: SqlLogicExpression) = LBop(SqlLogicBop.AND, LField(this.name), other)
    infix fun AliasField.or(other: SqlLogicExpression) = LBop(SqlLogicBop.OR, LField(this.name), other)
    infix fun SqlLogicExpression.and(other: AliasField) = LBop(SqlLogicBop.AND, this, LField(other.name))
    infix fun SqlLogicExpression.or(other: AliasField) = LBop(SqlLogicBop.OR, this, LField(other.name))
    infix fun SqlLogicExpression.and(other: SqlLogicExpression) = LBop(SqlLogicBop.AND, this, other)
    infix fun SqlLogicExpression.or(other: SqlLogicExpression) = LBop(SqlLogicBop.OR, this, other)

    infix fun <T:Number> SqlExpression.eq(other: T) = Eq(this, AConst(other))
    infix fun <T:Number> AliasField.eq(other: T) = Eq( AField(this.name), AConst(other))
    infix fun <T:Number> T.eq(other: SqlArithExpression) = Eq(AConst(this), other)
    infix fun AliasField.eq(other: SqlLogicExpression) = Eq(LField(this.name), other)
    infix fun AliasField.eq(other: SqlArithExpression) = Eq(AField(this.name), other)
    infix fun SqlLogicExpression.eq(other: AliasField) = Eq(this, LField(other.name))
    infix fun SqlArithExpression.eq(other: AliasField) = Eq(this, AField(other.name))
    infix fun SqlExpression.eq(other: SqlExpression) = Eq(this, other)

    fun count(v: SqlArithExpression) = AFun(SqlFun.COUNT,listOf(v))
    fun count(v: AliasField) = AFun(SqlFun.COUNT,listOf(AField(v.name)))
}

class Projection(val fieldName:String, val alias: String)

sealed interface SqlExpression
sealed interface SqlLogicExpression: SqlExpression
sealed interface SqlArithExpression: SqlExpression

enum class LConst: SqlLogicExpression { TRUE, FALSE }
data class LField(val name:String): SqlLogicExpression {
    override fun toString(): String = name
}
data class LBop(val op: SqlLogicBop, val e1: SqlLogicExpression,
                val e2: SqlLogicExpression): SqlLogicExpression {
    override fun toString(): String = "(${e1} ${op.op} ${e2})"
}
data class LFun(val fop: SqlFun, val e1: List<SqlExpression>): SqlLogicExpression {
    override fun toString(): String = "${fop.op}(${e1.joinToString()})"
}

data class Eq(val e1: SqlExpression, val e2: SqlExpression): SqlLogicExpression {
    override fun toString(): String = "(${e1} = ${e2})"
}

data class AConst<T:Number>(val v:T): SqlArithExpression {
   override fun toString() = v.toString()
}
data class AField(val name:String): SqlArithExpression {
    override fun toString(): String = name
}
data class ABop(val op: SqlArithBop, val e1: SqlArithExpression,
                val e2: SqlArithExpression): SqlArithExpression {
    override fun toString(): String = "(${e1} ${op.op} ${e2})"
}
data class AFun(val fop: SqlFun, val e1: List<SqlExpression>): SqlArithExpression {
    override fun toString(): String = "${fop.op}(${e1.joinToString()})"
}

enum class SqlLogicBop(val op:String) { AND("AND"), OR("OR") }
enum class SqlArithBop(val op:String) { PLUS("+"), MINUS("-") }
enum class SqlFun(val arity:Int, val op: String) { COUNT(1, "COUNT"), AVERAGE(1, "AVERAGE") }

class Query<Table:Entity>(val tableName: String, val table:Alias<Table>) {
    var projections: MutableList<Projection>? = null
    var filterExpr: SqlExpression? = null

    inline fun <reified T> addProjection(prop: KProperty1<Table,T>, alias: String=prop.name) {
        (projections ?: (mutableListOf<Projection>())).add(Projection(prop.name,alias))
    }
    inline fun <reified T> addProjection(prop: KProperty<T>, alias: String=prop.name) {
        (projections ?: (mutableListOf<Projection>())).add(Projection(prop.name,alias))
    }

    override fun toString():String {
        val pString = projections?.let {
            it.joinToString(",", prefix = "SELECT ", postfix = "\n") {
                "${it.fieldName} ${it.alias}"
            }
        } ?: "SELECT *\n"

        val from = "FROM $tableName\n"

        val wString = filterExpr?.let {
            "WHERE $it"
        } ?: ""

        return pString + from + wString
    }
}

class UserTable: Entity() {
    var id by long
    var name by string
    var email by string
}

fun main() {
    val sql = from(UserTable()) { table ->
        select {table ->
            table{::id}
        }
        where { table ->
            (table{::id} eq 10) and
                    (20.0 eq (table{::name} - count(table{::name})))
        }
    }

    println(sql.toString())
}