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
    val antinodes = nodes.groupBy { it.frequency }.flatMap { freqNodes ->
        freqNodes.value.flatMap { freqNode1 ->
            freqNodes.value.filter { it != freqNode1 }.flatMap { freqNode2 ->
                project.invoke(freqNode1, freqNode2, range)
            }
        }
    }.toSet()
    println(antinodes.size)
}

private fun scanLines(lines: List<String>, range: Pair<Int, Int>): List<Node> {
    return (0..<range.second).flatMap { y ->
        (0..<range.first)
            .filter { lines[y][it] != '.' }
            .map {
                Node((it to y), lines[y][it])
            }
    }
}

private data class Node(val index: Pair<Int, Int>, val frequency: Char) {
    fun projectOne(otherNode: Node, range: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val difference = (index.first - otherNode.index.first) to (index.second - otherNode.index.second)
        return listOf(index.first + difference.first to index.second + difference.second)
            .filter { withinBounds(it, range) }
            .toSet()
    }

    fun projectAll(otherNode: Node, range: Pair<Int, Int>): Set<Pair<Int, Int>> {
        val difference = (index.first - otherNode.index.first) to (index.second - otherNode.index.second)
        return generateSequence(0) { it + 1 }
            .map { index.first + it * difference.first to index.second + it * difference.second }
            .takeWhile { withinBounds(it, range) }
            .toSet()
    }
}

private fun withinBounds(index: Pair<Int, Int>, range: Pair<Int, Int>): Boolean {
    return index.first >= 0 && index.first < range.first && index.second >= 0 && index.second < range.second
}