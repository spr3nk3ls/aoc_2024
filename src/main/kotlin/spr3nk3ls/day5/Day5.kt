package spr3nk3ls.day5

import java.io.File

fun main() {
    getSolution("day5/example.txt")
    getSolution("day5/input.txt")
}

private fun getSolution(filename: String) {
    val (rules, pages) = readAndSplitFile(filename)
    val splitRules = rules.map {
        val split = it.split("|")
        split[0] to split[1]
    }
    val result = pages.map {
        val unsorted = it.split(",")
        val sorted = unsorted.sortedWith(RuleComparator(splitRules))
        Pair(unsorted == sorted, sorted[sorted.size / 2].toInt())
    }
    //A
    println(result.filter { (i, _) -> i }.sumOf { (_, j) -> j })
    //B
    println(result.filter { (i, _) -> !i }.sumOf { (_, j) -> j })
}

private class RuleComparator(private val rules: List<Pair<String, String>>) : Comparator<String> {
    override fun compare(number1: String, number2: String): Int {
        if (rules.any {
                it.first == number1 && it.second == number2
            }) return -1
        if (rules.any {
                it.first == number2 && it.second == number1
            }) return 1
        return 0
    }
}

private fun readAndSplitFile(filename: String) = File("src/main/resources/$filename")
    .readText(Charsets.UTF_8).split("\n\n").map { it.split("\n") }

