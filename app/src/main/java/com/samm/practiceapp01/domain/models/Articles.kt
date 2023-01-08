package com.samm.practiceapp01.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_table")
data class Articles(
    val author: String?,
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
