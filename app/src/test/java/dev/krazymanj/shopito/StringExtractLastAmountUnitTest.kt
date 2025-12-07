package dev.krazymanj.shopito

import dev.krazymanj.shopito.extension.extractLastAmount
import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtractLastAmountUnitTest {

    @Test
    fun `extractLastAmount returns default 1 when no amount pattern found`() {
        val input = "Milk semi-skimmed"
        val result = input.extractLastAmount()

        assertEquals("Milk semi-skimmed", result.first)
        assertEquals(1, result.second)
    }

    @Test
    fun `extractLastAmount parses suffix format (xNumber)`() {
        val input = "Bread x2"
        val result = input.extractLastAmount()

        assertEquals("Bread", result.first)
        assertEquals(2, result.second)
    }

    @Test
    fun `extractLastAmount parses prefix format (Numberx)`() {
        val input = "5x Yogurt"
        val result = input.extractLastAmount()

        assertEquals("Yogurt", result.first)
        assertEquals(5, result.second)
    }

    @Test
    fun `extractLastAmount is case insensitive`() {
        val input = "Beer X6"
        val result = input.extractLastAmount()

        assertEquals("Beer", result.first)
        assertEquals(6, result.second)
    }

    @Test
    fun `extractLastAmount takes the last match only`() {
        val input = "Water x6 pack x2"
        val result = input.extractLastAmount()

        assertEquals("Water x6 pack", result.first)
        assertEquals(2, result.second)
    }

    @Test
    fun `extractLastAmount handles extraction from middle of string correctly`() {
        val input = "Apples 1x red delicious"
        val result = input.extractLastAmount()

        assertEquals("Apples red delicious", result.first)
        assertEquals(1, result.second)
    }

    @Test
    fun `extractLastAmount handles large numbers`() {
        val input = "  Screws  x1000 "
        val result = input.extractLastAmount()

        assertEquals("Screws", result.first)
        assertEquals(1000, result.second)
    }

}