package dev.krazymanj.shopito.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.krazymanj.shopito.database.entities.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopitoDao {

    @Insert
    suspend fun insert(templateClass: ShoppingItem)

    @Query("SELECT * FROM shopping_item")
    fun getAll(): Flow<List<ShoppingItem>>
}