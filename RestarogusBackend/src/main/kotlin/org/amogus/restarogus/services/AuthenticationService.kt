package org.amogus.restarogus.services

import org.amogus.restarogus.responses.AuthenticationResponse
import org.amogus.restarogus.requests.LoginRequest
import org.amogus.restarogus.requests.RegisterRequest
import org.amogus.restarogus.entities.Role
import org.amogus.restarogus.entities.User
import org.amogus.restarogus.repositories.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    val userRepository: UserRepository,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager,
    val passwordEncoder: PasswordEncoder
) {
    fun register(request: RegisterRequest): AuthenticationResponse? {
        val user = User(
            id = UUID.randomUUID(),
            userName = request.username,
            authPassword = passwordEncoder.encode(request.password),
            role = Role.USER
        )
        userRepository.add(user)
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

    fun login(request: LoginRequest): AuthenticationResponse? {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )
        val user = userRepository.getByUserName(request.username) ?: throw Exception("User not found")
        val jwtToken = jwtService.generateToken(user)
        return AuthenticationResponse(jwtToken)
    }

}