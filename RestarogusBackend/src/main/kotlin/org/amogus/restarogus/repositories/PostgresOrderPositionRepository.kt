package org.amogus.restarogus.repositories

import org.amogus.restarogus.models.OrderPosition
import org.amogus.restarogus.repositories.interfaces.OrderPositionRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class PostgresOrderPositionRepository(
    private val dataBase: JdbcTemplate
) : OrderPositionRepository {
    override fun add(order: OrderPosition): Long {
        val keyHolder = GeneratedKeyHolder()
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """INSERT INTO order_positions(order_id, menu_item_id, quantity, quantity_done) 
                    VALUES (?, ?, ?, ?)""".trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            preparedStatement.setLong(1, order.orderId)
            preparedStatement.setLong(2, order.menuItemId)
            preparedStatement.setInt(3, order.quantity)
            preparedStatement.setInt(4, order.quantityDone)
            preparedStatement
        }

        dataBase.update(preparedStatementCreator, keyHolder)

        return keyHolder.keys!!["id"] as Long
    }

    override fun remove(id: Long) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """DELETE FROM order_positions WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, id)
            preparedStatement
        }

        val deletedCount = dataBase.update(preparedStatementCreator)

        if (deletedCount == 0) {
            throw NoSuchElementException("Order position with id $id does not exist")
        }
    }

    override fun update(orderPosition: OrderPosition) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE order_positions 
                    SET order_id = ?, menu_item_id = ?, quantity = ?, quantity_done = ?
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, orderPosition.orderId)
            preparedStatement.setLong(2, orderPosition.menuItemId)
            preparedStatement.setInt(3, orderPosition.quantity)
            preparedStatement.setInt(4, orderPosition.quantityDone)
            preparedStatement.setLong(5, orderPosition.id)
            preparedStatement
        }

        val updatedCount = dataBase.update(preparedStatementCreator)

        if (updatedCount == 0) {
            throw NoSuchElementException("Order position with id ${orderPosition.id} does not exist")
        }
    }

    override fun updateQuantityDone(id: Long, quantityDone: Int) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE order_positions 
                    SET quantity_done = ?
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setInt(1, quantityDone)
            preparedStatement.setLong(2, id)
            preparedStatement
        }

        val updatedCount = dataBase.update(preparedStatementCreator)

        if (updatedCount == 0) {
            throw NoSuchElementException("Order position with id $id does not exist")
        }
    }

    override fun getAll(): List<OrderPosition> {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT * FROM order_positions""".trimIndent()
            )
            preparedStatement
        }

        return dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(OrderPosition::class.java)
        )
    }

    override fun getAllByOrderId(orderId: Long): List<OrderPosition> {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT * FROM order_positions WHERE order_id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, orderId)
            preparedStatement
        }

        return dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(OrderPosition::class.java)
        )
    }

}