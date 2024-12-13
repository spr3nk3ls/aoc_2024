package spr3nk3ls.day13

import java.io.File

private val bigNumber = 10000000000000L

fun main() {
    getSolution("day13/example.txt", false)
    getSolution("day13/input.txt", false)
    getSolution("day13/example.txt", true)
    getSolution("day13/input.txt", true)
}

private fun getSolution(filename: String, addBigNumber: Boolean) {
    val coefs = readAndSplitFile(filename, addBigNumber)
    val results = coefs.map {
        with(it) {
            val det_d = a.first * b.second - b.first * a.second
            if (det_d == 0L)
                return@map 0L to 0L
            val det_n1 = x.first * b.second - b.first * x.second
            if (det_n1 % det_d != 0L)
                return@map 0L to 0L
            val det_n2 = a.first * x.second - x.first * a.second
            if (det_n2 % det_d != 0L)
                return@map 0L to 0L
            det_n1 / det_d to det_n2 / det_d
        }
    }.sumOf { it.first * 3 + it.second }
    println(results)
}

private fun readAndSplitFile(filename: String, addBigNumber: Boolean) = File("src/main/resources/$filename")
    .readText(Charsets.UTF_8).split("\n\n").map {
        Claw(Regex("\\d+").findAll(it).map { it.groupValues[0].toLong() }.toList(), addBigNumber)
    }

private class Claw(groupValues: List<Long>, addBigNumber: Boolean) {
    val a = groupValues[0] to groupValues[1]
    val b = groupValues[2] to groupValues[3]
    val x =
        if (addBigNumber) groupValues[4] + bigNumber to groupValues[5] + bigNumber else groupValues[4] to groupValues[5]

    override fun toString(): String {
        return a.toString() + ", " + b.toString() + ", " + x.toString()
    }
}

