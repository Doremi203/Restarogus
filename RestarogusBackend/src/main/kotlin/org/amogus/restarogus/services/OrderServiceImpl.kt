package org.amogus.restarogus.services

import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.dto.OrderDTO
import org.amogus.restarogus.repositories.dto.OrderPositionDTO
import org.amogus.restarogus.repositories.interfaces.MenuItemsRepository
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.requests.OrderRequest
import org.amogus.restarogus.services.interfaces.OrderService
import org.amogus.restarogus.services.interfaces.RestaurantStatsService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderPositionRepository: OrderPositionRepository,
    private val restaurantStatsService: RestaurantStatsService,
    private val menuRepository: MenuItemsRepository,
) : OrderService {
    override fun placeOrder(customerId: Long, orderRequest: OrderRequest): Long {
        val orderId = orderRepository.add(
            OrderDTO(
                customerId = customerId,
                status = OrderStatus.PENDING,
            )
        )

        for (menuItem in orderRequest.menuItems) {
            orderPositionRepository.add(
                OrderPositionDTO(
                    orderId = orderId,
                    menuItemId = menuItem.id,
                    quantity = menuItem.quantity,
                )
            )
        }

        return orderId
    }

    override fun cancelOrder(customerId: Long, orderId: Long) {
        val order = orderRepository.getById(orderId)
        if (order.customerId != customerId) {
            throw IllegalArgumentException("Order does not belong to the customer")
        }
        if (order.status == OrderStatus.FINISHED || order.status == OrderStatus.CANCELLED) {
            throw IllegalArgumentException("Order is not pending")
        }

        orderRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }

    override fun getOrderStatus(customerId: Long, orderId: Long): OrderStatus {
        val order = orderRepository.getById(orderId)
        if (order.customerId != customerId) {
            throw IllegalArgumentException("Order does not belong to the customer")
        }

        return orderRepository.getById(orderId).status
    }

    override fun addPositions(customerId: Long, orderId: Long, orderRequest: OrderRequest) {
        val order = orderRepository.getById(orderId)
        if (order.customerId != customerId) {
            throw IllegalArgumentException("Order does not belong to the customer")
        }
        if (order.status == OrderStatus.FINISHED || order.status == OrderStatus.CANCELLED) {
            throw IllegalArgumentException("Order is not pending")
        }

        for (menuItem in orderRequest.menuItems) {
            orderPositionRepository.add(
                OrderPositionDTO(
                    orderId = orderId,
                    menuItemId = menuItem.id,
                    quantity = menuItem.quantity,
                )
            )
        }
    }

    override fun payOrder(customerId: Long, orderId: Long) {
        val order = orderRepository.getById(orderId)
        if (order.customerId != customerId) {
            throw IllegalArgumentException("Order does not belong to the customer")
        }
        if (order.status != OrderStatus.FINISHED) {
            throw IllegalArgumentException("Order is not finished")
        }

        val totalPrice = calculateTotalPrice(orderId)
        restaurantStatsService.updateRevenue(totalPrice)
    }

    override fun calculateTotalPrice(orderId: Long): BigDecimal {
        var totalPrice = BigDecimal.ZERO
        val orderPositions = orderPositionRepository.getAllByOrderId(orderId)

        for (orderPosition in orderPositions) {
            val menuItem = menuRepository.getById(orderPosition.menuItemId)
            totalPrice += menuItem.price * orderPosition.quantity.toBigDecimal()
        }

        return totalPrice
    }
}