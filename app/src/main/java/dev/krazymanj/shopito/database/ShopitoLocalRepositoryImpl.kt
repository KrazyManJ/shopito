package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.ShoppingItemWithList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShopitoLocalRepositoryImpl @Inject constructor(private val shopitoDao: ShopitoDao) :
    IShopitoLocalRepository {
    override suspend fun insert(shoppingItem: ShoppingItem) {
        return shopitoDao.insert(shoppingItem)
    }

    override suspend fun insert(shoppingList: ShoppingList) {
        return shopitoDao.insert(shoppingList)
    }

    override suspend fun update(shoppingItem: ShoppingItem) {
        return shopitoDao.update(shoppingItem)
    }

    override suspend fun update(shoppingList: ShoppingList) {
        return shopitoDao.update(shoppingList)
    }

    override suspend fun upsert(shoppingList: ShoppingList) {
        return shopitoDao.upsert(shoppingList)
    }

    override suspend fun delete(shoppingItem: ShoppingItem) {
        return shopitoDao.delete(shoppingItem)
    }

    override suspend fun delete(shoppingList: ShoppingList) {
        return shopitoDao.delete(shoppingList)
    }

    override suspend fun getShoppingLists(): Flow<List<ShoppingList>> {
        return shopitoDao.getAllShoppingLists()
    }

    override suspend fun getShoppingListById(id: String): ShoppingList? {
        return shopitoDao.getShoppingListById(id)
    }

    override suspend fun getShoppingItemsByShoppingList(id: String): Flow<List<ShoppingItem>> {
        return shopitoDao.getShoppingItemsByShoppingList(id)
    }

    override suspend fun getShoppingItemById(id: String): ShoppingItem {
        return shopitoDao.getShoppingItemById(id)
    }

    override suspend fun getShoppingItemsGroupedByDate(): Flow<Map<Long, List<ShoppingItemWithList>>> {
        val now = System.currentTimeMillis()
        return shopitoDao.getAllShoppingItemsWithLists().map { list ->
            list
                .filter { it.item.buyTime != null && (!it.item.isDone || it.item.buyTime!! >= now) }
                .groupBy { it.item.buyTime ?: -1L }
        }
    }

    override suspend fun getShoppingItemsWithoutBuyTime(): Flow<List<ShoppingItemWithList>> {
        return shopitoDao.getAllShoppingItemsWithLists().map { list ->
            list.filter { it.item.buyTime == null && !it.item.isDone}
        }
    }

    override suspend fun removeAllCheckedItemsInShoppingList(listId: String) {
        return shopitoDao.removeAllCheckedItemsInShoppingList(listId)
    }

    override suspend fun getAllDistinctLocationsFromItems(): Flow<List<Location>> {
        return shopitoDao.getAllDistinctLocationsFromItems()
    }

    override suspend fun getItemsByLocation(location: Location): Flow<List<ShoppingItemWithList>> {
        return shopitoDao.getItemsByLocation(location.latitude, location.longitude)
    }
}