package dev.krazymanj.shopito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList

@Database(
    entities = [
        ShoppingItem::class,
        ShoppingList::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class ShopitoDatabase : RoomDatabase() {

    abstract fun templateDao(): ShopitoDao

    companion object {
        private var INSTANCE: ShopitoDatabase? = null
        fun getDatabase(context: Context): ShopitoDatabase {
            if (INSTANCE == null) {
                synchronized(ShopitoDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ShopitoDatabase::class.java,
                            "shopito_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}