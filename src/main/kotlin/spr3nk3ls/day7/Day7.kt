package spr3nk3ls.day7

import spr3nk3ls.util.Utils
import java.math.BigInteger
import kotlin.reflect.KFunction2

private fun add(i: BigInteger, j: BigInteger): BigInteger = i + j
private fun prod(i: BigInteger, j: BigInteger): BigInteger = i * j

fun main() {
    getSolution("day7/example.txt")
    getSolution("day7/input.txt")
}

private fun getSolution(filename: String) {
    val lines = Utils.readLines(filename)
    val result = lines.sumOf {
        val (summedAsString, input) = it.split(": ")
        val summed = summedAsString.toBigInteger()
        val numbers = input
            .split(" ")
            .map { it.toBigInteger() }
        val isCorrect = getFunctionLists(numbers.size).any {
            it.zip(numbers).reduce { i, j -> j.first to i.first.invoke(i.second, j.second) }.second == summed
        }
        if (isCorrect) summed.toInt() else 0
    }
    println(result)
}

private fun getFunctionLists(length: Int): List<List<KFunction2<BigInteger, BigInteger, BigInteger>>> {
    var list = listOf(listOf(::add), listOf(::prod))
    for (i in 1..<length) {
        list = list.flatMap { listOf(it + ::add, it + ::prod) }
    }
    return list
}