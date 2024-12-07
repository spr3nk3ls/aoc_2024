package spr3nk3ls.day7

import spr3nk3ls.util.Utils
import java.math.BigInteger
import kotlin.reflect.KFunction2


fun main() {
    getSolution("day7/example.txt", ::getFunctionListsA)
    getSolution("day7/input.txt", ::getFunctionListsA)
    getSolution("day7/example.txt", ::getFunctionListsB)
    getSolution("day7/input.txt", ::getFunctionListsB)
}

private fun getSolution(
    filename: String,
    getFunctionLists: (Int) -> List<List<KFunction2<BigInteger, BigInteger, BigInteger>>>
) {
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
        if (isCorrect) summed else BigInteger.ZERO
    }
    println(result)
}

private fun add(i: BigInteger, j: BigInteger): BigInteger = i + j
private fun prod(i: BigInteger, j: BigInteger): BigInteger = i * j

private fun getFunctionListsA(length: Int): List<List<KFunction2<BigInteger, BigInteger, BigInteger>>> {
    var list = listOf(listOf(::add), listOf(::prod))
    for (i in 1..<length) {
        list = list.flatMap { listOf(it + ::add, it + ::prod) }
    }
    return list
}

private fun concat(i: BigInteger, j: BigInteger): BigInteger = (i.toString() + j.toString()).toBigInteger()

private fun getFunctionListsB(length: Int): List<List<KFunction2<BigInteger, BigInteger, BigInteger>>> {
    var list = listOf(listOf(::add), listOf(::prod), listOf(::concat))
    for (i in 1..<length) {
        list = list.flatMap { listOf(it + ::add, it + ::prod, it + ::concat) }
    }
    return list
}