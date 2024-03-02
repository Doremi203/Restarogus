package org.amogus.restarogus.controllers

import org.amogus.restarogus.requests.LoginRequest
import org.amogus.restarogus.requests.RegisterRequest
import org.amogus.restarogus.responses.AuthenticationResponse
import org.amogus.restarogus.services.interfaces.authorization.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.register(request))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok(authenticationService.login(request))
    }
}