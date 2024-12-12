package spr3nk3ls.day12

import spr3nk3ls.util.Utils

fun main() {
    getSolution("day12/example1.txt")
    getSolution("day12/example2.txt")
    getSolution("day12/input.txt")
}

private fun getSolution(filename: String) {
    val lines = Utils.readLines(filename)
    val range = lines.first().length to lines.size
    val horizontalFences = (0..<range.second).map { y ->
        (0..<range.first - 1).map { x ->
            lines[y][x] != lines[y][x + 1]
        }
    }
    val verticalFences = (0..<range.second - 1).map { y ->
        (0..<range.first).map { x ->
            lines[y][x] != lines[y + 1][x]
        }
    }
    val grid = (0..<range.second).flatMap { y ->
        (0..<range.first).map { x ->
            var fences = 0
            val connections = mutableListOf<Pair<Int, Int>>()
            if (x == 0 || horizontalFences[y][x - 1]) {
                fences++
            } else {
                connections.add(Pair(x - 1, y))
            }
            if (x == range.first - 1 || horizontalFences[y][x]) {
                fences++
            } else {
                connections.add(Pair(x + 1, y))
            }
            if (y == 0 || verticalFences[y - 1][x]) {
                fences++
            } else {
                connections.add(Pair(x, y - 1))
            }
            if (y == range.second - 1 || verticalFences[y][x]) {
                fences++
            } else {
                connections.add(Pair(x, y + 1))
            }
            (x to y) to (fences to connections)
        }
    }.toMap().toMutableMap()

    println(grid)
    var regions = 0L
    while (grid.isNotEmpty()) {
        val regionSeed = grid.keys.first()
        val region = mutableListOf<Int>()
        addToRegion(region, regionSeed, grid, mutableSetOf(regionSeed))
        regions += region.sum() * region.size
    }
    println(regions)
}

fun addToRegion(
    region: MutableList<Int>,
    seed: Pair<Int, Int>,
    grid: MutableMap<Pair<Int, Int>, Pair<Int, MutableList<Pair<Int, Int>>>>,
    visited: MutableSet<Pair<Int, Int>>
) {
    val plot = grid.remove(seed)!!
    region.add(plot.first)
    val toVisit = plot.second.filter {
        it !in visited && it in grid.keys
    }
    visited.addAll(toVisit)
    toVisit.map {
        addToRegion(region, it, grid, visited)
    }
}