package spr3nk3ls.day15

import java.io.File


fun main() {
    getSolutionA("day15/example1.txt")
    getSolutionA("day15/example2.txt")
    getSolutionA("day15/input.txt")
    getSolutionB("day15/example3.txt")
    getSolutionB("day15/example2.txt")
    getSolutionB("day15/input.txt")
}

private fun getSolutionA(filename: String) {
    val (grid, moves) = readAndSplitFile(filename)
    val range = grid.first().length to grid.size
    val walls = scanLines(grid, range, '#')
    val boxes = scanLines(grid, range, 'O').toMutableSet()
    var fish = scanLines(grid, range, '@').first()
    moves.forEach { move ->
        val direction = when (move) {
            '^' -> 0 to -1
            'v' -> 0 to 1
            '>' -> 1 to 0
            '<' -> -1 to 0
            else -> throw IllegalArgumentException()
        }
        var wall = false
        val boxesToMove = generateSequence(fish.first + direction.first to fish.second + direction.second) {
            it.first + direction.first to it.second + direction.second
        }.takeWhile {
            wall = it in walls
            it in boxes && !wall
        }.toList()
        if (wall) {
            return@forEach
        }
        if (boxesToMove.isNotEmpty()) {
            boxes.remove(boxesToMove.first())
            boxes.add(with(boxesToMove.last()) {
                first + direction.first to second + direction.second
            })
        }
        val newFish = fish.first + direction.first to fish.second + direction.second
        if (newFish !in walls) {
            fish = newFish
        }
    }
    println(boxes.sumOf {
        it.first + 100 * it.second
    })
}

private fun getSolutionB(filename: String) {
    val (grid, moves) = readAndSplitFile(filename)
    val range = grid.first().length to grid.size
    val walls =
        scanLines(grid, range, '#').flatMap { setOf(2 * it.first to it.second, 2 * it.first + 1 to it.second) }.toSet()
    val boxes = scanLines(grid, range, 'O').map { 2 * it.first to it.second }.toMutableSet()
    var fish = scanLines(grid, range, '@').first()
    fish = 2 * fish.first to fish.second
    moves.forEach { move ->
        val direction = when (move) {
            '^' -> 0 to -1
            'v' -> 0 to 1
            '>' -> 1 to 0
            '<' -> -1 to 0
            else -> throw IllegalArgumentException()
        }
        if (direction.second == 0) {
            val boxesToMove = horizontalBoxes(direction, fish, boxes)
            val xWall = if (direction.first == 1) 2 else -1
            if (boxesToMove.isNotEmpty() && boxesToMove.last().first + xWall to fish.second !in walls) {
                moveBoxes(boxesToMove, boxes, direction)
            }
        } else {
            val boxesToMove = verticalBoxes(fish, direction, boxes)
            if (boxesToMove.isNotEmpty() && boxesToMove.all {
                    it.first to it.second + direction.second !in walls && it.first + 1 to it.second + direction.second !in walls
                }) {
                moveBoxes(boxesToMove, boxes, direction)
            }
        }
        val newFish = fish.first + direction.first to fish.second + direction.second
        if (newFish !in walls && newFish !in boxes && newFish.first - 1 to newFish.second !in boxes) {
            fish = newFish
        }
    }
    printgrid(fish, boxes, walls, range.first * 2 to range.second)
    println(boxes.sumOf {
        it.first + 100 * it.second
    })
}

private fun horizontalBoxes(
    direction: Pair<Int, Int>, fish: Pair<Long, Long>, boxes: MutableSet<Pair<Long, Long>>
): List<Pair<Long, Long>> {
    val xSeed = if (direction.first == 1) 1 else -2
    val boxesToMove = generateSequence(fish.first + xSeed to fish.second) {
        it.first + 2 * direction.first to it.second
    }.takeWhile {
        it in boxes
    }.toList()
    return boxesToMove
}

private fun verticalBoxes(
    fish: Pair<Long, Long>, direction: Pair<Int, Int>, boxes: MutableSet<Pair<Long, Long>>
): List<Pair<Long, Long>> {
    val middle = fish.first to fish.second + direction.second
    val boxesToMove = generateSequence(
        setOf(middle, middle.first - 1 to middle.second).filter { it in boxes }.toSet()
    ) {
        it.flatMap {
            val middle = it.first to it.second + direction.second
            setOf(middle, middle.first - 1 to middle.second, middle.first + 1 to middle.second)
        }.filter {
            it in boxes
        }.toSet()
    }.takeWhile {
        it.any { it in boxes }
    }.flatMap { it }.toList()
    return boxesToMove
}

private fun moveBoxes(
    boxesToMove: List<Pair<Long, Long>>, boxes: MutableSet<Pair<Long, Long>>, direction: Pair<Int, Int>
) {
    boxesToMove.forEach {
        boxes.remove(it)
    }
    boxesToMove.forEach {
        boxes.add(it.first + direction.first to it.second + direction.second)
    }
}

fun printgrid(
    fish: Pair<Long, Long>, boxes: MutableSet<Pair<Long, Long>>, walls: Set<Pair<Long, Long>>, range: Pair<Int, Int>
) {
    (0..<range.second).forEach { y ->
        (0..<range.first).forEach {
            val gridpoint = it.toLong() to y.toLong()
            when (gridpoint) {
                in walls -> {
                    print("#")
                }

                in boxes -> {
                    print("O")
                }

                fish -> {
                    print("@")
                }

                else -> {
                    print(".")
                }
            }
        }
        print("\n")
    }
}

private fun readAndSplitFile(filename: String): Pair<List<String>, String> {
    val (grid, moves) = File("src/main/resources/$filename").readText(Charsets.UTF_8).split("\n\n")
    return grid.split("\n") to moves.split("\n").joinToString("")
}

private fun scanLines(lines: List<String>, range: Pair<Int, Int>, character: Char): Set<Pair<Long, Long>> {
    return (0..<range.second).flatMap { y ->
        (0..<range.first).map { x ->
            (lines[y][x] == character) to (x.toLong() to y.toLong())
        }.filter { it.first }.map { it.second }
    }.toSet()
}