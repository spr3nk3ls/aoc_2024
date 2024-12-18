package spr3nk3ls.day18

import spr3nk3ls.util.Utils

fun main() {
    getSolution("day18/example.txt", 6, 12)
    // 140 too low
    getSolution("day18/input.txt", 70, 1024)
}

fun getSolution(filename: String, range: Int, corruption: Int) {
    val corrupted = Utils.readLines(filename).take(corruption).map {
        it.split(",").map { it.toInt() }
    }.map { it.first() to it.last() }
    val grid = (0..range).flatMap { y ->
        (0..range).filter { x -> x to y !in corrupted }.map { x -> x to y }
    }.toMutableSet()
    val deque = ArrayDeque<List<Pair<Int, Int>>>()
    deque.add(listOf(0 to 0))
    grid.remove(0 to 0)
    while (deque.first().last() != range to range) {
        val route = deque.removeFirst()
        available(route.last(), grid).forEach { av ->
            deque.add(route + av)
            grid.remove(av)
        }
    }
    println(deque.first().size - 1)
}

private fun available(node: Pair<Int, Int>, grid: MutableSet<Pair<Int, Int>>): List<Pair<Int, Int>> {
    return listOf(
        node.first to node.second + 1,
        node.first to node.second - 1,
        node.first + 1 to node.second,
        node.first - 1 to node.second
    ).filter {
        it.first >= 0 && it.second >= 0
    }.filter {
        it in grid
    }
}