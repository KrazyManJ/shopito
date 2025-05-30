package dev.krazymanj.shopito.navigation

sealed class Destination(val route: String) {
    object TemplateScreen : Destination(route = "template_screen")
}