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
    val direction = 0 to -1
    val pointer = index to direction

    // A
    val sequence = generateSequence(pointer) { move(it, obstacles) }.takeWhile { withinBounds(it, range) }
    val result = sequence.map { it.first }.distinct()
    println(result.count())

    // B
    val additionalObstacles = result
    val resultB = additionalObstacles.count { obstacle ->
        val sequenceWithAdditionalObstacle = generateSequence(pointer) { move(it, obstacles + obstacle) }
            .takeWhile { withinBounds(it, range) }
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
    pointer: Pair<Pair<Int, Int>, Pair<Int, Int>>,
    obstacles: List<Pair<Int, Int>>
): Pair<Pair<Int, Int>, Pair<Int, Int>> {
    val index = pointer.first
    val direction = pointer.second
    val newIndex = (index.first + direction.first) to (index.second + direction.second)
    if (obstacles.contains(newIndex)) {
        return index to rotate(direction)
    }
    return newIndex to direction
}

fun rotate(direction: Pair<Int, Int>): Pair<Int, Int> {
    return (-direction.second to direction.first)
}

fun withinBounds(indexToDirection: Pair<Pair<Int, Int>, Pair<Int, Int>>, range: Pair<Int, Int>): Boolean {
    val index = indexToDirection.first
    return index.first >= 0 && index.first < range.first && index.second >= 0 && index.second < range.second
}

fun detectLoop(sequence: Sequence<Pair<Pair<Int, Int>, Pair<Int, Int>>>): Boolean {
    val seen = mutableSetOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    for (element in sequence) {
        if (element in seen) {
            return true
        }
        seen.add(element)
    }
    return false
}