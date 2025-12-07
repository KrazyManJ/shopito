package dev.krazymanj.shopito

import dev.krazymanj.shopito.extension.ellipsis
import org.junit.Assert.assertEquals
import org.junit.Test

class StringEllipsisUnitTest {
    @Test
    fun `ellipsis returns original text when length is below limit`() {
        val input = "Short text"
        val result = input.ellipsis(limit = 20)
        assertEquals("Short text", result)
    }

    @Test
    fun `ellipsis returns original text when length equals limit`() {
        val input = "Exact"
        val result = input.ellipsis(limit = 5)
        assertEquals("Exact", result)
    }

    @Test
    fun `ellipsis truncates text and adds default suffix when limit exceeded`() {
        val input = "This is a very long text"
        val limit = 10

        val result = input.ellipsis(limit = limit)

        // "This is a " (10 chars) + "..."
        assertEquals("This is a ...", result)
    }

    @Test
    fun `ellipsis uses custom suffix`() {
        val input = "Hello World"
        val limit = 5
        val suffix = "!"

        val result = input.ellipsis(limit = limit, suffix = suffix)

        assertEquals("Hello!", result)
    }

    @Test
    fun `ellipsis works with empty string`() {
        val input = ""
        val result = input.ellipsis(limit = 5)
        assertEquals("", result)
    }
}