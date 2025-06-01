package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.flow.Flow

interface IShopitoLocalRepository {
    suspend fun insert(shoppingItem: ShoppingItem)
    suspend fun insert(shoppingList: ShoppingList)
    suspend fun getShoppingLists(): Flow<List<ShoppingList>>
}