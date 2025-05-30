package dev.krazymanj.shopito.database

import dev.krazymanj.shopito.database.ITemplateLocalRepository
import dev.krazymanj.shopito.database.TemplateDao
import dev.krazymanj.shopito.database.TemplateDatabaseClass
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TemplateLocalRepositoryImpl @Inject constructor(private val templateDao: TemplateDao) :
    ITemplateLocalRepository {
    override suspend fun insert(templateClass: TemplateDatabaseClass) {
        return templateDao.insert(templateClass)
    }

    override suspend fun getAll(): Flow<List<TemplateDatabaseClass>> {
        return templateDao.getAll()
    }
}