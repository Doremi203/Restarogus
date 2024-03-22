package org.amogus.restarogus.requests.validators
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.amogus.restarogus.requests.OrdersCountOverPeriodRequest

class TimeOrderValidator : ConstraintValidator<ValidTimeOrder, OrdersCountOverPeriodRequest> {
    override fun isValid(request: OrdersCountOverPeriodRequest, context: ConstraintValidatorContext): Boolean {
        return request.to.isAfter(request.from)
    }
}