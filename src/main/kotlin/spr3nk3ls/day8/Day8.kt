package spr3nk3ls.day8

import spr3nk3ls.util.Utils

fun main() {
    getSolution("day8/example.txt", Node::projectOne)
    getSolution("day8/input.txt", Node::projectOne)
    getSolution("day8/example.txt", Node::projectAll)
    getSolution("day8/input.txt", Node::projectAll)
}

private fun getSolution(filename: String, project: (Node, Node, Pair<Int, Int>) -> Set<Pair<Int, Int>>) {
    val lines = Utils.readLines(filename)
    val range = lines.first().length to lines.size
    val nodes = scanLines(lines, range).toSet()
    val antinodes = nodes.groupBy { it.frequency }.flatMap { (_, freqNodes) ->
        freqNodes.flatMap { freqNode1 ->
            freqNodes.filter { it != freqNode1 }.flatMap { freqNode2 ->
                project(freqNode1, freqNode2, range)
            }
        }
    }.toSet()
    println(antinodes.size)
}

private fun scanLines(lines: List<String>, range: Pair<Int, Int>): List<Node> = (0..<range.second).flatMap { y ->
    (0..<range.first).filter { lines[y][it] != '.' }.map {
        Node((it to y), lines[y][it])
    }
}

private data class Node(val index: Pair<Int, Int>, val frequency: Char) {
    private fun calculateDifference(otherNode: Node): Pair<Int, Int> =
        (index.first - otherNode.index.first) to (index.second - otherNode.index.second)

    fun projectOne(otherNode: Node, range: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val difference = calculateDifference(otherNode)
        val projected = index.first + difference.first to index.second + difference.second
        return if (withinBounds(projected, range)) setOf(projected) else emptySet()
    }

    fun projectAll(otherNode: Node, range: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val difference = calculateDifference(otherNode)
        return generateSequence(index) { (x, y) -> x + difference.first to y + difference.second }.takeWhile {
            withinBounds(
                it, range
            )
        }.toSet()
    }
}

private fun withinBounds(index: Pair<Int, Int>, range: Pair<Int, Int>): Boolean =
    index.first >= 0 && index.first < range.first && index.second >= 0 && index.second < range.second