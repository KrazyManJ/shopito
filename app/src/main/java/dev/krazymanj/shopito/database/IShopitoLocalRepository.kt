package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.ShoppingItemWithList
import kotlinx.coroutines.flow.Flow

interface IShopitoLocalRepository {
    suspend fun insert(shoppingList: ShoppingList)
    suspend fun update(shoppingList: ShoppingList)
    suspend fun delete(shoppingList: ShoppingList)
    suspend fun getShoppingLists(): Flow<List<ShoppingList>>
    suspend fun getShoppingListById(id: Long): ShoppingList

    suspend fun insert(shoppingItem: ShoppingItem)
    suspend fun update(shoppingItem: ShoppingItem)
    suspend fun delete(shoppingItem: ShoppingItem)
    suspend fun getShoppingItemsByShoppingList(id: Long): Flow<List<ShoppingItem>>
    suspend fun getShoppingItemById(id: Long): ShoppingItem

    suspend fun getShoppingItemsGroupedByDate(): Flow<Map<Long, List<ShoppingItemWithList>>>
    suspend fun getShoppingItemsWithoutBuyTime(): Flow<List<ShoppingItemWithList>>

    suspend fun removeAllCheckedItemsInShoppingList(listId: Long)

    suspend fun getAllDistinctLocationsFromItems(): Flow<List<Location>>
    suspend fun getItemsByLocation(location: Location): Flow<List<ShoppingItemWithList>>
}