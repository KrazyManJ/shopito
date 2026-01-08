package dev.krazymanj.shopito.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkShoppingList(
    val id: String,

    val name: String,
    val description: String,

    @field:Json(name = "created_at")
    val createdAt: Long,

    @field:Json(name = "updated_at")
    val updatedAt: Long,

    @field:Json(name = "is_deleted")
    val isDeleted: Boolean
)