package dev.krazymanj.shopito.model.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterForm(
    val username: String,
    val password: String
)