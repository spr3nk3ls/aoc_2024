package spr3nk3ls.day11

import spr3nk3ls.util.Utils


fun main() {
    getSolution("day11/example.txt")
    getSolution("day11/input.txt")
}

fun getSolution(filename: String) {
    var numbers = Utils.readLines(filename).first().split(" ").map { it.toLong() }
    for(i in 0 until 25) {
        numbers = numbers.flatMap {
            if (it == 0L) {
                listOf(1)
            } else {
                val str = it.toString()
                val ndig = str.length
                if (ndig % 2 == 0) {
                    listOf(str.substring(0, ndig / 2).toLong(), str.substring(ndig / 2).toLong())
                } else {
                    listOf(it * 2024)
                }
            }
        }
    }
    println(numbers.size)
}
