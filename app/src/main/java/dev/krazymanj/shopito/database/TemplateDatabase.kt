package dev.krazymanj.shopito.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TemplateDatabaseClass::class], version = 1, exportSchema = true)
// For enum converters install like this:
// @TypeConverters(FuelTypeConverter::class)
abstract class TemplateDatabase : RoomDatabase() {

    abstract fun templateDao(): TemplateDao

    companion object {
        private var INSTANCE: TemplateDatabase? = null
        fun getDatabase(context: Context): TemplateDatabase {
            if (INSTANCE == null) {
                synchronized(TemplateDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            TemplateDatabase::class.java,
                            "template_database"
                        ).fallbackToDestructiveMigration().build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

}