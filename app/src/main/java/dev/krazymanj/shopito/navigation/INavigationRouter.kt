package dev.krazymanj.shopito.navigation

interface INavigationRouter {
    fun navigateTo(destination: Destination)
    fun returnBack()
}