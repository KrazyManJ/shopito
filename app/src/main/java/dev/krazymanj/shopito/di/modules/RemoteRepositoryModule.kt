package dev.krazymanj.shopito.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.communication.GeoReverseRepositoryImpl
import dev.krazymanj.shopito.communication.IGeoReverseRepository
import dev.krazymanj.shopito.communication.NominatimApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun provideGeoReverseRepository(petsAPI: NominatimApi): IGeoReverseRepository =
        GeoReverseRepositoryImpl(petsAPI)
}