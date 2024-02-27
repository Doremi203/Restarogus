package org.amogus.restarogus.services

import org.amogus.restarogus.repositories.interfaces.RestaurantStatsRepository
import org.amogus.restarogus.services.interfaces.RestaurantStatsService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class RestaurantStatsServiceImpl(
    private val restaurantStatsRepository: RestaurantStatsRepository
) : RestaurantStatsService {
    override fun updateRevenue(revenue: BigDecimal) {
        val currentRevenue = getRevenue()
        val newRevenue = currentRevenue + revenue

        restaurantStatsRepository.updateRevenue(newRevenue)
    }

    override fun getRevenue(): BigDecimal {
        return restaurantStatsRepository.getRevenue()
    }
}