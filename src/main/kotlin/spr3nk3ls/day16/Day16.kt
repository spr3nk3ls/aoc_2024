package spr3nk3ls.day16

import spr3nk3ls.util.Utils
import java.util.*
import kotlin.math.abs

private enum class Direction(val dir: Pair<Int, Int>) {
    NORTH(0 to -1), EAST(1 to 0), SOUTH(0 to 1), WEST(-1 to 0)
}

fun main() {
    // 7036
    getSolution("day16/example1.txt")
    getSolution("day16/example2.txt")
    getSolution("day16/input.txt")
}

fun getSolution(filename: String) {
    val grid = Utils.readLines(filename)
    val nodeMap = getNodeMap(grid).toMutableMap()
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
    nodeMap[start] = nodeMap[start]!!.map { node ->
        if (node.first.first == 1) {
            node.first to node.second + 1000
        } else {
            node
        }
    }
    val result = dijkstra(nodeMap, start)
    println(result[end]!! - 1000)
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
    return (connections.contains(Direction.NORTH) && connections.contains(Direction.EAST)) || (connections.contains(
        Direction.NORTH
    ) && connections.contains(Direction.WEST)) || (connections.contains(Direction.SOUTH) && connections.contains(
        Direction.EAST
    )) || (connections.contains(Direction.SOUTH) && connections.contains(Direction.WEST))
}

private fun dijkstra(
    graph: Map<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Int>>>, start: Pair<Int, Int>
): Map<Pair<Int, Int>, Int> {
    val distances = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }
    val priorityQueue = PriorityQueue<Pair<Pair<Int, Int>, Int>>(compareBy { it.second }).apply { add(start to 0) }

    distances[start] = 0

    while (priorityQueue.isNotEmpty()) {
        val (node, currentDist) = priorityQueue.poll()
        graph[node]?.forEach { (adjacent, weight) ->
            val totalDist = currentDist + weight + 1000
            if (totalDist < distances.getValue(adjacent)) {
                distances[adjacent] = totalDist
                priorityQueue.add(adjacent to totalDist)
            }
        }
    }
    return distances
}

fun isHallway(pair: Pair<Int, Int>, grid: List<String>): Boolean {
    return grid[pair.second][pair.first] in setOf('.', 'S', 'E')
}
