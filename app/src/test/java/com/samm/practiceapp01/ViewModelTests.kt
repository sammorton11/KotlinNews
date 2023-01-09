package com.samm.practiceapp01

import com.samm.practiceapp01.data.RepositoryImpl
import com.samm.practiceapp01.presentation.NewsViewModel
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

class ViewModelTests {

    private val viewModel = mockk<NewsViewModel>()
    private val repo = mockk<RepositoryImpl>()

    @Test
    fun `call repository method`() {

        // Verify that the getArticles function was called with the correct arguments
        coVerify { viewModel.getArticles(1, "search") }

        // Verify that the fetchArticles function in the Repo class was called with the correct arguments
        coVerify { repo.fetchArticles("search", 1) }
    }
}
