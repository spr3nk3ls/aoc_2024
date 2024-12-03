package spr3nk3ls.day3

import spr3nk3ls.util.Utils

fun main() {
    getSolutionA("day3/example1.txt")
    getSolutionA("day3/input.txt")
    getSolutionB("day3/example2.txt")
    getSolutionB("day3/input.txt")
}

private fun getSolutionA(filename: String) {
    val line = Utils.readLines(filename).joinToString()
    val result = sumMuls(line)
    println(result)
}

private fun getSolutionB(filename: String) {
    val line = Utils.readLines(filename).joinToString()
    val sanitized = line
        .replace(Regex("don't\\(\\).*?do\\(\\)"), "")
        .replace(Regex("don't\\(\\).*"), "")
    val result = sumMuls(sanitized)
    println(result)
}

private fun sumMuls(line: String) = Regex("mul\\((\\d+),(\\d+)\\)")
    .findAll(line)
    .sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }