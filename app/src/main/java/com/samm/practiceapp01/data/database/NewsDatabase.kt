package com.samm.practiceapp01.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.samm.practiceapp01.domain.models.Articles

@Database(entities = [Articles::class], version = 2, exportSchema = false)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun myDao(): NewsDao

    companion object {
        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getDatabase(context: Context): NewsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NewsDatabase::class.java, "my_database"
            )
                .fallbackToDestructiveMigration()
                .build()
    }

}