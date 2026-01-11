package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.ShoppingItemWithList
import kotlinx.coroutines.flow.Flow

interface IShopitoLocalRepository {
    suspend fun upsert(shoppingList: ShoppingList)
    suspend fun delete(shoppingList: ShoppingList)
    suspend fun getShoppingLists(): Flow<List<ShoppingList>>
    suspend fun getShoppingListsByActivity(): Flow<List<ShoppingList>>
    suspend fun getShoppingListById(id: String): ShoppingList?
    suspend fun upsert(shoppingItem: ShoppingItem)
    suspend fun upsertMany(shoppingItems: List<ShoppingItem>)
    suspend fun delete(shoppingItem: ShoppingItem)
    suspend fun deleteMany(shoppingItems: List<ShoppingItem>)
    suspend fun getShoppingItemsByShoppingList(id: String): Flow<List<ShoppingItem>>
    suspend fun getCheckedShoppingItemsByShoppingList(id: String): List<ShoppingItem>
    suspend fun getShoppingItemById(id: String): ShoppingItem

    suspend fun getShoppingItemsGroupedByDate(): Flow<Map<Long, List<ShoppingItemWithList>>>
    suspend fun getShoppingItemsWithoutBuyTime(): Flow<List<ShoppingItemWithList>>

    suspend fun getAllDistinctLocationsFromItems(): Flow<List<Location>>
    suspend fun getItemsByLocation(location: Location): Flow<List<ShoppingItemWithList>>
    suspend fun wipeAllData()
}