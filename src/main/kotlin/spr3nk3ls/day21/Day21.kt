package spr3nk3ls.day21

import spr3nk3ls.util.Utils
import kotlin.math.abs

val numberGrid = mapOf(
    "7" to (0 to 0),
    "8" to (1 to 0),
    "9" to (2 to 0),
    "4" to (0 to 1),
    "5" to (1 to 1),
    "6" to (2 to 1),
    "1" to (0 to 2),
    "2" to (1 to 2),
    "3" to (2 to 2),
    "0" to (1 to 3),
    "A" to (2 to 3),
)

val digitGrid = mapOf(
    "^" to (1 to 0),
    "A" to (2 to 0),
    "<" to (0 to 1),
    "v" to (1 to 1),
    ">" to (2 to 1),
)

fun main() {
    getSolution("day21/example.txt")
    getSolution("day21/input.txt")
}

fun getSolution(filename: String) {
    val numberMap = getNumberMap(numberGrid, 0 to 3)
    val digitMap = getNumberMap(digitGrid, 0 to 0)
    val result = Utils.readLines(filename).map {
        val levelOne = getLevel("A" + it, numberMap)
        val levelTwo = levelOne.flatMap{getLevel("A" + it, digitMap)}
        val levelThree = levelTwo.flatMap{getLevel("A" + it, digitMap)}
        it to levelThree.map { it.length }.min()
    }.map { it.second to it.first.substring(0, it.first.length - 1).toLong()
    }.onEach { println(it)
    }.sumOf { it.first * it.second }
    println(result)
}

private fun getLevel(line: String, numberMap: Map<String, Set<String>>): Set<String> {
    val steps = line.windowed(2).map { numberMap[it]!! }
    var routes = steps.first().toList()
    steps.drop(1).forEach { step ->
        routes = routes.flatMap { current -> step.map { current + it } }
    }
    return routes.toSet()
}

private fun getNumberMap(grid: Map<String, Pair<Int, Int>>, forbidden: Pair<Int, Int>): Map<String, Set<String>> {
    return grid.keys.flatMap { key -> grid.keys.map { it + key } }.map {
        it to (grid[it.first().toString()]!! to grid[it.last().toString()]!!)
    }.associate { it.first to pathsOf(it.second, forbidden) }
}

private fun pathsOf(termini: Pair<Pair<Int, Int>, Pair<Int, Int>>, forbidden: Pair<Int, Int>): Set<String> {
    val (begin, end) = termini
    if(begin == end) return setOf("A")
    val x = end.first - begin.first
    val y = end.second - begin.second
    val xSteps = generateSequence { if(x > 0) 1 else -1 }.map { it to 0 }.take(abs(x)).toList()
    val ySteps = generateSequence { if(y > 0) 1 else -1 }.map { 0 to it }.take(abs(y)).toList()
    return (xSteps + ySteps).permutations().toSet().filter { route ->
        val places = (1..route.size)
            .map { route.subList(0, it).reduce{a, b -> a.first + b.first to a.second + b.second} }
            .map { it.first + begin.first to it.second + begin.second}
        forbidden !in places
    }.map { route ->
        route.map {
            when (it) {
                0 to 1 -> "v"
                1 to 0 -> ">"
                0 to -1 -> "^"
                -1 to 0 -> "<"
                else -> {
                    println("error")
                    "0"
                }
            }
        }.joinToString("") + "A"
    }.toSet()
}

private fun List<Pair<Int, Int>>.permutations(builtSequence: List<Pair<Int, Int>> = listOf()): List<List<Pair<Int, Int>>> =
    if (isEmpty()) listOf(builtSequence)
    else flatMap { (this - it).permutations(builtSequence + it) }