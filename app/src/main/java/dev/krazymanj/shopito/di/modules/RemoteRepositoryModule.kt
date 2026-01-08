package dev.krazymanj.shopito.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.communication.GeoReverseRepositoryImpl
import dev.krazymanj.shopito.communication.IGeoReverseRepository
import dev.krazymanj.shopito.communication.IShopitoRemoteRepository
import dev.krazymanj.shopito.communication.ShopitoRemoteRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RemoteRepositoryModule {

    @Binds
    @Singleton
    fun bindGeoReverseRepository(impl: GeoReverseRepositoryImpl): IGeoReverseRepository

    @Binds
    @Singleton
    fun bindShopitoRemoteRepository(impl: ShopitoRemoteRepositoryImpl): IShopitoRemoteRepository

}