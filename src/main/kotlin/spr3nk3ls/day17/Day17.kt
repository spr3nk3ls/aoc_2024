package spr3nk3ls.day17

import spr3nk3ls.day17.Day17.execute
import java.io.File
import java.math.BigInteger.*

fun main() {
    getSolutionA("day17/example1.txt")
    getSolutionA("day17/input.txt")
//    getSolutionB("day17/example2.txt")
    // 110270259759514 too high
    getSolutionB("day17/input.txt")
}

fun getSolutionA(filename: String) {
    val (registerValues, instructions) = readAndSplitFile(filename)
    val register = listOf('A', 'B', 'C').zip(registerValues).toMap().toMutableMap()
    val output = execute(instructions, register)
    println(output.joinToString(",") { it.toString() })
}

fun getSolutionB(filename: String) {
    val (_, instructions) = readAndSplitFile(filename)
    var result = 0L
    val register = mutableMapOf('A' to 0L, 'B' to 0L, 'C' to 0L)
    val initial =
        (valueOf(8L).pow(15).times(valueOf(3)))
            .add((valueOf(8L).pow(12)))
            .add((valueOf(8L).pow(11)).times(valueOf(8)))
            .add((valueOf(8L).pow(10)).times(valueOf(2)))
    for(i in generateSequence(initial){ it.add(ONE) }) {
        register['A'] = i.toLong()
        register['B'] = 0
        register['C'] = 0
        val output = execute(instructions, register)
        if (instructions == output){
            result = i.toLong()
            break
        }
        println(output)
    }
    println(result)
}

private fun readAndSplitFile(filename: String): Pair<List<Long>, List<Long>> {
    val (reg, instr) = File("src/main/resources/$filename").readText(Charsets.UTF_8).split("\n\n")
    val regs = reg.split("\n").map { it.split(": ").last().toLong() }
    val ins = instr.split(": ").last().split(",").map { it.toLong() }
    return regs to ins
}

object Day17 {
    fun execute(
        instructions: List<Long>, register: MutableMap<Char, Long>
    ): List<Long> {
        var pointer = 0
        var output = listOf<Long>()
        while (pointer < instructions.size - 1) {
            val ins = instructions[pointer]
            val op = instructions[pointer + 1]
            val (point, out) = operate(ins, op, register, output, pointer)
            pointer = point
            output = out
        }
        return output.toList()
    }

    private fun operate(
        ins: Long, op: Long, register: MutableMap<Char, Long>, output: List<Long>, pointer: Int
    ): Pair<Int, List<Long>> {
        var newPointer = pointer
        var newOutput = output
        when (ins) {
            0L -> adv(op, register)
            1L -> bxl(op, register)
            2L -> bst(op, register)
            3L -> newPointer = jnz(op, register, pointer)
            4L -> bxc(register)
            5L -> newOutput = out(op, register, output)
            6L -> bdv(op, register)
            7L -> cdv(op, register)
            else -> {
                println("error")
            }
        }
        if (ins != 3L) {
            newPointer += 2
        }
        return newPointer to newOutput
    }

    private fun adv(op: Long, register: MutableMap<Char, Long>) {
        val combo = combo(op, register).toInt()
        register['A'] = register['A']!! / TWO.pow(combo).toLong()
    }

    private fun bdv(op: Long, register: MutableMap<Char, Long>) {
        val combo = combo(op, register).toInt()
        register['B'] = register['A']!! / TWO.pow(combo).toLong()
    }

    private fun cdv(op: Long, register: MutableMap<Char, Long>) {
        val combo = combo(op, register).toInt()
        register['C'] = register['A']!! / TWO.pow(combo).toLong()
    }

    private fun bxl(op: Long, register: MutableMap<Char, Long>) {
        register['B'] = register['B']!!.xor(op)
    }

    private fun bxc(register: MutableMap<Char, Long>) {
        register['B'] = register['B']!!.xor(register['C']!!)
    }

    private fun bst(op: Long, register: MutableMap<Char, Long>) {
        val combo = combo(op, register)
        register['B'] = combo % 8
    }

    private fun jnz(op: Long, register: MutableMap<Char, Long>, pointer: Int): Int {
        if (register['A']!! == 0L) return pointer + 2
        return op.toInt()
    }

    private fun out(op: Long, register: MutableMap<Char, Long>, output: List<Long>): List<Long> {
        val combo = combo(op, register)
        return output + (combo % 8L)
    }

    private fun combo(op: Long, register: Map<Char, Long>): Long {
        return when (op) {
            4L -> register['A']!!
            5L -> register['B']!!
            6L -> register['C']!!
            7L -> {
                println("error")
                -1L
            }

            else -> op
        }
    }
}
