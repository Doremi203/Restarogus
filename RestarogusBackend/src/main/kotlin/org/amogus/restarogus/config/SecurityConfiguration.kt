package org.amogus.restarogus.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.amogus.restarogus.filters.AuthenticationExceptionHandlerFilter
import org.amogus.restarogus.filters.JwtAuthenticationFilter
import org.amogus.restarogus.models.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter
import java.net.URI

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationExceptionHandlerFilter: AuthenticationExceptionHandlerFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val objectMapper: ObjectMapper
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests { config ->
                config
                    .requestMatchers("/api/menuItems/**")
                    .hasAuthority(Role.ADMIN.name)
                    .requestMatchers("/api/restaurant_stats/**")
                    .hasAuthority(Role.ADMIN.name)
                    .requestMatchers("/api/orders/**")
                    .hasAnyAuthority(Role.ADMIN.name, Role.CUSTOMER.name)
                    .anyRequest()
                    .permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(authenticationExceptionHandlerFilter, CorsFilter::class.java)
            .exceptionHandling {
                it.authenticationEntryPoint { request, response, _ ->
                    response.status = HttpStatus.UNAUTHORIZED.value()
                    response.contentType = "application/json"
                    val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Not authenticated")
                    problemDetail.instance = URI.create(request.requestURI)
                    response.writer.write(objectMapper.writeValueAsString(problemDetail))
                }
            }
        return httpSecurity.build()
    }
}
