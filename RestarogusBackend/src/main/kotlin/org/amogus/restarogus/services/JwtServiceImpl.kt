package org.amogus.restarogus.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.amogus.restarogus.services.interfaces.JwtService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtServiceImpl : JwtService {

    private val secret = "5b9763e66a963ee6b756961c710e8f1b3a637dd6dce5e18e01317a360e186997"
    private val expirationTimeInMillis = 1000 * 60 * 24

    override fun isTokenValid(jwtToken: String, userDetails: UserDetails): Boolean {
        val userName = extractUserName(jwtToken)
        return userName == userDetails.username && !isTokenExpired(jwtToken)
    }

    private fun isTokenExpired(jwtToken: String): Boolean {
        val expiration = extractExpiration(jwtToken)
        return expiration?.before(Date(System.currentTimeMillis())) ?: true
    }

    override fun generateToken(userDetails: UserDetails): String {
        return generateToken(HashMap(), userDetails)
    }

    override fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts
            .builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expirationTimeInMillis))
            .signWith(getSecretKey(), Jwts.SIG.HS256)
            .compact()
    }

    override fun extractUserName(jwtToken: String): String? {
        return extractClaim(jwtToken, Claims::getSubject)
    }

    private fun extractExpiration(jwtToken: String): Date? {
        val expiration = extractClaim(jwtToken, Claims::getExpiration)
        return expiration
    }

    private fun <T> extractClaim(jwtToken: String, claimsResolver: (Claims) -> T): T {
        val claims = extractClaims(jwtToken)
        return claimsResolver(claims)
    }

    private fun extractClaims(jwtToken: String): Claims {
        return Jwts
            .parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(jwtToken)
            .payload
    }

    private fun getSecretKey(): SecretKey {
        val bytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(bytes)
    }

}
