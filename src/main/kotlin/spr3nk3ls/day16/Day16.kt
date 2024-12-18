package spr3nk3ls.day16

import spr3nk3ls.util.Utils
import kotlin.math.abs

private enum class Direction(val dir: Pair<Int, Int>) {
    NORTH(0 to -1), EAST(1 to 0), SOUTH(0 to 1), WEST(-1 to 0)
}

fun main() {
    // 7036
    getSolution("day16/example.txt")
//    getSolutionA("day16/input.txt")
}

fun getSolution(filename: String) {
    val grid = Utils.readLines(filename)
    val nodeMap = getNodeMap(grid)
    //TODO implement Dijkstra
    nodeMap.entries.forEach(::println)
}

private fun getNodeMap(grid: List<String>): Map<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Int>>> {
    val nodes = grid.indices.flatMap { y ->
        grid[y].indices.filter { x ->
            isHallway(x to y, grid)
        }.map { x ->
            x to Direction.entries.filter { grid[it.dir.second + y][it.dir.first + x] == '.' }.toMutableSet()
        }.filter { (x, connections) ->
            corner(connections) || grid[y][x] == 'S' || grid[y][x] == 'E'
        }.map { (x, connections) -> (x to y) to connections }
    }
    val nodeSet = nodes.unzip().first.toSet()
    return nodes.associate { node ->
        val nodeConnections = node.second.map {
            it.dir
        }.flatMap { dir ->
            generateSequence(node.first.first + dir.first to node.first.second + dir.second) { it.first + dir.first to it.second + dir.second }.takeWhile {
                isHallway(it, grid)
            }.filter { it in nodeSet }
                .map { it to abs(node.first.first - it.first) + abs(node.first.second - it.second) }
        }
        node.first to nodeConnections
    }
}

private fun corner(connections: Set<Direction>): Boolean {
    return (connections.contains(Direction.NORTH) && connections.contains(Direction.EAST)) ||
            (connections.contains(Direction.NORTH) && connections.contains(Direction.WEST)) ||
            (connections.contains(Direction.SOUTH) && connections.contains(Direction.EAST)) ||
            (connections.contains(Direction.SOUTH) && connections.contains(Direction.WEST))
}

fun isHallway(pair: Pair<Int, Int>, grid: List<String>): Boolean {
    return grid[pair.second][pair.first] in setOf('.', 'S', 'E')
}
