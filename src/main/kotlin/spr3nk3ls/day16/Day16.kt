package spr3nk3ls.day16

import spr3nk3ls.util.Utils

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
    val nodes = grid.indices.flatMap { y ->
        grid[y].indices.map { x ->
            val startOrEnd = grid[y][x] == 'S' || grid[y][x] == 'E'
            if (isHallway(x to y, grid)) {
                val connections = Direction.entries
                    .filter { grid[it.dir.second + y][it.dir.first + x] == '.' }.toMutableSet()
                if (connections.size >= 3 || startOrEnd) {
                    (y to x) to connections
                } else {
                    null
                }
            } else {
                null
            }
        }
    }.filterNotNull()
    nodes.forEach { node -> println(node) }
    val nodeSet = nodes.unzip().first.toSet()
    val result = nodes.map { node ->
        val nodeConnections = node.second.map {
            it.dir
        }.filter { isHallway(node.first.first + it.first to node.first.second + it.second, grid)
        }.map { initDir ->
            var pos = node.first.first + initDir.first to node.first.second + initDir.second
            var dir = initDir
            while (pos !in nodeSet){
                var nextPos = pos
                while(isHallway(nextPos, grid)){
                    pos = nextPos
                    nextPos = pos.first + dir.first to pos.second + dir.second
                }
                var newDir = (-dir.second to dir.first)
                if(isHallway(pos.first + newDir.first to pos.second + newDir.second, grid)){
                    dir = newDir
                    continue
                }
                newDir = (dir.second to -dir.first)
                if(isHallway(pos.first + newDir.first to pos.second + newDir.second, grid)){
                    dir = newDir
                    continue
                }
                println("dead end")
                println(pos)
                // Dead end
                break
            }
            (dir to pos)
//            println(pos)
//            println()
//            var entry = nodeMap[node.first]
//            if(entry == null) {
//                nodeMap[node.first] = mutableMapOf()
//                entry = nodeMap[node.first]
//            }
//            entry!!.put(dir, pos)
            //calculate distance to second node or null if dead end
            //connect both nodes (add other node to map if unavailable)
            //then remove this direction and opposite direction of other node
        }.toMap()
        node.first to nodeConnections
//        println(nodeConnections)
//    }.filter { it.second.isNotEmpty()
    }.toMap()
    result.entries.forEach(::println)
//    println(nodes)
}

fun isHallway(pair: Pair<Int, Int>, grid: List<String>): Boolean {
    return grid[pair.second][pair.first] in setOf('.','S','E')
}
