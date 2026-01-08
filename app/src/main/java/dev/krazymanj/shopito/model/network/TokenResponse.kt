package dev.krazymanj.shopito.model.network

import com.squareup.moshi.Json

data class TokenResponse(
    @param:Json(name = "access_token")
    val accessToken: String? = null,
    @param:Json(name = "token_type")
    val tokenType: String? = null,
)