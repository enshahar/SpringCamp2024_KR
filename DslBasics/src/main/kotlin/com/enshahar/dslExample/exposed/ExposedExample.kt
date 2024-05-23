package com.enshahar.dslExample.exposed

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object StarWarsFilms : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val sequelId: Column<Int> = integer("sequel_id").uniqueIndex()
    val name: Column<String> = varchar("name", 50)
    val director: Column<String> = varchar("director", 50)
    override val primaryKey = PrimaryKey(id, name = "PK_StarWarsFilms_Id") // PK_StarWarsFilms_Id is optional here
}

fun select() {
    val directors = StarWarsFilms
        .select(StarWarsFilms.director)
        .where { StarWarsFilms.sequelId less 5 }
        .withDistinct().map {
            it[StarWarsFilms.director]
        }
}

