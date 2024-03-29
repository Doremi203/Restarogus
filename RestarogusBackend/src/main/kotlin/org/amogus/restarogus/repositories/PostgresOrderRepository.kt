package org.amogus.restarogus.repositories

import org.amogus.restarogus.models.Order
import org.amogus.restarogus.models.OrderStatus
import org.amogus.restarogus.repositories.interfaces.OrderRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement
import java.sql.Timestamp

@Repository
class PostgresOrderRepository(
    private val dataBase: JdbcTemplate
) : OrderRepository {
    override fun add(order: Order): Long {
        val keyHolder = GeneratedKeyHolder()
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """INSERT INTO orders(customer_id, status, date) 
                    VALUES (?, ?, ?)""".trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            preparedStatement.setLong(1, order.customerId)
            preparedStatement.setString(2, order.status.name)
            preparedStatement.setTimestamp(3, Timestamp.valueOf(order.date))
            preparedStatement
        }

        dataBase.update(preparedStatementCreator, keyHolder)

        return keyHolder.keys!!["id"] as Long
    }

    override fun update(order: Order) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE orders 
                    SET customer_id = ?, status = ? 
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, order.customerId)
            preparedStatement.setString(2, order.status.name)
            preparedStatement.setLong(3, order.id)
            preparedStatement
        }

        dataBase.update(preparedStatementCreator)
    }

    override fun remove(orderId: Long) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """DELETE FROM orders WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, orderId)
            preparedStatement
        }

        val deletedCount = dataBase.update(preparedStatementCreator)

        if (deletedCount == 0) {
            throw IllegalArgumentException("Order with id $orderId does not exist")
        }
    }

    override fun getById(orderId: Long): Order {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT * FROM orders WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, orderId)
            preparedStatement
        }

        val order = dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(Order::class.java)
        ).firstOrNull()
            ?: throw NoSuchElementException("No order with id: $orderId")

        return order
    }

    override fun getAll(): List<Order> {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT * FROM orders""".trimIndent()
            )
            preparedStatement
        }

        return dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(Order::class.java)
        )
    }

    override fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE orders 
                    SET status = ? 
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setString(1, status.name)
            preparedStatement.setLong(2, orderId)
            preparedStatement
        }

        dataBase.update(preparedStatementCreator)
    }
}