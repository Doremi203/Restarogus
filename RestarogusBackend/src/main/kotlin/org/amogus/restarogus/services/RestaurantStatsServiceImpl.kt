package org.amogus.restarogus.services

import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.repositories.interfaces.RestaurantStatsRepository
import org.amogus.restarogus.repositories.interfaces.ReviewRepository
import org.amogus.restarogus.services.interfaces.MenuItemService
import org.amogus.restarogus.services.interfaces.RestaurantStatsService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class RestaurantStatsServiceImpl(
    private val restaurantStatsRepository: RestaurantStatsRepository,
    private val orderRepository: OrderRepository,
    private val orderPositionRepository: OrderPositionRepository,
    private val reviewRepository: ReviewRepository,
    private val menuItemService: MenuItemService
) : RestaurantStatsService {
    override fun updateRevenue(revenue: BigDecimal) {
        val currentRevenue = getRevenue()
        val newRevenue = currentRevenue + revenue

        restaurantStatsRepository.updateRevenue(newRevenue)
    }

    override fun getRevenue(): BigDecimal {
        return restaurantStatsRepository.getRevenue()
    }

    override fun getLoss(): BigDecimal {
        val orders = orderRepository.getAll().filter { it.status == OrderStatus.CANCELLED }

        val orderPositions = orders.flatMap { order ->
            orderPositionRepository.getAllByOrderId(order.id)
        }

        return orderPositions.sumOf { position ->
            menuItemService.getMenuItemById(position.menuItemId).price * position.quantityDone.toBigDecimal()
        }
    }

    override fun getMostPopularDish(): String {
        val orders = orderRepository.getAll().filter { it.status == OrderStatus.PAYED }

        val orderPositions = orders.flatMap { order ->
            orderPositionRepository.getAllByOrderId(order.id)
        }

        val orderPositionsByMenuItemId = orderPositions.groupBy { it.menuItemId }

        val quantityDoneByMenuItemId = orderPositionsByMenuItemId.mapValues { (_, orderPositions) ->
            orderPositions.sumOf { it.quantityDone }
        }

        val mostPopularMenuItemId = quantityDoneByMenuItemId.maxByOrNull { it.value }?.key

        if (mostPopularMenuItemId == null) {
            return "No popular dish"
        }

        return menuItemService.getMenuItemById(mostPopularMenuItemId).name
    }

        override fun getAverageRating(): Double {
            val reviews = reviewRepository.getAll()
            if (reviews.isEmpty()) {
                return 0.0
            }
            return reviews.map { it.rating }.average()
        }

    override fun getOrdersCountOverPeriod(from: LocalDateTime, to: LocalDateTime): Int {
        val orders = orderRepository.getAll().filter { it.date in from..to }
        return orders.size
    }
}