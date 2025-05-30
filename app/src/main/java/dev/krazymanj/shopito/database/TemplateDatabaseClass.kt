package dev.krazymanj.shopito.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "template_table")
data class TemplateDatabaseClass(
    // Variables here

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
)
