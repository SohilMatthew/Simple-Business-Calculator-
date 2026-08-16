package com.example.businesscalculator.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.businesscalculator.data.dao.DailyRecordDao
import com.example.businesscalculator.data.dao.MenuItemDao
import com.example.businesscalculator.data.entity.DailyRecord
import com.example.businesscalculator.data.entity.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [MenuItem::class, DailyRecord::class],
    version = 1,
    exportSchema = false
)
abstract class BusinessDatabase : RoomDatabase() {

    abstract fun menuItemDao(): MenuItemDao
    abstract fun dailyRecordDao(): DailyRecordDao

    companion object {
        @Volatile
        private var INSTANCE: BusinessDatabase? = null

        fun getDatabase(context: Context): BusinessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BusinessDatabase::class.java,
                    "business_calculator_database"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.menuItemDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: MenuItemDao) {
                dao.insertMenuItem(MenuItem(name = "Item XXX", isActive = true))
                dao.insertMenuItem(MenuItem(name = "Item YYY", isActive = true))
            }
        }
    }
}
