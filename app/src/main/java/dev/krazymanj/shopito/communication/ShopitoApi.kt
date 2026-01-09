package dev.krazymanj.shopito.communication

import dev.krazymanj.shopito.model.network.RegisterForm
import dev.krazymanj.shopito.model.network.SyncRequest
import dev.krazymanj.shopito.model.network.SyncResponse
import dev.krazymanj.shopito.model.network.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ShopitoApi {
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<TokenResponse>

    @POST("register")
    suspend fun register(
        @Body registerForm: RegisterForm
    ): Response<TokenResponse>

    @POST("sync")
    suspend fun sync(
        @Header("Authorization") token: String,
        @Body syncRequest: SyncRequest
    ): Response<SyncResponse>
}