package org.amogus.restarogus.repositories

import org.amogus.restarogus.repositories.dto.MenuItemDTO
import org.amogus.restarogus.repositories.interfaces.MenuItemRepository
import org.springframework.jdbc.core.DataClassRowMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.Statement

@Repository
class PostgresMenuItemRepository(
    private val dataBase: JdbcTemplate
) : MenuItemRepository {
    override fun add(item: MenuItemDTO): Long {
        val keyHolder = GeneratedKeyHolder()

        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """INSERT INTO menu_items(name, price, cook_time_in_minutes, quantity) 
                    VALUES (?, ?, ?, ?)""".trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            preparedStatement.setString(1, item.name)
            preparedStatement.setBigDecimal(2, item.price)
            preparedStatement.setInt(3, item.cookTimeInMinutes)
            preparedStatement.setInt(4, item.quantity)
            preparedStatement
        }
        dataBase.update(preparedStatementCreator, keyHolder)

        return keyHolder.keys!!["id"] as Long
    }

    override fun remove(id: Long) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """DELETE FROM menu_items WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, id)
            preparedStatement
        }

        val deletedCount = dataBase.update(preparedStatementCreator)

        if (deletedCount == 0) {
            throw IllegalArgumentException("Item with id $id does not exist")
        }
    }

    override fun update(item: MenuItemDTO) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE menu_items 
                    SET name = ?, price = ?, cook_time_in_minutes = ?, quantity = ? 
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setString(1, item.name)
            preparedStatement.setBigDecimal(2, item.price)
            preparedStatement.setInt(3, item.cookTimeInMinutes)
            preparedStatement.setInt(4, item.quantity)
            preparedStatement.setLong(5, item.id)
            preparedStatement
        }

        val updatedCount = dataBase.update(preparedStatementCreator)

        if (updatedCount == 0) {
            throw IllegalArgumentException("Item with id $item.id does not exist")
        }
    }

    override fun getById(id: Long): MenuItemDTO {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT id, name, price, cook_time_in_minutes, quantity 
                    FROM menu_items 
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, id)
            preparedStatement
        }

        val menuItem = dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(MenuItemDTO::class.java)
        ).firstOrNull()

        if (menuItem == null) {
            throw IllegalArgumentException("Item with id $id does not exist")
        }

        return menuItem
    }
}

