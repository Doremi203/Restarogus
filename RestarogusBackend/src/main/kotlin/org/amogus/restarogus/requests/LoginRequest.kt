package org.amogus.restarogus.requests

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @get:NotBlank(message = "Username is required")
    val username: String,
    @get:NotBlank(message = "Password is required")
    val password: String,
)
