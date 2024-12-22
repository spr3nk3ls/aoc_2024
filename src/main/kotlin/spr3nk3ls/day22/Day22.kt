package spr3nk3ls.day22

import spr3nk3ls.util.Utils

fun main() {
    Day22().getSolutionA("day22/example1.txt")
    Day22().getSolutionA("day22/input.txt")
    Day22().getSolutionB("day22/example2.txt")
    Day22().getSolutionB("day22/input.txt")
}

class Day22 {
    fun getSolutionA(filename: String) {
        val result = Utils.readLines(filename).map { it.toLong() }.sumOf {
            next(it, 2000)
        }
        println(result)
    }

    fun next(number: Long, times: Long): Long {
        var result = number
        for (i in 0..<times) {
            result = next(result)
        }
        return result
    }

    fun next(number: Long): Long {
        return third(second(first(number)))
    }

    private fun first(number: Long) :Long {
        val by64 = number*64
        return prune(mix(number, by64))
    }

    private fun second(number: Long) :Long {
        val by32 = number/32
        return prune(mix(number, by32))
    }

    private fun third(number: Long) :Long {
        val by2048 = number*2048
        return prune(mix(number, by2048))
    }

    private fun mix(number: Long, other: Long) : Long {
        return number xor other
    }

    private fun prune(number: Long) : Long {
        return number.mod(16777216L)
    }

    fun getSolutionB(filename: String) {
        val diffs = Utils.readLines(filename).map { it.toLong() }.map { nextDiff(it) }
        val windowSet = getWindowSet(diffs.map { it.map { it.second } })
        val windowSize = windowSet.size
        var i = 0
        val result = windowSet.map { window ->
            if(i % 1000 == 0)
            println("$i of $windowSize")
            i++
            getBananaSum(diffs, window)
        }.max()
        println(result)
    }

    fun getBananaSum(diffs: List<List<Pair<Long, Long>>>, changes: List<Long>): Long {
        return diffs.sumOf { list ->
            var sum = 0L
            for (i in 0..list.size - 4) {
                if (list[i + 0].second == changes[0] && list[i + 1].second == changes[1] &&
                    list[i + 2].second == changes[2] && list[i + 3].second == changes[3]) {
                    sum = list[i + 3].first
                    break
                }
            }
            sum
        }
    }

    fun getWindowSet(diffs: List<List<Long>>): Set<List<Long>> {
        return diffs.flatMap {
            it.windowed(4).toSet()
        }.toSet()
    }

    fun nextDiff(number: Long, times: Int = 2000): List<Pair<Long, Long>> {
        return generateSequence(number to 0L) { (n, _) ->
            val nextNumber = next(n)
            nextNumber to (nextNumber % 10L) - (n % 10L)
        }.take(times).drop(1).map { it.first % 10 to it.second }.toList()
    }
}