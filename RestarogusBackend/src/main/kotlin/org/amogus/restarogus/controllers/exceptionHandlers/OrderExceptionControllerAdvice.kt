package org.amogus.restarogus.controllers.exceptionHandlers

import org.amogus.restarogus.exceptions.DuplicateReviewException
import org.amogus.restarogus.exceptions.IncorrectOrderStatusException
import org.amogus.restarogus.exceptions.MenuItemNotInMenuException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class OrderExceptionControllerAdvice {
    @ExceptionHandler(MenuItemNotInMenuException::class)
    fun handleMenuItemNotFoundException(e: MenuItemNotInMenuException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Ordered menu item not present in the menu")
    }

    @ExceptionHandler(IncorrectOrderStatusException::class)
    fun handleIncorrectOrderStatusException(e: IncorrectOrderStatusException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message ?: "Incorrect order status")
    }

    @ExceptionHandler(DuplicateReviewException::class)
    fun handleDuplicateReviewException(e: DuplicateReviewException): ProblemDetail {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.message ?: "Review already exists")
    }
}