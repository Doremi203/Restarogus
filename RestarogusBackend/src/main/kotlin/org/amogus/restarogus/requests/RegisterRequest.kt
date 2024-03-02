package org.amogus.restarogus.requests

import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @get:NotBlank
    val username: String,
    @get:NotBlank
    val password: String,
    @get:NotBlank
    val role: String
)
