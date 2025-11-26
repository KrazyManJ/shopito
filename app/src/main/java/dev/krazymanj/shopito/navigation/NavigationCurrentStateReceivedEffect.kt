package dev.krazymanj.shopito.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect


@Composable
inline fun <reified T> NavigationCurrentStateReceivedEffect(
    navRouter: INavigationRouter,
    key: NavStateKey<T>,
    crossinline block: (T) -> Unit
) {
    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        val value = navRouter.getCurrentState(key, T::class.java)
        navRouter.clearCurrentState(key)
        value?.let(block)
    }
}