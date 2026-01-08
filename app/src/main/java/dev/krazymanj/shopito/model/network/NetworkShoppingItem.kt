package dev.krazymanj.shopito.model.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.krazymanj.shopito.model.Location

@JsonClass(generateAdapter = true)
data class NetworkShoppingItem(
    val id: String,

    @field:Json(name = "item_name")
    val itemName: String,

    val amount: Int,

    @field:Json(name = "is_done")
    val isDone: Boolean,

    @field:Json(name = "buy_time")
    val buyTime: Long?,

    val location: Location?,

    @field:Json(name = "list_id")
    val listId: String?,

    @field:Json(name = "updated_at")
    val updatedAt: Long,

    @field:Json(name = "is_deleted")
    val isDeleted: Boolean
)