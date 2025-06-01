package dev.krazymanj.shopito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dev.krazymanj.shopito.R
import dev.krazymanj.shopito.database.entities.ShoppingItem
import dev.krazymanj.shopito.database.entities.ShoppingList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ShoppingItem::class,
        ShoppingList::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class ShopitoDatabase : RoomDatabase() {

    abstract fun shopitoDao(): ShopitoDao

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
                        )
                            .addCallback(object: Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val instance = getDatabase(context)
                                        val dao = instance.shopitoDao()
                                        dao.insert(ShoppingList(
                                            name = context.getString(R.string.general_shopping_list_name),
                                            description = context.getString(R.string.general_shopping_list_description)
                                        ))
                                    }
                                }
                            })
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}