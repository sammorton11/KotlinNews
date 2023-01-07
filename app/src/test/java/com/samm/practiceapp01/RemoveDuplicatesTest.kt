package com.samm.practiceapp01

import com.samm.practiceapp01.util.removeDuplicateArticles
import org.junit.Assert
import org.junit.Test

class RemoveDuplicatesTest {

    private val integerList = listOf(1, 2, 3, 3, 4, 4, 5, 5)
    private val expectedIntegerList = listOf(1, 2, 3, 4, 5)

    private val stringList = listOf("A", "B", "C", "D", "E", "F", "F")
    private val expectedStringList = listOf("A", "B", "C", "D", "E", "F")

    private val doublesList = listOf(1.0, 2.0, 3.0, 4.0, 5.0, 5.0)
    private val expectedDoublesList = listOf(1.0, 2.0, 3.0, 4.0, 5.0)

    @Test
    fun `remove duplicate integers`() {
        val result = removeDuplicateArticles(integerList)
        Assert.assertEquals(expectedIntegerList, result)
    }

    @Test
    fun `remove duplicate strings`() {
        val result = removeDuplicateArticles(stringList)
        Assert.assertEquals(expectedStringList, result)
    }

    @Test
    fun `remove duplicate doubles`() {
        val result = removeDuplicateArticles(doublesList)
        Assert.assertEquals(expectedDoublesList, result)
    }
}