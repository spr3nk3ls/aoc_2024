package spr3nk3ls.day11

import spr3nk3ls.util.Utils


fun main() {
    getSolution("day11/example.txt", 25)
    getSolution("day11/input.txt", 25)
    getSolution("day11/example.txt", 75)
    getSolution("day11/input.txt", 75)
}

fun getSolution(filename: String, blinks: Int) {
    val stones = Utils.readLines(filename).first().split(" ").map { it.toLong() }
    var cache = mutableMapOf<Pair<Long, Int>, Long>()
    val numberOfStones = stones.sumOf {
        transformStones(it, blinks, cache)
    }
    println(numberOfStones)
}

private fun transformStones(stone: Long, index: Int, cache: MutableMap<Pair<Long, Int>, Long>): Long {
    val cacheKey = stone to index
    if (cache.containsKey(cacheKey)) {
        return cache[cacheKey]!!
    }
    if (index == 0) {
        return 1
    }
    if (stone == 0L) {
        val res = transformStones(1, index - 1, cache)
        cache[cacheKey] = res
        return res
    } else {
        val str = stone.toString()
        val ndig = str.length
        if (ndig % 2 == 0) {
            val res = transformStones(
                str.substring(0, ndig / 2).toLong(), index - 1, cache
            ) + transformStones(
                str.substring(ndig / 2).toLong(), index - 1, cache
            )
            cache[cacheKey] = res
            return res
        } else {
            val res = transformStones(stone * 2024, index - 1, cache)
            cache[cacheKey] = res
            return res
        }
    }
}


