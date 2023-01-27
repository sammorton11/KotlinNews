package com.samm.practiceapp01

import com.samm.practiceapp01.domain.models.Articles

data class NewsState(
    val isLoading: Boolean = false,
    val articles: List<Articles> = emptyList(),
    val error: String = ""
)
