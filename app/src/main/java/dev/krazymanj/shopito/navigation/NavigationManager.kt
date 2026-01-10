package dev.krazymanj.shopito.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationManager @Inject constructor() {
    private val _navigationEvent = MutableSharedFlow<Destination>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val navigationEvent = _navigationEvent.asSharedFlow()

    suspend fun navigate(destination: Destination) {
        _navigationEvent.emit(destination)
    }
}