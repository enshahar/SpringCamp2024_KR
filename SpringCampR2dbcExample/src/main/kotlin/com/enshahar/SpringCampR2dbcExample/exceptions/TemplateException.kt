package com.enshahar.SpringCampR2dbcExample.exceptions

open class TemplateApiException(message:String, override val cause: Throwable?=null): RuntimeException(message, cause)