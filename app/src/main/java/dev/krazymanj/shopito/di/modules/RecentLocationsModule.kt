package dev.krazymanj.shopito.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.core.IRecentLocationsManager
import dev.krazymanj.shopito.core.RecentLocationsManagerImpl
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecentLocationsModule {

    @Provides
    @Singleton
    fun provideRecentLocationsManager(
        dataStore: IDataStoreRepository,
        coroutineScope: CoroutineScope
    ): IRecentLocationsManager = RecentLocationsManagerImpl(dataStore, coroutineScope)
}




