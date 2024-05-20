package com.enshahar.queryDsl2

import com.enshahar.queryDsl.Entity
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

@DslMarker
annotation class QueryDslMarker

@JvmInline
value class Alias<V: Entity>(val v:V)

fun <Table:Entity> from(table:Table, block:QueryDslContext<Table>.(Alias<Table>)->Unit) =
    Query<Table>(Alias(table)).also {
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

class Projection(val fieldName:String, val alias: String)

@QueryDslMarker
class FilteringContext<Table:Entity>(val data:Query<Table>) {
    data class AliasField(val name: String)
    inline operator fun Alias<Table>.invoke(block: Table.() -> KProperty<*>) = AliasField(this.v.block().name)

    operator fun SqlExpression.plus(other: SqlExpression) = Bop(SqlBop.PLUS, this, other)
    operator fun SqlExpression.minus(other: SqlExpression) = Bop(SqlBop.MINUS, this, other)
    infix fun SqlExpression.and(other: SqlExpression) = Bop(SqlBop.AND, this, other)
    infix fun SqlExpression.or(other: SqlExpression) = Bop(SqlBop.OR, this, other)
    infix fun SqlExpression.eq(other: SqlExpression) = Bop(SqlBop.EQ,this, other)

    fun count(v: SqlExpression) = Fun(SqlFun.COUNT,listOf(v))

    fun <T:Number> T.sql(): SqlExpression = Const(this)
    fun AliasField.sql(): SqlExpression = Field(this.name)
}

sealed interface SqlExpression
data class Const<T:Number>(val v:T): SqlExpression
data class Field(val name:String): SqlExpression
data class Bop(val op: SqlBop, val e1: SqlExpression, val e2: SqlExpression): SqlExpression
data class Fun(val fop: SqlFun, val e1: List<SqlExpression>): SqlExpression

enum class SqlBop {
    AND, OR, PLUS, MINUS, EQ
}

enum class SqlFun(val arity:Int) {
    COUNT(1),
    AVERAGE(1)
}

class Query<Table:Entity>(val table:Alias<Table>) {
    var projections: MutableList<Projection>? = null
    var filterExpr: SqlExpression? = null

    inline fun <reified T> addProjection(prop: KProperty1<Table,T>, alias: String=prop.name) {
        (projections ?: (mutableListOf<Projection>())).add(Projection(prop.name,alias))
    }
    inline fun <reified T> addProjection(prop: KProperty<T>, alias: String=prop.name) {
        (projections ?: (mutableListOf<Projection>())).add(Projection(prop.name,alias))
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
            (table{::id}.sql() eq 10.sql()) and
                    (20.0.sql() eq (table{::name}.sql() - count(table{::name}.sql())))
        }
    }
}