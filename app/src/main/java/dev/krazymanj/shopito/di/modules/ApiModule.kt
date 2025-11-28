package dev.krazymanj.shopito.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.krazymanj.shopito.communication.NominatimApi
import dev.krazymanj.shopito.di.Nominatim
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideNominatimAPI(@Nominatim retrofit: Retrofit): NominatimApi =
        retrofit.create(NominatimApi::class.java)
}