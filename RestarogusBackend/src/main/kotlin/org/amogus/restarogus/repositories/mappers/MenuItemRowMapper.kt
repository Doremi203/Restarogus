package org.amogus.restarogus.repositories.mappers

import org.amogus.restarogus.models.MenuItem
import org.springframework.jdbc.core.RowMapper
import java.sql.ResultSet

class MenuItemRowMapper : RowMapper<MenuItem> {
    override fun mapRow(rs: ResultSet, rowNum: Int): MenuItem {
        return MenuItem(
            id = rs.getInt("id"),
            name = rs.getString("name"),
            price = rs.getDouble("price"),
            cookTimeInMinutes = rs.getInt("cook_time_in_minutes"),
            quantity = rs.getInt("quantity")
        )
    }
}