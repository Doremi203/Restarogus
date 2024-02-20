package org.amogus.restarogus.repositories

import org.amogus.restarogus.entities.MenuItemEntity
import org.amogus.restarogus.entities.MenuItemUpdateEntity
import org.amogus.restarogus.models.MenuItem
import org.springframework.dao.DuplicateKeyException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class MenuRepositoryImpl(
    private val dataBase: JdbcTemplate
) : MenuRepository {
    override fun addMenuItem(item: MenuItemEntity): Int {
        val sqlQuery = """
        INSERT INTO menu_items(id, name, price, cook_time_in_minutes, quantity) 
        VALUES (nextval('menu_item_id_sequence'), ?, ?, ?, ?) 
        RETURNING id;
    """.trimIndent()

        return try {
            dataBase.queryForObject(
                sqlQuery,
                Int::class.java,
                item.name,
                item.price,
                item.cookTimeInMinutes,
                item.quantity
            )
        } catch (e: DuplicateKeyException) {
            throw DuplicateKeyException("Item with name ${item.name} already exists")
        }
    }

    override fun removeMenuItem(id: Int) {
        val sqlQuery = """
        DELETE FROM menu_items WHERE id = ?;
    """.trimIndent()

        dataBase.update(sqlQuery, id)
    }

    override fun updateMenuItem(id: Int, item: MenuItemUpdateEntity) {
        val sqlQuery = """
        UPDATE menu_items 
        SET name = COALESCE(?, name), 
            price = COALESCE(?, price), 
            cook_time_in_minutes = COALESCE(?, cook_time_in_minutes), 
            quantity = COALESCE(?, quantity) 
        WHERE id = ?;
    """.trimIndent()

        dataBase.update(
            sqlQuery,
            item.name,
            item.price,
            item.cookTimeInMinutes,
            item.quantity,
            id
        )
    }

    override fun getMenuItem(id: Int): MenuItem {
        TODO("Not yet implemented")
    }
}