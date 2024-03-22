package org.amogus.restarogus.controllers.exceptionHandlers

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.net.URI

@ControllerAdvice
class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>>{
        val fieldErrors = exception.bindingResult.fieldErrors.associate { error ->
            error.field to error.defaultMessage
        }

        val globalErrors = exception.bindingResult.globalErrors.associate { error ->
            error.objectName to error.defaultMessage
        }

        val errors = fieldErrors + globalErrors

        val problemDetails = mapOf(
            "type" to "about:blank",
            "title" to "Validation Error",
            "status" to HttpStatus.BAD_REQUEST.value(),
            "detail" to "Your request parameters didn't validate.",
            "instance" to URI.create(request.requestURI),
            "errors" to errors
        )

        return ResponseEntity.badRequest().body(problemDetails)
    }
}