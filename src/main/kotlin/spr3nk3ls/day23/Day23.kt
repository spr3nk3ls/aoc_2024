package spr3nk3ls.day23

import spr3nk3ls.util.Utils

fun main() {
    getSolutionA("day23/example.txt")
    getSolutionA("day23/input.txt")
    getSolutionB("day23/example.txt")
    getSolutionB("day23/input.txt")
}

fun getSolutionA(filename: String) {
    val (computerMap, computerSet) = generateMapAndSet(filename)
    val triples = computerSet
        .flatMap { one ->
            computerSet.filter { one in computerMap[it]!! }
                .flatMap { two ->
                    inner(setOf(one, two), computerSet, computerMap)
                }
        }.toSet()

    val ts = computerSet.filter { it.startsWith("t") }
    val result = triples.count { it.any { it in ts } }

    println(result)
}

private fun inner(
    thisComputerSet: Set<String>,
    fullComputerSet: Set<String>,
    computerMap: Map<String, MutableSet<String>>,
) = fullComputerSet.filter { last ->
    thisComputerSet.all { it in computerMap[last]!! }
}.map { thisComputerSet + it }.toSet()


fun getSolutionB(filename: String) {
    val (computerMap, _) = generateMapAndSet(filename)

    val max = computerMap.map { it.value.size }.max()

    val selfContained = getSelfContained(max, computerMap)

    println(selfContained.sorted().joinToString(","))

}

private fun generateMapAndSet(filename: String): Pair<Map<String, MutableSet<String>>, Set<String>> {
    val pairs = Utils.readLines(filename)
        .map { it.split("-") }
        .map { it[0] to it[1] }
    val computerSet = (pairs.map { it.first } + pairs.map { it.second }).toSet()
    val computerMap = computerSet.associateWith { mutableSetOf<String>() }
    pairs.forEach {
        computerMap[it.first]!!.add(it.second)
        computerMap[it.second]!!.add(it.first)
    }
    return computerMap to computerSet
}

private fun getSelfContained(
    max: Int,
    computerMap: Map<String, MutableSet<String>>
): Set<String> {
    for (i in max downTo 0) {
        val selfContained = computerMap
            .filter { it.value.size >= i }
            .flatMap { (it.value + it.key).subsetsOfSize(i + 1) }
            .firstOrNull { it.isSelfContained(computerMap) }
        if (selfContained != null) {
            return selfContained
        }
    }
    return setOf()
}

private fun Set<String>.isSelfContained(computerMap: Map<String, Set<String>>): Boolean {
    return this.all { thisOne ->
        this.filter { it != thisOne }.all { thisOne in computerMap[it]!! }
    }
}

fun Set<String>.subsetsOfSize(subsetSize: Int): Set<Set<String>> {
    val result = mutableSetOf<Set<String>>()
    val list = this.toList()

    fun generateSubsetsRecursive(
        startIndex: Int,
        currentSubset: MutableSet<String>
    ) {
        if (currentSubset.size == subsetSize) {
            result.add(currentSubset.toSet())
            return
        }

        for (i in startIndex until list.size) {
            currentSubset.add(list[i])
            generateSubsetsRecursive(i + 1, currentSubset)
            currentSubset.remove(list[i])
        }
    }

    generateSubsetsRecursive(0, mutableSetOf())
    return result
}