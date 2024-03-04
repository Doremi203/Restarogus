package org.amogus.restarogus.services.orderSystem

import org.amogus.restarogus.exceptions.IncorrectOrderStatusException
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.dto.OrderDTO
import org.amogus.restarogus.repositories.dto.OrderPositionDTO
import org.amogus.restarogus.repositories.dto.ReviewDTO
import org.amogus.restarogus.repositories.interfaces.MenuItemRepository
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.repositories.interfaces.ReviewRepository
import org.amogus.restarogus.requests.OrderRequest
import org.amogus.restarogus.requests.ReviewRequest
import org.amogus.restarogus.services.interfaces.RestaurantStatsService
import org.amogus.restarogus.services.interfaces.orderSystem.OrderService
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderPositionRepository: OrderPositionRepository,
    private val reviewRepository: ReviewRepository,
    private val restaurantStatsService: RestaurantStatsService,
    private val menuRepository: MenuItemRepository,
) : OrderService {
    @Transactional
    override fun placeOrder(customerId: Long, orderRequest: OrderRequest): Long {
        val orderId = orderRepository.add(
            OrderDTO(
                customerId = customerId,
                status = OrderStatus.PENDING,
                date = LocalDateTime.now()
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

        assertOrderOwnership(order, customerId)
        asserOrderPending(order)

        orderRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }

    override fun getOrderStatus(customerId: Long, orderId: Long): OrderStatus {
        val order = orderRepository.getById(orderId)

        assertOrderOwnership(order, customerId)

        return orderRepository.getById(orderId).status
    }

    override fun addPositions(customerId: Long, orderId: Long, orderRequest: OrderRequest) {
        val order = orderRepository.getById(orderId)

        assertOrderOwnership(order, customerId)
        asserOrderPending(order)

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

        assertOrderOwnership(order, customerId)
        assertOrderCanBePayed(order)

        val totalPrice = calculateTotalPrice(orderId)
        restaurantStatsService.updateRevenue(totalPrice)
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAYED)
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

    override fun addReview(customerId: Long, orderId: Long, review: ReviewRequest) {
        val order = orderRepository.getById(orderId)

        assertOrderOwnership(order, customerId)
        assertOrderCanBeReviewed(order)

        reviewRepository.add(
            ReviewDTO(
                orderId = orderId,
                rating = review.rating,
                comment = review.comment,
            )
        )
    }

    fun assertOrderOwnership(order: OrderDTO, customerId: Long) {
        if (order.customerId != customerId) {
            throw AccessDeniedException("Order does not belong to the customer")
        }
    }

    fun asserOrderPending(order: OrderDTO) {
        if (order.status == OrderStatus.FINISHED || order.status == OrderStatus.CANCELLED) {
            throw IncorrectOrderStatusException("Order is not pending")
        }
    }

    fun assertOrderCanBePayed(order: OrderDTO) {
        if (order.status != OrderStatus.FINISHED) {
            throw IncorrectOrderStatusException("Order is not finished")
        }
    }

    private fun assertOrderCanBeReviewed(order: OrderDTO) {
        if (order.status != OrderStatus.PAYED) {
            throw IncorrectOrderStatusException("Order is not payed")
        }
    }
}