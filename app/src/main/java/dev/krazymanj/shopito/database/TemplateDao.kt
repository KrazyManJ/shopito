package dev.krazymanj.shopito.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TemplateDao {

    @Insert
    suspend fun insert(templateClass: TemplateDatabaseClass)

    @Query("SELECT * FROM template_table")
    fun getAll(): Flow<List<TemplateDatabaseClass>>
}