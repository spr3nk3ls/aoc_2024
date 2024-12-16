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
            if (grid[y][x] == '.' || startOrEnd) {
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
    val nodeMap = mutableMapOf<Pair<Int, Int>, MutableMap<Pair<Int, Int>, Pair<Int, Int>>>()
    val nodeSet = nodes.unzip().first.toSet()
    nodes.forEach { node ->
        node.second.forEach { initialDirection ->
            var dir = initialDirection.dir
            var pos = node.first.first + dir.first to node.first.second + dir.second
            while (pos !in nodeSet){
                while(grid[pos.second][pos.first] == '.'){
                    //TODO one too far
                    pos = pos.first + dir.first to pos.second + dir.second
                }
                val nextDir = (-dir.second to dir.first)
                val nextPos = pos.first + nextDir.first to pos.second + nextDir.second
                if(grid[nextPos.second][nextPos.first] == '.') {
                    dir = nextDir
                    // TODO if pos + turned dir is dot, then turn
                    //TODO
                    break
                }
                //TODO
                break
            }
            var entry = nodeMap[node.first]
            if(entry == null) {
                nodeMap[node.first] = mutableMapOf()
                entry = nodeMap[node.first]
            }
            entry!!.put(dir, pos)
            //calculate distance to second node or null if dead end
            //connect both nodes (add other node to map if unavailable)
            //then remove this direction and opposite direction of other node
        }
    }
    print(nodeMap)
    println(nodes)
}