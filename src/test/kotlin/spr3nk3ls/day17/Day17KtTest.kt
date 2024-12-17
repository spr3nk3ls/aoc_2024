package spr3nk3ls.day17

import kotlin.test.Test

class Day17KtTest {

    @Test
    fun getSolutionTest1() {
        val day17 = Day17
        val register = mutableMapOf('A' to 0, 'B' to 0, 'C' to 9)
        day17.execute(listOf(2, 6), register)
        kotlin.test.assertEquals(1, register['B'])
    }

    @Test
    fun getSolutionTest2() {
        val day17 = Day17
        val register = mutableMapOf('A' to 10, 'B' to 0, 'C' to 0)
        val output = day17.execute(listOf(5, 0, 5, 1, 5, 4), register)
        kotlin.test.assertEquals("0,1,2", output)
    }

    @Test
    fun getSolutionTest3() {
        val day17 = Day17
        val register = mutableMapOf('A' to 2024, 'B' to 0, 'C' to 0)
        val output = day17.execute(listOf(0, 1, 5, 4, 3, 0), register)
        kotlin.test.assertEquals("4,2,5,6,7,7,7,7,3,1,0", output)
        kotlin.test.assertEquals(0, register['A'])
    }
}

