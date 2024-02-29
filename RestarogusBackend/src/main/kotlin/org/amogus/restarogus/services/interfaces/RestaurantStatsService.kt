package org.amogus.restarogus.services.interfaces

import java.math.BigDecimal
import java.time.LocalDateTime

interface RestaurantStatsService {
    fun updateRevenue(revenue: BigDecimal)
    fun getRevenue(): BigDecimal
    fun getMostPopularDish(): String
    fun getAverageRating(): Double
    fun getOrdersCountOverPeriod(from: LocalDateTime, to: LocalDateTime): Int
}
