package org.amogus.restarogus.services.authorization

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.amogus.restarogus.exceptions.IllegalJwtTokenException
import org.amogus.restarogus.services.interfaces.authorization.JwtService
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.crypto.SecretKey

class JwtServiceImpl(
    private val secret: String,
    expirationTimeInMinutes: Int
) : JwtService {

    private val expirationTimeInMillis = 1000 * 60 * expirationTimeInMinutes
    override fun isTokenValid(jwtToken: String, userDetails: UserDetails): Boolean {
        val userName = extractUserName(jwtToken)
        return userName == userDetails.username
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

    private fun <T> extractClaim(jwtToken: String, claimsResolver: (Claims) -> T): T {
        val claims = extractClaims(jwtToken)
        return claimsResolver(claims)
    }

    private fun extractClaims(jwtToken: String): Claims {
        return try {
            Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwtToken)
                .payload
        }
        catch (e: ExpiredJwtException) {
            throw IllegalJwtTokenException("JWT token has expired")
        }
        catch (e: Exception) {
            throw IllegalJwtTokenException("Incorrect JWT token")
        }
    }

    private fun getSecretKey(): SecretKey {
        val bytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(bytes)
    }

}
