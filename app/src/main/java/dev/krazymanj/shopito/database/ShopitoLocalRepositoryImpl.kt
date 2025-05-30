package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShopitoLocalRepositoryImpl @Inject constructor(private val shopitoDao: ShopitoDao) :
    IShopitoLocalRepository {
    override suspend fun insert(shoppingItem: ShoppingItem) {
        return shopitoDao.insert(shoppingItem)
    }

    override suspend fun getAll(): Flow<List<ShoppingItem>> {
        return shopitoDao.getAll()
    }
}