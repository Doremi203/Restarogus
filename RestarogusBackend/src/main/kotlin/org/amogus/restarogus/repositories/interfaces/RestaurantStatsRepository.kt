package org.amogus.restarogus.repositories.interfaces

import java.math.BigDecimal

interface RestaurantStatsRepository {
    fun updateRevenue(revenue: BigDecimal)
    fun getRevenue(): BigDecimal
}