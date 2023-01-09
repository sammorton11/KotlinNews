package com.samm.practiceapp01.domain.models

import java.io.Serializable

data class NewsItem(
    val status: String,
    val totalResults: Int,
    val articles: List<Articles>
): Serializable
