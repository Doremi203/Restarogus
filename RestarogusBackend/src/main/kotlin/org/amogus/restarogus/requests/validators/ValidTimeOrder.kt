package org.amogus.restarogus.requests.validators
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [TimeOrderValidator::class])
annotation class ValidTimeOrder(
    val message: String = "To time must be after From time",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)