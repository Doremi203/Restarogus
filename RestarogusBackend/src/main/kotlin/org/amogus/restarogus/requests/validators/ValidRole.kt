package org.amogus.restarogus.requests.validators
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [RoleValidator::class])
annotation class ValidRole(
    val message: String = "Invalid role",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)