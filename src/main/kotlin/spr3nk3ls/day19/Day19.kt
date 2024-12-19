package spr3nk3ls.day19

import java.io.File

fun main() {
    getSolution("day19/example.txt")
    getSolution("day19/input.txt")
}

fun getSolution(filename: String) {
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

private fun readAndSplitFile(filename: String): Pair<Set<String>, List<String>> {
    val (tow, pat) = File("src/main/resources/$filename").readText(Charsets.UTF_8).split("\n\n")
    val towels = tow.split(", ")
    val patterns = pat.split("\n")
    return towels.toSet() to patterns
}
