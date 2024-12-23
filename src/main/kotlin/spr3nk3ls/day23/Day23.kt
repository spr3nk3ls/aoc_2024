package spr3nk3ls.day23

import spr3nk3ls.util.Utils

fun main() {
    getSolution("day23/example.txt")
    getSolution("day23/input.txt")
}

fun getSolution(filename: String) {
    val pairs = Utils.readLines(filename)
        .map { it.split("-") }
        .map { it[0] to it[1] }
    val computerSet = (pairs.map { it.first } + pairs.map { it.second }).toSet()
    val computerMap = computerSet.associateWith { mutableSetOf<String>() }
    pairs.forEach {
        computerMap[it.first]!!.add(it.second)
        computerMap[it.second]!!.add(it.first)
    }
    val triples = computerSet.flatMap{ one -> computerSet.filter{ one in computerMap[it]!! }.flatMap { two -> computerSet.filter {
        one in computerMap[it]!! && two in computerMap[it]!!
    }.map { setOf(one, two, it) } }}.toSet()

    val ts = computerSet.filter { it.startsWith("t") }
    val result = triples.count { it.any { it in ts } }

    println(result)
}
