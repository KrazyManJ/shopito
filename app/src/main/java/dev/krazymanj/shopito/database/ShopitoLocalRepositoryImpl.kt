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


    override suspend fun upsert(shoppingItem: ShoppingItem) {
        return shopitoDao.upsert(shoppingItem.withUpdatedAt())
    }

    override suspend fun upsertMany(shoppingItems: List<ShoppingItem>) {
        return shopitoDao.upsertMany(shoppingItems.map { it.withUpdatedAt() })
    }

    override suspend fun upsert(shoppingList: ShoppingList) {
        return shopitoDao.upsert(shoppingList.withUpdatedAt())
    }

    override suspend fun delete(shoppingItem: ShoppingItem) {
        if (shoppingItem.isSynced) {
            shopitoDao.upsert(shoppingItem.withUpdatedAt().copy(isDeleted = true))
        } else {
            shopitoDao.delete(shoppingItem)
        }
    }

    override suspend fun deleteMany(shoppingItems: List<ShoppingItem>) {
        val (syncedItems, notSyncedItems) = shoppingItems.partition { it.isSynced }
        if (notSyncedItems.isNotEmpty()) {
            shopitoDao.deleteMany(notSyncedItems)
        }
        if (syncedItems.isNotEmpty()) {
            shopitoDao.upsertMany(syncedItems.map { it.withUpdatedAt().copy(isDeleted = true) })
        }
    }

    override suspend fun delete(shoppingList: ShoppingList) {
        if (shoppingList.isSynced) {
            shopitoDao.softDeleteListWithItems(shoppingList.id)
        } else {
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

    override suspend fun getCheckedShoppingItemsByShoppingList(id: String): List<ShoppingItem> {
        return shopitoDao.getCheckedShoppingItemsByShoppingList(id)
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
            list.filter { it.item.buyTime == null && !it.item.isDone }
        }
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