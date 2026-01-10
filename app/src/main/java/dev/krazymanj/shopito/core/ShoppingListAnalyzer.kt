package dev.krazymanj.shopito.core

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import dev.krazymanj.shopito.BuildConfig
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.extension.extractLastAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShoppingListAnalyzer @Inject constructor() {

    private val generativeModel = GenerativeModel(

        modelName = "gemini-2.5-flash",
//        modelName = "gemini-2.5-flash-lite", // USE THIS IN CASE IF FIRST ONE RUNS OUT OF CREDITS
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    sealed class Result {
        data object NotAList : Result()
        data object Empty : Result()
        data object Error : Result()
        data class Success(val items: List<ShoppingItem>) : Result()
    }

    suspend fun analyzeImage(image: Bitmap, shoppingList: ShoppingList): Result {
        return withContext(Dispatchers.IO) {
            val prompt = """
                You are a reader of items from physical or digital shopping list.
                Your task is to read and extract all items on shopping list.
                Ignore crossed-out items.
                Return ONLY the items, one per line. No other text.
                Each line should be in format "<name> <amount>x" for example "Milk 2x"
                Exclude dashes as representation of list as well.
                If you decide, that picture is does not contain a shopping list (in any form digital/paper like, not even an empty one), just return word "NotAList".
                If you decide, that picture is shopping list, but is empty, just return word "Empty".
            """.trimIndent()
            try {

                val response = generativeModel.generateContent(content {
                    image(image)
                    text(prompt)
                })

                val responseText = response.text

                when (responseText) {
                    "NotAList" -> Result.NotAList
                    "Empty" -> Result.Empty
                    else -> Result.Success(items = responseText?.lines()?.map {
                        val (name, amount) = it.extractLastAmount()
                        ShoppingItem(
                            itemName = name,
                            amount = amount,
                            listId = shoppingList.id
                        )
                    } ?: emptyList())
                }

            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error
            }
        }
    }
}