package org.amogus.restarogus.requests.validators

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.amogus.restarogus.models.Role

class RoleValidator : ConstraintValidator<ValidRole, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }
        return try {
            Role.valueOf(value)
            true
        } catch (e: IllegalArgumentException) {
            context.disableDefaultConstraintViolation()
            context
                .buildConstraintViolationWithTemplate(
                    "Invalid role, roles are: ${Role.entries.joinToString()}"
                )
                .addConstraintViolation()
            false
        }
    }
}