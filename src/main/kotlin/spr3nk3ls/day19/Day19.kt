package spr3nk3ls.day19

import java.io.File

fun main() {
    getSolutionA("day19/example.txt")
    getSolutionA("day19/input.txt")
    getSolutionB("day19/example.txt")
    getSolutionB("day19/input.txt")
}

fun getSolutionA(filename: String) {
    val (towels, patterns) = readAndSplitFile(filename)
    val result = patterns.count { pattern ->
        var matches = setOf("")
        while (matches.isNotEmpty() && pattern !in matches) {
            matches = matches
                .flatMap { match -> towels.map { match + it } }
                .filter { pattern.startsWith(it) }
                .toSet()
        }
        pattern in matches
    }
    println(result)
}

fun getSolutionB(filename: String) {
    val (towels, patterns) = readAndSplitFile(filename)
    val total = patterns.size
    val result = patterns.sumOf { pattern ->
        val index = patterns.indexOf(pattern)
        println("Taken $index of $total patterns")
        var matches = setOf(listOf(""))
        var count = 0
        val len = pattern.length
        while (matches.isNotEmpty()) {
            matches = matches
                .asSequence()
                .flatMap { match -> towels.map { match + it } }
                .map { it to it.joinToString("") }
                .filter { (_, str) -> pattern.startsWith(str) }
                .filter { (_, str) -> str.length <= len }
                .map { (ls, _) -> ls }
                .toSet()
            val exactMatches = matches.filter { it.joinToString("") == pattern }
            count += exactMatches.size
            matches = matches.filter { it !in exactMatches }.toSet()
        }
        count
    }
    println(result)
}

private fun readAndSplitFile(filename: String): Pair<List<String>, List<String>> {
    val (tow, pat) = File("src/main/resources/$filename").readText(Charsets.UTF_8).split("\n\n")
    val towels = tow.split(", ")
    val patterns = pat.split("\n")
    return towels to patterns
}
