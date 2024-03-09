package org.amogus.restarogus.config

import org.amogus.restarogus.exceptions.UserNotRegisteredException
import org.amogus.restarogus.repositories.interfaces.UserRepository
import org.amogus.restarogus.services.authorization.JwtServiceImpl
import org.amogus.restarogus.services.interfaces.authorization.JwtService
import org.amogus.restarogus.services.interfaces.orderSystem.OrderPriorityStrategy
import org.amogus.restarogus.services.orderSystem.OlderOrdersFirstPriorityStrategy
import org.springframework.boot.web.servlet.error.ErrorAttributes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfiguration(
    val userRepository: UserRepository,
) {
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.getByUserName(username) ?: throw UserNotRegisteredException()
        }
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        val daoAuthenticationProvider = DaoAuthenticationProvider()
        daoAuthenticationProvider.setUserDetailsService(userDetailsService())
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder())
        return daoAuthenticationProvider
    }

    @Bean
    fun authenticationManager(authConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authConfiguration.authenticationManager
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun jwtService(): JwtService {
        return JwtServiceImpl(
            "5b9763e66a963ee6b756961c710e8f1b3a637dd6dce5e18e01317a360e186997",
            15
        )
    }

    @Bean
    fun priorityStrategy(): OrderPriorityStrategy {
        return OlderOrdersFirstPriorityStrategy()
    }

    @Bean
    fun errorAttributes(): ErrorAttributes {
        return ProblemDetailsErrorAttributes()
    }
}

