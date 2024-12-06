package spr3nk3ls.day6

import spr3nk3ls.util.Utils

fun main() {
    getSolution("day6/example.txt")
    getSolution("day6/input.txt")
}

private fun getSolution(filename: String) {
    val lines = Utils.readLines(filename)
    val range = lines.first().length to lines.size
    val obstacles = scanLines(lines, range, '#').toSet()
    val index = scanLines(lines, range, '^').first()
    val guard = Guard(index, 0 to -1)

    // A
    val route = generateSequence(guard) { it.move(obstacles) }.takeWhile { withinBounds(it.index, range) }
    val positions = route.map { it.index }.toSet()
    println(positions.count())

    // B
    val obstaclesWithExtra = positions.map { obstacles + it }
    val numberOfLoops = obstaclesWithExtra.count { withExtra ->
        val routeWithExtraObstacle = generateSequence(guard) { it.move(withExtra) }
            .takeWhile { withinBounds(it.index, range) }
        detectLoop(routeWithExtraObstacle)
    }
    println(numberOfLoops)
}

private fun scanLines(lines: List<String>, range: Pair<Int, Int>, character: Char): List<Pair<Int, Int>> {
    return (0..<range.second).flatMap { y ->
        (0..<range.first).map { x ->
            (lines[y][x] == character) to (x to y)
        }.filter { it.first }.map { it.second }
    }
}

private data class Guard(val index: Pair<Int, Int>, val direction: Pair<Int, Int>){
    fun move(obstacles: Set<Pair<Int, Int>>): Guard {
        val newIndex = (index.first + direction.first) to (index.second + direction.second)
        return if (newIndex in obstacles)
            Guard(index, -direction.second to direction.first)
        else
            Guard(newIndex, direction)
    }
}

private fun withinBounds(index: Pair<Int, Int>, range: Pair<Int, Int>): Boolean {
    return index.first >= 0 && index.first < range.first && index.second >= 0 && index.second < range.second
}

private fun detectLoop(sequence: Sequence<Guard>): Boolean {
    val seen = mutableSetOf<Guard>()
    return sequence.any { !seen.add(it) }
}