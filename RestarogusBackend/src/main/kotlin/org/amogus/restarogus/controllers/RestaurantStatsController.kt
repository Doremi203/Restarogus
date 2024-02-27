package org.amogus.restarogus.controllers

import org.amogus.restarogus.services.interfaces.RestaurantStatsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
}