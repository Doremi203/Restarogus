package org.amogus.restarogus.services.interfaces

import java.math.BigDecimal

interface RestaurantStatsService {
    fun updateRevenue(revenue: BigDecimal)
    fun getRevenue(): BigDecimal
}
