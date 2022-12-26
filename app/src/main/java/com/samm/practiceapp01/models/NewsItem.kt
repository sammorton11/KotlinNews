package com.samm.practiceapp01.models

import com.samm.practiceapp01.models.Articles

data class NewsItem(
    val status: String,
    val totalResults: Int,
    val articles: List<Articles>
)
