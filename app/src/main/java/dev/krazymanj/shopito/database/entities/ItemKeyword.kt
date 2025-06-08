package dev.krazymanj.shopito.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item_keywords",
    indices = [Index(value=["value"], unique = true)]
)
data class ItemKeyword(
    val value: String,

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)