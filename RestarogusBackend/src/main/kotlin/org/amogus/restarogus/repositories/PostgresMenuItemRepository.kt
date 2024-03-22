package org.amogus.restarogus.repositories

import org.amogus.restarogus.models.MenuItem
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
    override fun add(item: MenuItem): Long {
        val keyHolder = GeneratedKeyHolder()

        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """INSERT INTO menu_items(name, price, cook_time_in_minutes, quantity, in_menu) 
                    VALUES (?, ?, ?, ?, ?)""".trimIndent(),
                Statement.RETURN_GENERATED_KEYS
            )
            preparedStatement.setString(1, item.name)
            preparedStatement.setBigDecimal(2, item.price)
            preparedStatement.setInt(3, item.cookTimeInMinutes)
            preparedStatement.setInt(4, item.quantity)
            preparedStatement.setBoolean(5, item.inMenu)
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
            throw NoSuchElementException("Item with id $id does not exist")
        }
    }

    override fun update(item: MenuItem) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE menu_items 
                    SET name = ?, price = ?, cook_time_in_minutes = ?, quantity = ?, in_menu = ?
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setString(1, item.name)
            preparedStatement.setBigDecimal(2, item.price)
            preparedStatement.setInt(3, item.cookTimeInMinutes)
            preparedStatement.setInt(4, item.quantity)
            preparedStatement.setBoolean(5, item.inMenu)
            preparedStatement.setLong(6, item.id)
            preparedStatement
        }

        val updatedCount = dataBase.update(preparedStatementCreator)

        if (updatedCount == 0) {
            throw NoSuchElementException("Item with id ${item.id} does not exist")
        }
    }

    override fun updateInMenuStatus(id: Long, inMenu: Boolean) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE menu_items 
                    SET in_menu = ? 
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setBoolean(1, inMenu)
            preparedStatement.setLong(2, id)
            preparedStatement
        }

        val updatedCount = dataBase.update(preparedStatementCreator)

        if (updatedCount == 0) {
            throw NoSuchElementException("Item with id $id does not exist")
        }
    }

    override fun updateQuantity(id: Long, quantity: Int) {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """UPDATE menu_items 
                    SET quantity = ? 
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setInt(1, quantity)
            preparedStatement.setLong(2, id)
            preparedStatement
        }

        val updatedCount = dataBase.update(preparedStatementCreator)

        if (updatedCount == 0) {
            throw NoSuchElementException("Item with id $id does not exist")
        }
    }

    override fun getById(id: Long): MenuItem {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT id, name, price, cook_time_in_minutes, quantity, in_menu
                    FROM menu_items 
                    WHERE id = ?""".trimIndent()
            )
            preparedStatement.setLong(1, id)
            preparedStatement
        }

        val menuItem = dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(MenuItem::class.java)
        ).firstOrNull()
            ?: throw NoSuchElementException("Item with id $id does not exist")

        return menuItem
    }

    override fun getAll(): List<MenuItem> {
        val preparedStatementCreator = PreparedStatementCreator { connection ->
            val preparedStatement = connection.prepareStatement(
                """SELECT id, name, price, cook_time_in_minutes, quantity, in_menu
                    FROM menu_items""".trimIndent()
            )
            preparedStatement
        }

        return dataBase.query(
            preparedStatementCreator,
            DataClassRowMapper.newInstance(MenuItem::class.java)
        )
    }
}

