package spr3nk3ls.day20

import spr3nk3ls.util.Utils
import kotlin.math.abs

fun main() {
    getSolution("day20/input.txt", 2)
    getSolution("day20/input.txt", 20)
}

fun getSolution(filename: String, distance: Int) {
    val grid = Utils.readLines(filename)
    val hallway = grid.indices.flatMap { y ->
        grid[y].indices.filter { x ->
            grid[y][x] == '.' || grid[y][x] == 'S' || grid[y][x] == 'E'
        }.map { it to y }
    }.toSet()
    val start = grid.indices.flatMap { y ->
        grid[y].indices.filter { x ->
            grid[y][x] == 'S'
        }.map { it to y }
    }.first()
    val end = grid.indices.flatMap { y ->
        grid[y].indices.filter { x ->
            grid[y][x] == 'E'
        }.map { it to y }
    }.first()
    val route = calculateRoute(hallway, start, end)

    val routeWithDistance = route.zip((0..<route.size)).toMap()

    val manhattans = route.map { mh ->
        manhattansOf(mh, distance)
            .asSequence()
            .filter { it.first in route }
            .filter { distance != 2 || it.second == 2 }
            .map { routeWithDistance[it.first]!! - routeWithDistance[mh]!! - it.second }
            .filter { it >= 100 }
            .toList()
    }.filter { it.isNotEmpty() }.flatten()

    println(manhattans.count())
}

private fun calculateRoute(
    hallway: Set<Pair<Int, Int>>,
    start: Pair<Int, Int>,
    end: Pair<Int, Int>
): List<Pair<Int, Int>> {
    return generateSequence(start to adjacent(start, hallway).first()) { (one, two) ->
        two to adjacent(two, hallway).first { it != one }
    }.takeWhile { it.second != end }.flatMap { listOf(it.first, it.second) }.distinct().toList() + end
}

fun adjacent(point: Pair<Int, Int>, hallway: Set<Pair<Int, Int>>): Set<Pair<Int, Int>> {
    return setOf(
        point.first - 1 to point.second,
        point.first + 1 to point.second,
        point.first to point.second - 1,
        point.first to point.second + 1
    ).filter { it in hallway }.toSet()
}

fun manhattansOf(point: Pair<Int, Int>, distance: Int): Set<Pair<Pair<Int, Int>, Int>> {
    return (-distance..distance).flatMap { x ->
        (-distance..distance).filter { abs(x) + abs(it) <= distance }.map {
            (point.first + x to point.second + it) to (abs(x) + abs(it))
        }
    }.filter { it.first != point }.toSet()
}
