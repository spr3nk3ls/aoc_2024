package spr3nk3ls.day10

import spr3nk3ls.util.Utils

val directions = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

fun main() {
    getSolution("day10/example.txt")
    getSolution("day10/input.txt")
//    getSolution("day8/example.txt", Node::projectAll)
//    getSolution("day8/input.txt", Node::projectAll)
}

private fun getSolution(filename: String) {
    val lines = Utils.readLines(filename)
    val range = lines.first().length to lines.size
    val grid = lines.map { it.toCharArray().map { it.digitToInt() } }
    val trailheads = getTrailheads(grid, range)
    val result = trailheads.sumOf { numberOfTrails(it, grid, range) }
    println(result)
}

fun getTrailheads(grid: List<List<Int>>, range: Pair<Int, Int>): List<Pair<Int, Int>> {
    return (0 until range.first).flatMap { y ->
        (0 until range.second).filter { x ->
            grid[y][x] == 0
        }.map { x -> x to y }
    }

}

private fun numberOfTrails(
    pair: Pair<Int, Int>, grid: List<List<Int>>, range: Pair<Int, Int>
): Int {
    val trailhead = Hiker(pair, grid[pair.second][pair.first])
    var hikers = listOf(trailhead)
    var nines = mutableSetOf<Hiker>()
    while (hikers.size > 0) {
        hikers = hikers.onEach { if (it.height == 9) nines.add(it) }.flatMap {
            it.hike(grid, range)
        }
    }
    return nines.size
}

private data class Hiker(val position: Pair<Int, Int>, val height: Int) {
    fun hike(grid: List<List<Int>>, range: Pair<Int, Int>): List<Hiker> {
        return directions.map {
            position.first + it.first to position.second + it.second
        }.filter { withinBounds(it, range) }.map {
            val newHeight = grid[it.second][it.first]
            if (newHeight == height + 1) Hiker(it, newHeight) else null
        }.filterNotNull()
    }
}

private fun withinBounds(index: Pair<Int, Int>, range: Pair<Int, Int>): Boolean =
    index.first >= 0 && index.first < range.first && index.second >= 0 && index.second < range.second