package dev.krazymanj.shopito.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.database.ShopitoDao
import dev.krazymanj.shopito.database.ShopitoLocalRepositoryImpl
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: ShopitoDao): IShopitoLocalRepository {
        return ShopitoLocalRepositoryImpl(dao)
    }
}