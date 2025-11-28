package dev.krazymanj.shopito.communication

data class CommunicationError(
    val code: Int,
    val message: String? = null
)