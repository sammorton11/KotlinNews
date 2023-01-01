package com.samm.practiceapp01.domain.models

data class NewsItem(
    val status: String,
    val totalResults: Int,
    val articles: List<Articles>
)
