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
    val (result, predecessors) = dijkstra(nodeMap, start, end)
    println(result[end]!! - 1000)

    val paths = mutableSetOf<List<Pair<Int, Int>>>()
    var pathgen = setOf(listOf(end))
    while (pathgen.isNotEmpty()) {
        pathgen = pathgen.filter {
            predecessors[it.last()] != null
        }.flatMap { list ->
            predecessors[list.last()]!!.map {
                list + it
            }
        }.toSet()
        pathgen.filter { it.last() == start }.forEach { paths.add(it) }
    }
    val count = paths.flatMap { path ->
        path.indices.drop(1).flatMap { i ->
            val j = path[i - 1]
            val k = path[i]
            if (j.first == k.first) {
                if (j.second < k.second) {
                    (j.second..k.second).map { path[i].first to it }
                } else {
                    (k.second..j.second).map { path[i].first to it }
                }
            } else {
                if (j.first < k.first) {
                    (j.first..k.first).map { it to j.second }
                } else {
                    (k.first..j.first).map { it to j.second }
                }
            }
        }
    }.toSet()
    println(count.size)
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
    graph: Map<Pair<Int, Int>, List<Pair<Pair<Int, Int>, Int>>>, start: Pair<Int, Int>, end: Pair<Int, Int>
): Pair<MutableMap<Pair<Int, Int>, Int>, MutableMap<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>> {
    val distances = mutableMapOf<Pair<Int, Int>, Int>().withDefault { Int.MAX_VALUE }
    val predecessors = mutableMapOf<Pair<Int, Int>, MutableSet<Pair<Int, Int>>>()
    val priorityQueue = PriorityQueue<Pair<Pair<Int, Int>, Int>>(compareBy { it.second }).apply { add(start to 0) }

    distances[start] = 0

    while (priorityQueue.isNotEmpty()) {
        val (node, currentDist) = priorityQueue.poll()
        graph[node]?.forEach { (adjacent, weight) ->
            val totalDist = currentDist + weight + 1000
            if (totalDist <= distances.getValue(adjacent)) {
                if (totalDist < distances.getValue(adjacent)) {
                    predecessors[adjacent] = mutableSetOf()
                }
                distances[adjacent] = totalDist
                if (predecessors[adjacent] == null)
                    predecessors[adjacent] = mutableSetOf()
                predecessors[adjacent]!!.add(node)
                priorityQueue.add(adjacent to totalDist)
            }
        }
    }
    return distances to predecessors
}

fun isHallway(pair: Pair<Int, Int>, grid: List<String>): Boolean {
    return grid[pair.second][pair.first] in setOf('.', 'S', 'E')
}
