package org.amogus.restarogus.services.interfaces.orderSystem

import org.amogus.restarogus.models.MenuItem
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.requests.OrderRequest
import org.amogus.restarogus.requests.ReviewRequest
import java.math.BigDecimal

interface OrderService {
    fun placeOrder(orderRequest: OrderRequest): Long
    fun cancelOrder(orderId: Long)
    fun getOrderStatus(orderId: Long): OrderStatus
    fun addPositions(orderId: Long, orderRequest: OrderRequest)
    fun payOrder(orderId: Long)
    fun calculateTotalPrice(orderId: Long): BigDecimal
    fun addReview(orderId: Long, review: ReviewRequest)
    fun getMenu(): List<MenuItem>
}