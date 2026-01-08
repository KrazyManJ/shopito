package dev.krazymanj.shopito.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.ShoppingItemWithList
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopitoDao {

    @Insert suspend fun insert(shoppingList: ShoppingList)
    @Update suspend fun update(shoppingList: ShoppingList)
    @Upsert suspend fun upsert(shoppingList: ShoppingList)
    @Delete suspend fun delete(shoppingList: ShoppingList)

    @Query("SELECT * FROM shopping_list WHERE isDeleted = 0 ORDER BY createdAt ASC")
    fun getAllShoppingLists(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_list WHERE id = :id AND isDeleted = 0")
    suspend fun getShoppingListById(id: String): ShoppingList?

    @Insert suspend fun insert(shoppingItem: ShoppingItem)
    @Update suspend fun update(shoppingItem: ShoppingItem)
    @Upsert suspend fun upsert(shoppingItem: ShoppingItem)
    @Delete suspend fun delete(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM shopping_item WHERE id=:id AND isDeleted = 0")
    suspend fun getShoppingItemById(id: String): ShoppingItem

    @Query("""
        SELECT *
        FROM shopping_item
        WHERE listId = :id AND isDeleted = 0
        ORDER BY
            CASE WHEN buyTime IS NULL THEN 1 ELSE 0 END,
            buyTime ASC,
            createdAt ASC
     """)
    fun getShoppingItemsByShoppingList(id: String): Flow<List<ShoppingItem>>

    @Transaction
    @Query("""
        SELECT * 
        FROM shopping_item
        WHERE isDeleted = 0
        ORDER BY
            CASE WHEN buyTime IS NULL THEN 1 ELSE 0 END,
            buyTime ASC,
            createdAt ASC
    """)
    fun getAllShoppingItemsWithLists(): Flow<List<ShoppingItemWithList>>

    @Query("DELETE FROM shopping_item WHERE listId = :listId AND isDone = 1 AND isSynced = 0")
    suspend fun hardDeleteUnknownCheckedItems(listId: String)

    @Query("""
        UPDATE shopping_item 
        SET isDeleted = 1, 
            updatedAt = :updateTime, 
            isDirty = 1 
        WHERE listId = :listId 
          AND isDone = 1 
          AND isDeleted = 0
    """)
    suspend fun softDeleteKnownCheckedItems(listId: String, updateTime: Long = System.currentTimeMillis())

    @Transaction
    suspend fun removeAllCheckedItemsInShoppingList(listId: String) {
        hardDeleteUnknownCheckedItems(listId)
        softDeleteKnownCheckedItems(listId, System.currentTimeMillis())
    }

    @Query("SELECT DISTINCT latitude, longitude FROM shopping_item WHERE latitude IS NOT NULL AND longitude IS NOT NULL AND isDeleted = 0")
    fun getAllDistinctLocationsFromItems(): Flow<List<Location>>

    @Query("""
        SELECT *
        FROM shopping_item
        WHERE latitude = :latitude AND longitude = :longitude AND isDeleted = 0
        ORDER BY createdAt ASC
    """)
    fun getItemsByLocation(latitude: Double, longitude: Double): Flow<List<ShoppingItemWithList>>

    @Query("SELECT * FROM shopping_list WHERE isDirty = 1")
    suspend fun getDirtyLists(): List<ShoppingList>

    @Query("SELECT * FROM shopping_item WHERE isDirty = 1")
    suspend fun getDirtyItems(): List<ShoppingItem>

    @Upsert
    suspend fun upsertLists(lists: List<ShoppingList>)

    @Upsert
    suspend fun upsertItems(items: List<ShoppingItem>)

    @Query("UPDATE shopping_item SET isDirty = 0, isSynced = 1 WHERE id IN (:ids)")
    suspend fun markItemsAsSynced(ids: List<String>)

    @Query("UPDATE shopping_list SET isDirty = 0, isSynced = 1 WHERE id IN (:ids)")
    suspend fun markListsAsSynced(ids: List<String>)

    @Query("DELETE FROM shopping_list WHERE isDeleted = 1 AND isDirty = 0")
    suspend fun pruneSyncedDeletedLists()

    @Query("DELETE FROM shopping_item WHERE isDeleted = 1 AND isDirty = 0")
    suspend fun pruneSyncedDeletedItems()

    @Transaction
    suspend fun pruneGlobally() {
        pruneSyncedDeletedLists()
        pruneSyncedDeletedItems()
    }
}