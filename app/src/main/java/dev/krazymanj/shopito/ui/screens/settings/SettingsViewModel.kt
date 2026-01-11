package dev.krazymanj.shopito.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.core.UserManager
import dev.krazymanj.shopito.database.IShopitoLocalRepository
import dev.krazymanj.shopito.database.entities.ShoppingList
import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.navigation.StartDestinationSetting
import dev.krazymanj.shopito.ui.UiText
import dev.krazymanj.shopito.worker.SyncWorker
import dev.krazymanj.shopito.worker.WorkScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class UserSettings(
    val googleMapStartNavigation: Boolean,
    val startScreenSetting: StartDestinationSetting,
    val startShoppingListId: String?
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val datastore: IDataStoreRepository,
    private val repository: IShopitoLocalRepository,
    private val userManager: UserManager,
    private val workScheduler: WorkScheduler
) : ViewModel(),
    SettingsActions {

    private val _state : MutableStateFlow<SettingsUIState> = MutableStateFlow(value = SettingsUIState())

    val state = _state.asStateFlow()

    init {
        observeSyncStatus()
    }

    override fun loadSettings() {
        viewModelScope.launch {
            val settingsFlow = combine(
                datastore.getFlow(DataStoreKey.GoogleMapsStartNavigationKey),
                datastore.getFlow(DataStoreKey.StartScreenSetting),
                datastore.getFlow(DataStoreKey.StartShoppingListId),
            ) { googleMapStartNavigation, startScreenSetting, startShoppingListId ->
                UserSettings(
                    googleMapStartNavigation = googleMapStartNavigation,
                    startScreenSetting = startScreenSetting,
                    startShoppingListId = startShoppingListId
                )
            }

            combine(
                settingsFlow,
                repository.getShoppingLists(),
                datastore.getFlow(DataStoreKey.LastSyncTime),
            ) { settings, shoppingLists, lastSyncTime ->
                SettingsUIState(
                    isLoading = false,
                    startNavigationSetting = settings.googleMapStartNavigation,
                    startScreenSetting = settings.startScreenSetting,
                    startShoppingListId = settings.startShoppingListId,
                    shoppingLists = shoppingLists,
                    loggedData = userManager.getUserInfo(),
                    lastTimeSynced = lastSyncTime
                )
            }.collect { state ->
                _state.update { state }
            }
        }
    }

    override fun onStartNavigationSettingChange(value: Boolean) {
        viewModelScope.launch {
            datastore.set(DataStoreKey.GoogleMapsStartNavigationKey, value)
        }
    }

    override fun onStartScreenSettingChange(value: StartDestinationSetting) {
        viewModelScope.launch {
            datastore.set(DataStoreKey.StartScreenSetting, value)
        }
    }

    override fun onStartShoppingListChange(value: ShoppingList) {
        viewModelScope.launch {
            datastore.set(DataStoreKey.StartShoppingListId, value.id)
        }
    }

    override fun logout(wipeData: Boolean) {
        viewModelScope.launch {
            userManager.logout()
            workScheduler.cancelAllWork()
            if (wipeData) {
                repository.wipeAllData()
            }
        }
    }

    private fun observeSyncStatus() {
        viewModelScope.launch {
            workScheduler.getSyncWorkInfoFlow().collect { workInfo ->

                if (workInfo == null) return@collect

                val state = workInfo.state

                val isSyncing = state == WorkInfo.State.RUNNING ||
                        state == WorkInfo.State.ENQUEUED

                val resultMessage = when (state) {
                    WorkInfo.State.FAILED -> {
                        val error = workInfo.outputData.getInt(SyncWorker.ERROR_KEY, defaultValue = -1)
                        UiText.StringResource(when (error) {
                            -1 -> R.string.error_unknown
                            else -> error
                        })
                    }
                    else -> null
                }

                _state.update { it.copy(
                    isSyncing = isSyncing,
                    syncError = resultMessage
                ) }
            }
        }
    }

    override fun attemptSync() {
        workScheduler.scheduleOneTimeSync()
    }
}