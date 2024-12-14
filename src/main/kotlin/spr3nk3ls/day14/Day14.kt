package spr3nk3ls.day14

import spr3nk3ls.util.Utils

fun main() {
    getSolutionA("day14/example.txt", 11L to 7L, 100L)
    getSolutionA("day14/input.txt", 101L to 103L, 100L)
    getSolutionB("day14/input.txt", 101L to 103L, 10000000L)
}

private fun getSolutionA(filename: String, range: Pair<Long, Long>, iterations: Long) {
    val robots = Utils.readLines(filename)
        .map { it.split(" ").map { it.split("=")[1].split(",").map { it.toLong() } } }
        .map { it[0].first() + iterations * it[1].first() to it[0].last() + iterations * it[1].last() }
        .map { wrap(it, range) }
    val result = listOf(
        robots.filter { it.first > range.first / 2 && it.second > range.second / 2 },
        robots.filter { it.first > range.first / 2 && it.second < range.second / 2 },
        robots.filter { it.first < range.first / 2 && it.second < range.second / 2 },
        robots.filter { it.first < range.first / 2 && it.second > range.second / 2 },
    ).map { it.size }.reduce { i, j -> i * j }
    println(result)
}

private fun getSolutionB(filename: String, range: Pair<Long, Long>, iterations: Long) {
    val robots = Utils.readLines(filename)
        .map { it.split(" ").map { it.split("=")[1].split(",").map { it.toLong() } } }

    for (i in 0 until iterations) {
        val iteratedRobots = robots
            .map { it[0].first() + i * it[1].first() to it[0].last() + i * it[1].last() }
            .map { wrap(it, range) }

        if (blockDetected(iteratedRobots)) {
            (0..<range.second).forEach { y ->
                (0..<range.first).forEach { x ->
                    if ((x to y) in iteratedRobots) print('x') else print('.')
                }
                print("\n")
            }
            println(i)
            break
        }
    }
}

fun wrap(robot: Pair<Long, Long>, range: Pair<Long, Long>): Pair<Long, Long> {
    val x = robot.first % range.first
    val y = robot.second % range.second
    return (if (x < 0) (range.first + x) else x) to (if (y < 0) (range.second + y) else y)
}

fun blockDetected(iteratedRobots: List<Pair<Long, Long>>): Boolean {
    val robotSet = iteratedRobots.toSet()
    for ((x, y) in iteratedRobots) {
        if (
            (x to y + 1) in robotSet &&
            (x + 1 to y) in robotSet &&
            (x + 1 to y + 1) in robotSet &&
            (x to y + 2) in robotSet &&
            (x + 1 to y + 2) in robotSet &&
            (x to y + 3) in robotSet &&
            (x + 1 to y + 3) in robotSet &&
            (x to y + 4) in robotSet &&
            (x + 1 to y + 4) in robotSet
        ) {
            return true
        }
    }
    return false
}
