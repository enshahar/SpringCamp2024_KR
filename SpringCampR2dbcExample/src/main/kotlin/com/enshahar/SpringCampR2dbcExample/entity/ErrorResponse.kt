package com.enshahar.SpringCampR2dbcExample.entity

data class ErrorResponse(val message: String, val cause: ErrorResponse?) {
    constructor(e:Throwable):this(e.message ?: "${e::class}", if(e.cause==null) null else ErrorResponse(e.cause!!))
}
data class RuntimeErrorResponse(val message: String, val cause: ErrorResponse?) {
    constructor(e:Throwable):this("RuntimeError:\n${e.message}" ?: "${e::class}", if(e.cause==null) null else ErrorResponse(e.cause!!))
}
