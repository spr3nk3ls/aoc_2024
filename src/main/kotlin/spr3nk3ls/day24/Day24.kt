package spr3nk3ls.day24

import java.io.File

fun main() {
    getSolution("day24/example.txt")
    getSolution("day24/input.txt")
}

private fun getSolution(filename: String) {
    val (values, operations) = readAndSplitFile(filename)

    val zeroSet = operations.map { it.output }.filter { it.startsWith("z") }.toSet()

    while (!values.keys.containsAll(zeroSet)) {
        operations.forEach { it.calc(values) }
    }

    var result = ""
    zeroSet.sorted().forEach {
        if (values[it]!!) {
            result = "1" + result
        } else {
            result = "0" + result
        }
    }

    println(result.toLong(2))
}

class Operation(val input: Pair<String, String>, val output: String, val op: String)

fun Operation.calc(map: MutableMap<String, Boolean>) {
    val (one, two) = map[input.first] to map[input.second]
    val result = when (op) {
        "AND" -> one?.let { two?.and(it) }
        "OR" -> one?.let { two?.or(it) }
        "XOR" -> one?.let { two?.xor(it) }
        else -> null
    }
    if (result != null) {
        map[output] = result
    }
}

private fun readAndSplitFile(filename: String): Pair<MutableMap<String, Boolean>, List<Operation>> {
    val (v, o) = File("src/main/resources/$filename")
        .readText(Charsets.UTF_8).split("\n\n")
    val values = v.split("\n").map { it.split(": ") }.associate { it[0] to (it[1].toInt() == 1) }.toMutableMap()
    val operations = o.split("\n")
        .map { it.split(" -> ") }
        .map { (instruction, output) -> instruction.split(" ") to output }
        .map { (instruction, output) -> Operation(instruction[0] to instruction[2], output, instruction[1]) }
    return values to operations
}

