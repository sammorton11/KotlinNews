package com.samm.practiceapp01.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samm.practiceapp01.domain.models.Articles

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(articles: Articles)

    @Query("SELECT * FROM my_table ORDER BY id ASC")
    fun getAllNewsItems(): LiveData<List<Articles>>

    @Query("DELETE FROM my_table")
    suspend fun clearCache()

    @Query("SELECT * FROM my_table LIMIT :limit OFFSET :offset")
    fun getPage(limit: Int, offset: Int): LiveData<List<Articles>>

}