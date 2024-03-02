package org.amogus.restarogus.services.authorization

import org.amogus.restarogus.models.Role
import org.amogus.restarogus.models.User
import org.amogus.restarogus.repositories.interfaces.UserRepository
import org.amogus.restarogus.requests.LoginRequest
import org.amogus.restarogus.requests.RegisterRequest
import org.amogus.restarogus.responses.AuthenticationResponse
import org.amogus.restarogus.services.interfaces.authorization.AuthenticationService
import org.amogus.restarogus.services.interfaces.authorization.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
    val userRepository: UserRepository,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager,
    val passwordEncoder: PasswordEncoder
) : AuthenticationService {
    override fun register(request: RegisterRequest): AuthenticationResponse? {
        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = Role.valueOf(request.role)
        )
        val userId = userRepository.add(user)
        val jwtToken = jwtService.generateToken(
            user.copy(id = userId)
        )
        return AuthenticationResponse(jwtToken)
    }

    override fun login(request: LoginRequest): AuthenticationResponse? {
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