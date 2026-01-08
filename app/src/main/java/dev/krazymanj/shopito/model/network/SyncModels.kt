package dev.krazymanj.shopito.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncRequest(
    val lists: List<NetworkShoppingList>,
    val items: List<NetworkShoppingItem>,
    @field:Json("last_sync_timestamp")
    val lastSyncTimestamp: Long
)

@JsonClass(generateAdapter = true)
data class SyncResponse(
    val lists: List<NetworkShoppingList>,
    val items: List<NetworkShoppingItem>,
    @field:Json("new_sync_timestamp")
    val newSyncTimestamp: Long
)