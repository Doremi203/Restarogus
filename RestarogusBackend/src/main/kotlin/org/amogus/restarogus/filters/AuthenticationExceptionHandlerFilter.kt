package org.amogus.restarogus.filters

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.net.URI

@Component
class AuthenticationExceptionHandlerFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: AuthenticationException) {
            writeProblemDetailToResponse(request, response, e)
        }
    }

    private fun writeProblemDetailToResponse(
        request: HttpServletRequest,
        response: HttpServletResponse,
        e: AuthenticationException
    ) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = "application/json"
        val problemDetail =
            ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.message ?: "Authentication error")
        problemDetail.instance = URI.create(request.requestURI)
        response.writer.write(objectMapper.writeValueAsString(problemDetail))
    }
}