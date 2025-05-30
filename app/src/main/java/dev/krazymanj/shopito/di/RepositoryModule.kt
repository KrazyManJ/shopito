package dev.krazymanj.shopito.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.database.TemplateDao
import dev.krazymanj.shopito.database.TemplateLocalRepositoryImpl
import dev.krazymanj.shopito.database.ITemplateLocalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: TemplateDao): ITemplateLocalRepository {
        return TemplateLocalRepositoryImpl(dao)
    }
}