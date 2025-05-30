package dev.krazymanj.shopito.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.database.TemplateDao
import dev.krazymanj.shopito.database.TemplateDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(database: TemplateDatabase): TemplateDao {
        return database.templateDao()
    }
}