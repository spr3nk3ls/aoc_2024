package spr3nk3ls.day17

import java.math.BigInteger.valueOf
import kotlin.test.Test
import kotlin.test.assertEquals

class Day17KtTest {

    @Test
    fun testExecute1() {
        val day17 = Day17
        val register = mutableMapOf('A' to 0L, 'B' to 0L, 'C' to 9L)
        day17.execute(listOf(2L, 6L), register)
        assertEquals(1L, register['B'])
    }

    @Test
    fun testExecute2() {
        val day17 = Day17
        val register = mutableMapOf('A' to 10L, 'B' to 0L, 'C' to 0L)
        val output = day17.execute(listOf(5L, 0L, 5L, 1L, 5L, 4L), register)
        assertEquals(listOf(0L, 1L, 2L), output)
    }

    @Test
    fun testExecute3() {
        val day17 = Day17
        val register = mutableMapOf('A' to 2024L, 'B' to 0L, 'C' to 0L)
        val output = day17.execute(listOf(0L, 1L, 5L, 4L, 3L, 0L), register)
        assertEquals(listOf(4L, 2L, 5L, 6L, 7L, 7L, 7L, 7L, 3L, 1L, 0L), output)
        assertEquals(0L, register['A'])
    }

    @Test
    fun testExecuteQuine1() {
        val day17 = Day17
        val register = mutableMapOf('A' to 117440L, 'B' to 0L, 'C' to 0L)
        val instructions = listOf(0L, 3L, 5L, 4L, 3L, 0L)
        val output = day17.execute(instructions, register)
        assertEquals(instructions, output)
    }

    @Test
    fun testExecuteQuine2() {
        val day17 = Day17
        val sixteen = (valueOf(8L).pow(15)).toLong()
        val register = mutableMapOf('A' to sixteen, 'B' to 0L, 'C' to 0L)
        val instructions = listOf(0L, 3L, 5L, 4L, 3L, 0L)
        val output = day17.execute(instructions, register)
        assertEquals(16, output.size)
    }

    @Test
    fun testExecuteQuine3() {
        val day17 = Day17
        val sixteen = (valueOf(8L).pow(15)).toLong() - 1
        val register = mutableMapOf('A' to sixteen, 'B' to 0L, 'C' to 0L)
        val instructions = listOf(0L, 3L, 5L, 4L, 3L, 0L)
        val output = day17.execute(instructions, register)
        assertEquals(15, output.size)
    }

    @Test
    fun testExecuteQuine4() {
        val day17 = Day17
        val ansatz =
            (valueOf(8L).pow(15).times(valueOf(3)))
                .add((valueOf(8L).pow(14)))
                .add((valueOf(8L).pow(12)).times(valueOf(4)))
                .add((valueOf(8L).pow(11)).times(valueOf(5)))
                .add((valueOf(8L).pow(10)).times(valueOf(1)))
                .add((valueOf(8L).pow(9)).times(valueOf(1)))
                .add((valueOf(8L).pow(8)).times(valueOf(2)))
                .add((valueOf(8L).pow(7)).times(valueOf(7)))
                .add((valueOf(8L).pow(6)).times(valueOf(7)))
        val register = mutableMapOf('A' to ansatz.toLong(), 'B' to 0L, 'C' to 0L)
        val instructions = listOf(2,4,1,5,7,5,4,3,1,6,0,3,5,5,3,0).map { it.toLong() }
        val output = day17.execute(instructions, register)
        assertEquals(16, output.size)
        println(output)
    }

    @Test
    fun testExecuteQuine5() {
        val day17 = Day17
        val ansatz =
            (valueOf(8L).pow(15).times(valueOf(2)))
                .add(valueOf(8L).pow(14).times(valueOf(7)))
                .add(valueOf(8L).pow(13).times(valueOf(7)))
                .add(valueOf(8L).pow(12).times(valueOf(7)))
                .add(valueOf(8L).pow(11).times(valueOf(7)))
                .add(valueOf(8L).pow(10).times(valueOf(7)))
                .add(valueOf(8L).pow(9).times(valueOf(7)))
                .add(valueOf(8L).pow(8).times(valueOf(7)))
                .add(valueOf(8L).pow(7).times(valueOf(7)))
                .add(valueOf(8L).pow(6).times(valueOf(8)))
//                .add((valueOf(8L).pow(12)))
//                .add((valueOf(8L).pow(11)).times(valueOf(8)))
//                .add((valueOf(8L).pow(10)).times(valueOf(2)))
//                .add((valueOf(8L).pow(9)).times(valueOf(2)))
//                .add((valueOf(8L).pow(9)).times(valueOf(9)))
        val register = mutableMapOf('A' to ansatz.toLong(), 'B' to 0L, 'C' to 0L)
        val instructions = listOf(2,4,1,5,7,5,4,3,1,6,0,3,5,5,3,0).map { it.toLong() }
        val output = day17.execute(instructions, register)
        assertEquals(16, output.size)
        println(output)
    }
}

