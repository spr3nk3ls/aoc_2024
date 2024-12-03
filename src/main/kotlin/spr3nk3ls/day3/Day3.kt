package spr3nk3ls.day1

import spr3nk3ls.util.Utils

fun main() {
    getSolutionA("day3/example1.txt")
    getSolutionA("day3/input.txt")
    getSolutionB("day3/example2.txt")
    getSolutionB("day3/input.txt")
}

private fun getSolutionA(filename: String) {
    val line = Utils.readLines(filename).joinToString { it }
    val result = Regex("mul\\((\\d+),(\\d+)\\)").findAll(line)
        .sumOf { it.groupValues[1].toInt()*it.groupValues[2].toInt() }
    println(result)
}

private fun getSolutionB(filename: String) {
    val line = Utils.readLines(filename).joinToString { it }
    val stripDontDo = Regex("don't\\(\\).*?do\\(\\)").replace(line, "")
    val stripAfterFirstDont = Regex("don't\\(\\).*").replace(stripDontDo, "")
    val result = Regex("mul\\((\\d+),(\\d+)\\)").findAll(stripAfterFirstDont)
        .sumOf { it.groupValues[1].toInt()*it.groupValues[2].toInt() }
    println(result)
}