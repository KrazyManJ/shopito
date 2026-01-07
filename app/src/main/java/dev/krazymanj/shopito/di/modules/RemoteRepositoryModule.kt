package dev.krazymanj.shopito.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.communication.GeoReverseRepositoryImpl
import dev.krazymanj.shopito.communication.IGeoReverseRepository
import dev.krazymanj.shopito.communication.IShopitoRemoteRepository
import dev.krazymanj.shopito.communication.NominatimApi
import dev.krazymanj.shopito.communication.ShopitoApi
import dev.krazymanj.shopito.communication.ShopitoRemoteRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun provideGeoReverseRepository(nominatimApi: NominatimApi): IGeoReverseRepository =
        GeoReverseRepositoryImpl(nominatimApi)

    @Provides
    @Singleton
    fun provideShopitoRemoteRepository(shopitoApi: ShopitoApi): IShopitoRemoteRepository =
        ShopitoRemoteRepositoryImpl(shopitoApi)

}