package org.amogus.restarogus.services.interfaces.authorization

import org.amogus.restarogus.requests.LoginRequest
import org.amogus.restarogus.requests.RegisterRequest
import org.amogus.restarogus.responses.AuthenticationResponse

interface AuthenticationService {
    fun register(request: RegisterRequest): AuthenticationResponse?
    fun login(request: LoginRequest): AuthenticationResponse?
}