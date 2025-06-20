package dev.krazymanj.shopito.views.addEditShoppingList

interface AddEditShoppingListAction {
    fun onNameInput(name: String)
    fun onDescriptionInput(desc: String)
    fun submit()
    fun loadShoppingListData(id: Long?)
    fun deleteShoppingList()
}