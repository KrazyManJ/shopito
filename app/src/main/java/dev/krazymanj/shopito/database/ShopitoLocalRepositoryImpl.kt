package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.Location
import dev.krazymanj.shopito.model.ShoppingItemWithList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ShopitoLocalRepositoryImpl @Inject constructor(
    private val shopitoDao: ShopitoDao
) :
    IShopitoLocalRepository {
    override suspend fun insert(shoppingItem: ShoppingItem) {
        return shopitoDao.insert(shoppingItem.withUpdatedAt())
    }

    override suspend fun insert(shoppingList: ShoppingList) {
        return shopitoDao.insert(shoppingList.withUpdatedAt())
    }

    override suspend fun update(shoppingItem: ShoppingItem) {
        return shopitoDao.update(shoppingItem.withUpdatedAt())
    }

    override suspend fun upsert(shoppingItem: ShoppingItem) {
        return shopitoDao.upsert(shoppingItem.withUpdatedAt())
    }

    override suspend fun update(shoppingList: ShoppingList) {
        return shopitoDao.update(shoppingList.withUpdatedAt())
    }

    override suspend fun upsert(shoppingList: ShoppingList) {
        return shopitoDao.upsert(shoppingList.withUpdatedAt())
    }

    override suspend fun delete(shoppingItem: ShoppingItem) {
        if (shoppingItem.isSynced) {
            shopitoDao.update(shoppingItem.withUpdatedAt().copy(isDeleted = true))
        }
        else {
            shopitoDao.delete(shoppingItem)
        }
    }

    override suspend fun delete(shoppingList: ShoppingList) {
        if (shoppingList.isSynced) {
            shopitoDao.softDeleteListWithItems(shoppingList.id)
        }
        else {
            shopitoDao.delete(shoppingList)
        }
    }

    override suspend fun getShoppingLists(): Flow<List<ShoppingList>> {
        return shopitoDao.getAllShoppingLists()
    }

    override suspend fun getShoppingListsByActivity(): Flow<List<ShoppingList>> {
        return shopitoDao.getAllShoppingListsByActivity()
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

    override suspend fun wipeAllData() {
        return shopitoDao.clearAllTables()
    }
}