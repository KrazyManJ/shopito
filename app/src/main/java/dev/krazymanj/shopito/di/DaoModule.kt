package dev.krazymanj.shopito.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.database.ShopitoDao
import dev.krazymanj.shopito.database.ShopitoDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun provideDao(database: ShopitoDatabase): ShopitoDao {
        return database.shopitoDao()
    }
}