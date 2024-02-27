package org.amogus.restarogus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class RestarogusApplication

fun main(args: Array<String>) {
    runApplication<RestarogusApplication>(*args)
}
