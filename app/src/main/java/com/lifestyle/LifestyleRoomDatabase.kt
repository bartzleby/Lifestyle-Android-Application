package com.lifestyle;

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.jvm.Volatile
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UserData::class], version = 1, exportSchema = false)
abstract class LifestyleRoomDatabase : RoomDatabase() {
    abstract fun lifestyleDao(): LifestyleDao

    // Make the db singleton.
    companion object {
        @Volatile
        private var mInstance: LifestyleRoomDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): LifestyleRoomDatabase {
            return mInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LifestyleRoomDatabase::class.java, "lifestyle.db"
                )
                    .addCallback(RoomDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                mInstance = instance
                instance
            }
        }

        private class RoomDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                mInstance?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDbTask(database.lifestyleDao())
                    }
                }
            }
        }

        suspend fun populateDbTask(lifestyleDao: LifestyleDao) {
            lifestyleDao.insert(UserData("Damien"))
        }
    }
}
