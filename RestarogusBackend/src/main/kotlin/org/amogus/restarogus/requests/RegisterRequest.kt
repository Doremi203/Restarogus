package org.amogus.restarogus.requests

import jakarta.validation.constraints.NotBlank
import org.amogus.restarogus.requests.validators.ValidRole

data class RegisterRequest(
    @get:NotBlank(message = "Username is required")
    val username: String,
    @get:NotBlank(message = "Password is required")
    val password: String,
    @get:ValidRole(message = "Invalid role")
    val role: String
)
