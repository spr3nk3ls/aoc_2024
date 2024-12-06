package spr3nk3ls.day6

import spr3nk3ls.util.Utils

fun main() {
    getSolution("day6/example.txt")
    getSolution("day6/input.txt")
}

private fun getSolution(filename: String) {
    val lines = Utils.readLines(filename)
    val range = lines.first().length to lines.size
    val obstacles = scanLines(lines, range, '#')
    val index = scanLines(lines, range, '^').first()
    val pointer = Pointer(index, 0 to -1)

    // A
    val sequence = generateSequence(pointer) { move(it, obstacles) }.takeWhile { withinBounds(it.index, range) }
    val result = sequence.map { it.index }.distinct()
    println(result.count())

    // B
    val additionalObstacles = result
    val resultB = additionalObstacles.count { additional ->
        val sequenceWithAdditionalObstacle = generateSequence(pointer) { move(it, obstacles + additional) }
            .takeWhile { withinBounds(it.index, range) }
        detectLoop(sequenceWithAdditionalObstacle)
    }
    println(resultB)
}

private fun scanLines(lines: List<String>, range: Pair<Int, Int>, character: Char): List<Pair<Int, Int>> {
    return (0..<range.second).flatMap { y ->
        (0..<range.first).map { x ->
            (lines[y][x] == character) to (x to y)
        }.filter { it.first }.map { it.second }
    }
}

fun move(
    pointer: Pointer,
    obstacles: List<Pair<Int, Int>>
): Pointer {
    val index = pointer.index
    val direction = pointer.direction
    val newIndex = (index.first + direction.first) to (index.second + direction.second)
    if (obstacles.contains(newIndex)) {
        return Pointer(index, rotate(direction))
    }
    return Pointer(newIndex, direction)
}

fun rotate(direction: Pair<Int, Int>): Pair<Int, Int> {
    return (-direction.second to direction.first)
}

fun withinBounds(index: Pair<Int, Int>, range: Pair<Int, Int>): Boolean {
    return index.first >= 0 && index.first < range.first && index.second >= 0 && index.second < range.second
}

fun detectLoop(sequence: Sequence<Pointer>): Boolean {
    val seen = mutableSetOf<Pointer>()
    return sequence.any { !seen.add(it) }
}

data class Pointer(val index: Pair<Int, Int>, val direction: Pair<Int, Int>)