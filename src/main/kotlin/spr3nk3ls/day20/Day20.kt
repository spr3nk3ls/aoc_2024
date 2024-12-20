package spr3nk3ls.day20

import spr3nk3ls.util.Utils
import kotlin.math.abs

fun main() {
    getSolution("day20/example.txt")
    getSolution("day20/input.txt")
}

fun getSolution(filename: String) {
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

    val range = grid.first().length to grid.size

    val routeWithDistance = route.zip((0..<route.size)).toMap()

    val horizontalBridges = (1..<range.second - 1).flatMap { y ->
        (1..<range.first - 1).filter {
            it to y !in route
        }.filter {
            it - 1 to y in route && it + 1 to y in route
        }.map { (it to y) to abs(routeWithDistance[it - 1 to y]!! - routeWithDistance[it + 1 to y]!!) }
    }
    val verticalBridges = (1..<range.second - 1).flatMap { y ->
        (1..<range.first - 1).filter {
            it to y !in route
        }.filter {
            it to y - 1 in route && it to y + 1 in route
        }.map { (it to y) to abs(routeWithDistance[it to y - 1]!! - routeWithDistance[it to y + 1]!!) }
    }

    val saves = (horizontalBridges + verticalBridges).map { it.second }.map { it - 2 }.filter { it > 0 }.sorted()
    println(saves.distinct().map { save ->
        save to saves.count { it == save }
    })

    println(saves.count { it >= 100 })
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

fun printgrid(
    range: Pair<Int, Int>,
    start: Pair<Int, Int>,
    end: Pair<Int, Int>,
    hallway: Set<Pair<Int, Int>>,
    bridges: List<Pair<Int, Int>>
) {
    (0..<range.second).forEach { y ->
        (0..<range.first).forEach {
            val gridpoint = it to y
            when (gridpoint) {
                start -> {
                    print("S")
                }

                end -> {
                    print("E")
                }

                in hallway -> {
                    print(".")
                }

                in bridges -> {
                    print("O")
                }

                else -> {
                    print("#")
                }
            }
        }
        print("\n")
    }
}
