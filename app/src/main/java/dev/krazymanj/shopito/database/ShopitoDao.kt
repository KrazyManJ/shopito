package dev.krazymanj.shopito.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopitoDao {

    @Insert
    suspend fun insert(shoppingItem: ShoppingItem)

    @Insert
    suspend fun insert(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_list")
    fun getShoppingLists(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    fun getShoppingListById(id: Long): Flow<ShoppingList>

    @Query("SELECT * FROM shopping_item WHERE listId = :id")
    fun getShoppingItemsByShoppingList(id: Long): Flow<List<ShoppingItem>>
}