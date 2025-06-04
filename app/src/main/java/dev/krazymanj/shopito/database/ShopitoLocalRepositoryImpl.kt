package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShopitoLocalRepositoryImpl @Inject constructor(private val shopitoDao: ShopitoDao) :
    IShopitoLocalRepository {
    override suspend fun insert(shoppingItem: ShoppingItem) {
        return shopitoDao.insert(shoppingItem)
    }

    override suspend fun insert(shoppingList: ShoppingList) {
        return shopitoDao.insert(shoppingList)
    }

    override suspend fun getShoppingLists(): Flow<List<ShoppingList>> {
        return shopitoDao.getShoppingLists()
    }

    override suspend fun getShoppingListById(id: Long): Flow<ShoppingList> {
        return shopitoDao.getShoppingListById(id)
    }

    override suspend fun getShoppingItemsByShoppingList(id: Long): Flow<List<ShoppingItem>> {
        return shopitoDao.getShoppingItemsByShoppingList(id)
    }
}