package spr3nk3ls.day1

import spr3nk3ls.util.Utils
import kotlin.math.abs

fun main() {
    getSolutionA("day1/example.txt")
    getSolutionA("day1/input.txt")
    getSolutionB("day1/example.txt")
    getSolutionB("day1/input.txt")
}

private fun getSolutionA(filename: String) {
    val (first, second) = getColumns(filename)

    val diff = first.sorted().zip(second.sorted()) { a, b -> abs(a - b) }
    println(diff.sum())
}

private fun getSolutionB(filename: String) {
    val (first, second) = getColumns(filename)

    val products = first.map {
        element -> element*second.count{ it==element }
    }
    println(products.sum())
}

private fun getColumns(filename: String) = Utils.readLines(filename).map { line ->
    val (f, s) = line.split("   ").map { it.toInt() }
    f to s
}.unzip()