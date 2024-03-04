package org.amogus.restarogus.config

import org.amogus.restarogus.repositories.interfaces.UserRepository
import org.amogus.restarogus.services.interfaces.orderSystem.PriorityStrategy
import org.amogus.restarogus.services.orderSystem.OlderOrdersFirstPriorityStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Configuration
class ApplicationConfiguration(
    val userRepository: UserRepository,
) {
    @Bean
    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            userRepository.getByUserName(username) ?: throw UsernameNotFoundException("User not found")
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
    fun chefExecutorService(): ExecutorService {
        val corePoolSize = 2
        val maximumPoolSize = 2
        val keepAliveTime = 0L
        val unit = TimeUnit.MILLISECONDS
        val workerQueue = ArrayBlockingQueue<Runnable>(2)
        val handler = ThreadPoolExecutor.DiscardPolicy()
        return ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            workerQueue,
            handler
        )
    }

    @Bean
    fun priorityStrategy(): PriorityStrategy {
        return OlderOrdersFirstPriorityStrategy()
    }
}