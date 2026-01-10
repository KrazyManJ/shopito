package dev.krazymanj.shopito.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavGraphViewModel @Inject constructor(
    private val navigationManager: NavigationManager
) : ViewModel() {
    val navigationEvent = navigationManager.navigationEvent
}