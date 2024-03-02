package org.amogus.restarogus.services.interfaces.authorization

import org.springframework.security.core.userdetails.UserDetails

interface JwtService {
    fun isTokenValid(jwtToken: String, userDetails: UserDetails): Boolean
    fun generateToken(userDetails: UserDetails): String
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String
    fun extractUserName(jwtToken: String): String?
}