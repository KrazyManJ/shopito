package dev.krazymanj.shopito.core

import dev.krazymanj.shopito.datastore.DataStoreKey
import dev.krazymanj.shopito.datastore.IDataStoreRepository
import dev.krazymanj.shopito.extension.removeFirstItem
import dev.krazymanj.shopito.model.SavedLocation
import dev.krazymanj.shopito.ui.elements.modal.MAX_OPTIONS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecentLocationsManagerImpl @Inject constructor(
    private val dataStore: IDataStoreRepository,
    private val coroutineScope: CoroutineScope
): IRecentLocationsManager {

    override fun addToRecentLocations(savedLocation: SavedLocation) {
        coroutineScope.launch {
            val set = dataStore.get(DataStoreKey.LastFiveLocations) as LinkedHashSet<SavedLocation>
            set.add(savedLocation)
            if (set.size > MAX_OPTIONS) {
                set.removeFirstItem()
            }
            dataStore.set(DataStoreKey.LastFiveLocations, set)
        }
    }

    override fun removeFromRecentLocations(savedLocation: SavedLocation) {
        coroutineScope.launch {
            val set = dataStore.get(DataStoreKey.LastFiveLocations) as LinkedHashSet<SavedLocation>
            set.remove(savedLocation)
            dataStore.set(DataStoreKey.LastFiveLocations, set)
        }
    }
}