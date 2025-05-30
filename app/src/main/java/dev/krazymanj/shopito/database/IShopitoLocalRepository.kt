package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface IShopitoLocalRepository {
    suspend fun insert(shoppingItem: ShoppingItem)
    suspend fun getAll(): Flow<List<ShoppingItem>>
}