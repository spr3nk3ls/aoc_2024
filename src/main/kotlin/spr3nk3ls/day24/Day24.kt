package spr3nk3ls.day24

import java.io.File

fun main() {
    getSolutionA("day24/example1.txt")
    getSolutionA("day24/input.txt")
    getSolutionB("day24/input_corrected.txt")
}

private fun getSolutionA(filename: String) {
    val (values, operations) = readAndSplitFile(filename)

    val zSet = operations.map { it.output }.filter { it.startsWith("z") }.toSet()

    while (!values.keys.containsAll(zSet)) {
        operations.forEach { it.calc(values) }
    }

    var result = ""
    zSet.sorted().forEach {
        if (values[it]!!) {
            result = "1" + result
        } else {
            result = "0" + result
        }
    }

    println(result.toLong(2))
}

private fun getSolutionB(filename: String) {
    val (_, operations) = readAndSplitFile(filename)

    val opMap = operations.associate { it.output to it }
    val orderedZ = operations.filter { it.output.startsWith("z") }.sortedBy { it.output }.reversed()
    val lastZ = orderedZ.first()

    assert(lastZ.op == "OR")

    var connectors = lastZ.input
    orderedZ.drop(1).forEach { z ->
        val ands = connectors.map { opMap[it]!! }
        val (xyand, otherand) =
            if (ands[0].input[0].startsWith("x") || ands[0].input[1].startsWith("x"))
                ands[0] to ands[1]
            else ands[1] to ands[0]

        assert(xyand.op == "AND")
        assert(otherand.op == "AND")

        if (z.output != "z00") {
            assert(otherand.input.toSet() == z.input.toSet())

            val ors = z.input.map { opMap[it]!! }
            val (xyxor, or) =
                if (ors[0].input[0].startsWith("x") || ors[0].input[1].startsWith("x"))
                    ors[0] to ors[1]
                else ors[1] to ors[0]

            if (z.output != "z01") {
                assert(xyxor.op == "XOR")
                assert(xyand.input.toSet() == xyxor.input.toSet())
                assert(or.op == "OR")
                assert(setOf(xyxor.output, or.output) == otherand.input.toSet())

                connectors = or.input
            }
        }
    }

    println(listOf("z39", "pfw", "vgs", "dtk", "z21", "shh", "dqr", "z33").sorted().joinToString(","))
}

// y00 XOR x00 -> z00
// x00 AND y00 -> mcg
// mcg XOR vcn -> z01
// mcg AND vcn -> ndb
// mcg XOR vcn -> z01

// kbm OR spc -> z45

// x44 AND y44 -> spc //xyand
// hhp AND hhg -> kbm //otherand
// hhg XOR hhp -> z44 //z
// y44 XOR x44 -> hhg //xyxor
// bns OR gvv -> hhp  //or

// y43 AND x43 -> gvv
// qtr AND tfq -> bns
// qtr XOR tfq -> z43
// x43 XOR y43 -> qtr
// sjr OR jrk -> tfq

// y42 AND x42 -> jrk
// vvg AND pvj -> sjr
// pvj XOR vvg -> z42
// x42 XOR y42 -> vvg
// wsw OR dqf -> pvj

// y41 AND x41 -> wsw
// pgd AND wjd -> dqf
// pgd XOR wjd -> z41
// y41 XOR x41 -> wjd
// tcf OR cpp -> pgd

// y40 AND x40 -> tcf
// jqk AND vnc -> cpp
// jqk XOR vnc -> z40
// y40 XOR x40 -> vnc
// pfw OR kdd -> jqk

// x39 AND y39 -> z39 WRONG
// gqn AND sjq -> kdd
// x39 XOR y39 -> gqn
// gqn XOR sjq -> pfw

private fun initialize(list: List<String>, number: Long): Map<String, Boolean> {
    val binary = number.toString(2).toCharArray().map { it == '1' }.reversed()
    return list.zip(binary).toMap()
}

private data class Operation(val input: List<String>, val output: String, val op: String)

private fun Operation.calc(map: MutableMap<String, Boolean>) {
    val (one, two) = map[input[0]] to map[input[1]]
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
        .map { (instruction, output) -> Operation(listOf(instruction[0], instruction[2]), output, instruction[1]) }
    return values to operations
}
