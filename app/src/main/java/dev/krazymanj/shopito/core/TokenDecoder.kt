package dev.krazymanj.shopito.core

import android.util.Base64
import dev.krazymanj.shopito.model.TokenData
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets

object TokenDecoder {
    fun decodeToken(token: String): TokenData? {
        try {
            val split = token.split(".")
            if (split.size < 2) return null

            val payloadBase64 = split[1]
            val decodedBytes = Base64.decode(payloadBase64, Base64.URL_SAFE)
            val decodedString = String(decodedBytes, StandardCharsets.UTF_8)

            val jsonParser = Json { ignoreUnknownKeys = true }
            return jsonParser.decodeFromString<TokenData>(decodedString)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}