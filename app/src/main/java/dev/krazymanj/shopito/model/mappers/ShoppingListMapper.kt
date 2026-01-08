package dev.krazymanj.shopito.model.mappers

import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.model.network.NetworkShoppingList


fun ShoppingList.toNetworkModel(): NetworkShoppingList {
    return NetworkShoppingList(
        id = this.id,
        name = this.name,
        description = this.description,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        createdAt = this.createdAt
    )
}

fun NetworkShoppingList.toEntity(): ShoppingList {
    return ShoppingList(
        id = this.id,
        name = this.name,
        description = this.description,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        isDeleted = this.isDeleted,
        isSynced = true,
        isDirty = false
    )
}