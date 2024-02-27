package org.amogus.restarogus.services.interfaces

import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.requests.OrderRequest
import java.math.BigDecimal

interface OrderService {
    fun placeOrder(customerId: Long, orderRequest: OrderRequest): Long
    fun cancelOrder(customerId: Long, orderId: Long)
    fun getOrderStatus(customerId: Long, orderId: Long): OrderStatus
    fun addPositions(customerId: Long, orderId: Long, orderRequest: OrderRequest)
    fun payOrder(customerId: Long, orderId: Long)
    fun calculateTotalPrice(orderId: Long): BigDecimal
}