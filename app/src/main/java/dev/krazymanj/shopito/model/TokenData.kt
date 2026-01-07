package dev.krazymanj.shopito.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenData(
    @SerialName("sub")
    val username: String
)
