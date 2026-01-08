package dev.krazymanj.shopito.model.mappers

import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.model.network.NetworkShoppingItem

fun ShoppingItem.toNetworkModel(): NetworkShoppingItem {
    return NetworkShoppingItem(
        id = this.id,
        itemName = this.itemName,
        amount = this.amount,
        isDone = this.isDone,
        buyTime = this.buyTime,
        location = this.location,
        listId = this.listId,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted
    )
}

fun NetworkShoppingItem.toEntity(): ShoppingItem {
    return ShoppingItem(
        id = this.id,
        itemName = this.itemName,
        amount = this.amount,
        isDone = this.isDone,
        buyTime = this.buyTime,
        location = this.location,
        listId = this.listId,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        isSynced = true,
        isDirty = false
    )
}