package spr3nk3ls.day1

import java.io.File
import kotlin.math.abs

fun main() {
    getSolutionA("src/main/resources/day1/example.txt")
    getSolutionA("src/main/resources/day1/input.txt")
    getSolutionB("src/main/resources/day1/example.txt")
    getSolutionB("src/main/resources/day1/input.txt")
}

fun getSolutionA(filename: String) {
    val first = mutableListOf<Int>()
    val second = mutableListOf<Int>()
    File(filename).forEachLine {
        line ->
        run {
            val s = line.split("   ").toTypedArray()
            first.add(s.first().toInt())
            second.add(s.last().toInt())
        }
    }
    first.sort()
    second.sort()
    val diff = mutableListOf<Int>()
    for (i in 0 .. first.lastIndex) {
        diff.add(abs( first[i] - second[i]))
    }
    println(diff.sum())
}

fun getSolutionB(filename: String) {
    val first = mutableListOf<Int>()
    val second = mutableListOf<Int>()
    File(filename).forEachLine { line ->
        run {
            val s = line.split("   ").toTypedArray()
            first.add(s.first().toInt())
            second.add(s.last().toInt())
        }
    }
    println(first.map {
        element -> element*second.count{it==element}
    }.sum())
}