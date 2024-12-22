package spr3nk3ls.day22

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContains

class Day22Test {
    @Test
    fun testNext() {
        assertEquals(15887950L, Day22().next(123L))
    }

    @Test
    fun testNextTenTimes() {
        assertEquals(5908254L, Day22().next(123L, 10L))
    }

    @Test
    fun testNextDiff(){
        assertEquals(
         listOf(0L to -3L, 6L to 6L, 5L to -1L, 4L to -1L, 4L to 0L, 6L to 2L, 4L to -2L, 4L to 0L, 2L to -2L),
         Day22().nextDiff(123L, 10))
    }

    @Test
    fun testGetWindowSet(){
        val diffs = listOf(1L, 2L, 3L, 2024L).map { Day22().nextDiff(it) }
        val windowSet = Day22().getWindowSet(diffs.map { it.map { it.second } })
        assertContains(windowSet, listOf(-2L, 1L, -1L, 3L))
    }

    @Test
    fun testGetBananaSum(){
        val diffs = listOf(1L, 2L, 3L, 2024L).map { Day22().nextDiff(it) }
        assertEquals(23L, Day22().getBananaSum(diffs, listOf(-2L, 1L, -1L, 3L)))
    }
}