package org.amogus.restarogus.controllers

import jakarta.validation.Valid
import org.amogus.restarogus.requests.OrdersCountOverPeriodRequest
import org.amogus.restarogus.services.interfaces.RestaurantStatsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/restaurant_stats")
class RestaurantStatsController(
    private val restaurantStatsService: RestaurantStatsService
) {
    @GetMapping("/revenue")
    fun getRevenue(): ResponseEntity<BigDecimal> {
        return ResponseEntity.ok(restaurantStatsService.getRevenue())
    }

    @GetMapping("/loss")
    fun getLoss(): ResponseEntity<BigDecimal> {
        return ResponseEntity.ok(restaurantStatsService.getLoss())
    }

    @GetMapping("/most_popular_dish")
    fun getMostPopularDish(): ResponseEntity<String> {
        return ResponseEntity.ok(restaurantStatsService.getMostPopularDish())
    }

    @GetMapping("/average_rating")
    fun getAverageRating(): ResponseEntity<Double> {
        return ResponseEntity.ok(restaurantStatsService.getAverageRating())
    }

    @GetMapping("/orders_count_over_period")
    fun getOrdersCountOverPeriod(@Valid @RequestBody request: OrdersCountOverPeriodRequest): ResponseEntity<Int> {
        return ResponseEntity.ok(restaurantStatsService.getOrdersCountOverPeriod(request.from, request.to))
    }
}