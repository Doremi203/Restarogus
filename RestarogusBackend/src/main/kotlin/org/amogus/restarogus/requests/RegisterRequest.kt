package org.amogus.restarogus.requests

data class RegisterRequest(
    val username: String,
    val password: String,
    val role: String
)