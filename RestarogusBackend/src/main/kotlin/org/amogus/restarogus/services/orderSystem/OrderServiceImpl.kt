package org.amogus.restarogus.services.orderSystem

import org.amogus.restarogus.exceptions.DuplicateReviewException
import org.amogus.restarogus.models.*
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.amogus.restarogus.repositories.interfaces.ReviewRepository
import org.amogus.restarogus.requests.OrderRequest
import org.amogus.restarogus.requests.ReviewRequest
import org.amogus.restarogus.services.interfaces.MenuItemService
import org.amogus.restarogus.services.interfaces.RestaurantStatsService
import org.amogus.restarogus.services.interfaces.orderSystem.OrderService
import org.amogus.restarogus.validators.OrderServiceValidator
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.core.context.SecurityContextHolder
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
    private val menuItemService: MenuItemService,
    private val orderServiceValidator: OrderServiceValidator
) : OrderService {
    @Transactional
    override fun placeOrder(orderRequest: OrderRequest): Long {
        val user = SecurityContextHolder.getContext().authentication.principal as User
        val orderId = orderRepository.add(
            Order(
                customerId = user.id,
                status = OrderStatus.PENDING,
                date = LocalDateTime.now()
            )
        )

        for (menuItem in orderRequest.menuItems) {
            orderServiceValidator.assertMenuItemInMenu(menuItem.id)
            orderPositionRepository.add(
                OrderPosition(
                    orderId = orderId,
                    menuItemId = menuItem.id,
                    quantity = menuItem.quantity,
                )
            )
        }

        return orderId
    }

    override fun cancelOrder(orderId: Long) {
        val order = orderRepository.getById(orderId)

        orderServiceValidator.assertOrderOwnershipWithAdminAccess(order)
        orderServiceValidator.asserOrderPending(order)

        orderRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }

    override fun getOrderStatus(orderId: Long): OrderStatus {
        val order = orderRepository.getById(orderId)

        orderServiceValidator.assertOrderOwnershipWithAdminAccess(order)

        return orderRepository.getById(orderId).status
    }

    override fun addPositions(orderId: Long, orderRequest: OrderRequest) {
        val order = orderRepository.getById(orderId)

        orderServiceValidator.assertOrderOwnership(order)
        orderServiceValidator.asserOrderPending(order)

        for (menuItem in orderRequest.menuItems) {
            orderServiceValidator.assertMenuItemInMenu(menuItem.id)
            orderPositionRepository.add(
                OrderPosition(
                    orderId = orderId,
                    menuItemId = menuItem.id,
                    quantity = menuItem.quantity,
                )
            )
        }
    }

    override fun payOrder(orderId: Long) {
        val order = orderRepository.getById(orderId)

        orderServiceValidator.assertOrderOwnership(order)
        orderServiceValidator.assertOrderCanBePayed(order)

        val totalPrice = calculateTotalPrice(orderId)
        restaurantStatsService.updateRevenue(totalPrice)
        orderRepository.updateOrderStatus(orderId, OrderStatus.PAYED)
    }

    override fun calculateTotalPrice(orderId: Long): BigDecimal {
        var totalPrice = BigDecimal.ZERO
        val orderPositions = orderPositionRepository.getAllByOrderId(orderId)

        for (orderPosition in orderPositions) {
            val menuItem = menuItemService.getMenuItemById(orderPosition.menuItemId)
            totalPrice += menuItem.price * orderPosition.quantity.toBigDecimal()
        }

        return totalPrice
    }

    override fun addReview(orderId: Long, review: ReviewRequest) {
        val order = orderRepository.getById(orderId)

        orderServiceValidator.assertOrderOwnership(order)
        orderServiceValidator.assertOrderCanBeReviewed(order)

        try {
            reviewRepository.add(
                Review(
                    orderId = orderId,
                    rating = review.rating,
                    comment = review.comment,
                )
            )
        } catch (e: DataIntegrityViolationException) {
            throw DuplicateReviewException()
        }
    }

    override fun getMenu(): List<MenuItem> {
        return menuItemService.getMenuItems().filter { it.inMenu }
    }
}