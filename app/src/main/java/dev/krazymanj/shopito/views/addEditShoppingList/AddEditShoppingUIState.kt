package dev.krazymanj.shopito.views.addEditShoppingList


data class AddEditShoppingUIState(
    val loading: Boolean = true,

    val nameInput: String = "",
    val descriptionInput: String = "",

    val isDone: Boolean = false,
)
