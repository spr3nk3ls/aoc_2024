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
            val plot = getPlot(x, y, horizontalFences, verticalFences, range)
            (x to y) to (plot)
        }
    }.toMap().toMutableMap()

    var regionsFences = 0L
    var regionsEdges = 0L
    while (grid.isNotEmpty()) {
        val regionSeed = grid.keys.first()
        val regionFences = mutableListOf<Int>()
        val regionEdges = mutableListOf<Int>()
        addToRegion(regionFences, regionEdges, regionSeed, grid, mutableSetOf(regionSeed))
        regionsFences += regionFences.sum() * regionFences.size
        regionsEdges += regionEdges.sum() * regionEdges.size
    }
    //A
    println(regionsFences)
    //B
    println(regionsEdges)
}

private fun getPlot(
    x: Int,
    y: Int,
    horizontalFences: List<List<Boolean>>,
    verticalFences: List<List<Boolean>>,
    range: Pair<Int, Int>
): Plot {
    val connections = mutableListOf<Pair<Int, Int>>()
    val westFence = x == 0 || horizontalFences[y][x - 1]
    val eastFence = x == range.first - 1 || horizontalFences[y][x]
    val northFence = y == 0 || verticalFences[y - 1][x]
    val southFence = y == range.second - 1 || verticalFences[y][x]
    val fences = listOf(
        westFence,
        eastFence,
        northFence,
        southFence
    ).filter { it }.size
    val westNorthFence = x > 0 && (y == 0 || verticalFences[y - 1][x - 1])
    val westSouthFence = x > 0 && (y == range.second - 1 || verticalFences[y][x - 1])
    val eastNorthFence = x < range.first - 1 && (y == 0 || verticalFences[y - 1][x + 1])
    val eastSouthFence = x < range.first - 1 && (y == range.second - 1 || verticalFences[y][x + 1])
    val northWestFence = y > 0 && (x == 0 || horizontalFences[y - 1][x - 1])
    val northEastFence = y > 0 && (x == range.first - 1 || horizontalFences[y - 1][x])
    val southWestFence = y < range.second - 1 && (x == 0 || horizontalFences[y + 1][x - 1])
    val southEastFence = y < range.second - 1 && (x == range.first - 1 || horizontalFences[y + 1][x])
    val edges = listOf(
        westFence && northFence,
        westFence && southFence,
        eastFence && northFence,
        eastFence && southFence,
        westNorthFence && northWestFence && !(northFence || westFence),
        eastNorthFence && northEastFence && !(northFence || eastFence),
        westSouthFence && southWestFence && !(westFence || southFence),
        eastSouthFence && southEastFence && !(eastFence || southFence),
    ).filter { it }.size
    if (!westFence)
        connections.add(Pair(x - 1, y))
    if (!eastFence)
        connections.add(Pair(x + 1, y))
    if (!northFence)
        connections.add(Pair(x, y - 1))
    if (!southFence)
        connections.add(Pair(x, y + 1))

    return Plot(fences, edges, connections)
}

private fun addToRegion(
    regionFences: MutableList<Int>,
    regionEdges: MutableList<Int>,
    seed: Pair<Int, Int>,
    grid: MutableMap<Pair<Int, Int>, Plot>,
    visited: MutableSet<Pair<Int, Int>>
) {
    val plot = grid.remove(seed)!!
    regionFences.add(plot.fences)
    regionEdges.add(plot.edges)
    val toVisit = plot.connections.filter {
        it !in visited && it in grid.keys
    }
    visited.addAll(toVisit)
    toVisit.map {
        addToRegion(regionFences, regionEdges, it, grid, visited)
    }
}

private data class Plot(val fences: Int, val edges: Int, val connections: MutableList<Pair<Int, Int>>)