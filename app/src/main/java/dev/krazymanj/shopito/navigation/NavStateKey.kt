package dev.krazymanj.shopito.navigation

import dev.krazymanj.shopito.model.Location
import kotlinx.serialization.Serializable


@Serializable
sealed class NavStateKey<T>(val key: String) {
    @Serializable
    data object LocationModalResult: NavStateKey<Location>(
        key = "location_modal_result"
    )
    @Serializable
    data object LocationQuickAddResult: NavStateKey<Location>(
        key = "location_quick_add_result"
    )
}