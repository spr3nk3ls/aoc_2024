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

    val result = patterns.map { recursive(it, towels, mutableMapOf<String, Long>()) }

    println(result.sum())
}

private fun recursive(toSolve: String, towels: List<String>, cache: MutableMap<String, Long>): Long {
    if (cache.containsKey(toSolve)) {
        return cache[toSolve]!!
    }
    val result = towels.filter {
        toSolve.startsWith(it)
    }.sumOf {
        if (toSolve == it) {
            1
        } else {
            recursive(toSolve.substring(it.length), towels, cache)
        }
    }
    cache[toSolve] = result
    return result
}

private fun readAndSplitFile(filename: String): Pair<List<String>, List<String>> {
    val (tow, pat) = File("src/main/resources/$filename").readText(Charsets.UTF_8).split("\n\n")
    val towels = tow.split(", ")
    val patterns = pat.split("\n")
    return towels to patterns
}
