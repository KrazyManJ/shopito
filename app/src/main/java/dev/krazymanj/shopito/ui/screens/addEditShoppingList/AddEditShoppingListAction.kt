package dev.krazymanj.shopito.ui.screens.addEditShoppingList

interface AddEditShoppingListAction {
    fun onNameInput(name: String)
    fun onDescriptionInput(desc: String)
    fun submit()
    fun loadShoppingListData(id: String?)
    fun deleteShoppingList()
}