package org.amogus.restarogus.config

import org.amogus.restarogus.filters.JwtAuthenticationFilter
import org.amogus.restarogus.models.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    val jwtAuthenticationFilter: JwtAuthenticationFilter,
    val authenticationProvider: AuthenticationProvider
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
        return httpSecurity.build()
    }
}