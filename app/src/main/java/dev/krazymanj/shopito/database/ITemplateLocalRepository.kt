package dev.krazymanj.shopito.database

import kotlinx.coroutines.flow.Flow

interface ITemplateLocalRepository {
    suspend fun insert(templateClass: TemplateDatabaseClass)
    suspend fun getAll(): Flow<List<TemplateDatabaseClass>>
}