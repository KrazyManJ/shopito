package dev.krazymanj.shopito.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopitoDao {

    @Insert
    suspend fun insert(shoppingItem: ShoppingItem)

    @Insert
    suspend fun insert(shoppingList: ShoppingList)

    @Update
    suspend fun update(shoppingItem: ShoppingItem)

    @Update
    suspend fun update(shoppingList: ShoppingList)

    @Delete
    suspend fun delete(shoppingItem: ShoppingItem)

    @Delete
    suspend fun delete(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_list")
    fun getAllShoppingLists(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_list WHERE id = :id")
    suspend fun getShoppingListById(id: Long): ShoppingList

    @Query("""
        SELECT *
        FROM shopping_item
        WHERE listId = :id
        ORDER BY
            CASE WHEN buyTime IS NULL THEN 1 ELSE 0 END,
            buyTime ASC
     """)
    fun getShoppingItemsByShoppingList(id: Long): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_item WHERE id=:id")
    suspend fun getShoppingItemById(id: Long): ShoppingItem

    @Transaction
    @Query("SELECT * FROM shopping_item")
    fun getAllShoppingItemsWithLists(): Flow<List<ShoppingItemWithList>>
}