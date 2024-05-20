package com.enshahar.SpringCampR2dbcExample.controller

import com.enshahar.SpringCampR2dbcExample.entity.ErrorResponse
import com.enshahar.SpringCampR2dbcExample.entity.RuntimeErrorResponse
import com.enshahar.SpringCampR2dbcExample.exceptions.TemplateApiException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(*[(TemplateApiException::class)])
    fun handle(ex: Throwable): ErrorResponse = ErrorResponse(ex)

    @ExceptionHandler(*[(RuntimeException::class)])
    fun handleRuntimeException(ex: Throwable): RuntimeErrorResponse = RuntimeErrorResponse(ex)
}