package org.amogus.restarogus.requests

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @get:NotBlank
    val username: String,
    @get:NotBlank
    val password: String,
)
