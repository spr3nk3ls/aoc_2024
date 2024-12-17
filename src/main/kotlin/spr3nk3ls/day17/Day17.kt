package spr3nk3ls.day17

import spr3nk3ls.day17.Day17.getSolution
import java.io.File
import java.math.BigInteger

val TWO: BigInteger = BigInteger.valueOf(2)


fun main() {
    getSolution("day17/example.txt")
    getSolution("day17/input.txt")
}

object Day17 {
    fun getSolution(filename: String) {
        val (registerValues, instructions) = readAndSplitFile(filename)
        val register = listOf('A', 'B', 'C').zip(registerValues).toMap().toMutableMap()
        val output = execute(instructions, register)
        println(output)
    }

    fun execute(
        instructions: List<Int>,
        register: MutableMap<Char, Int>
    ): String {
        var pointer = 0
        val output = mutableListOf<Int>()
        while (pointer < instructions.size - 1) {
            val ins = instructions[pointer]
            val op = instructions[pointer + 1]
            pointer = operate(ins, op, register, output, pointer)
        }
        return output.map { it.toString() }.joinToString(",")
    }

    private fun operate(
        ins: Int,
        op: Int,
        register: MutableMap<Char, Int>,
        output: MutableList<Int>,
        pointer: Int
    ): Int {
        var newPointer = pointer
        when (ins) {
            0 -> adv(op, register)
            1 -> bxl(op, register)
            2 -> bst(op, register)
            3 -> newPointer = jnz(op, register, pointer)
            4 -> bxc(register)
            5 -> out(op, register, output)
            6 -> bdv(op, register)
            7 -> cdv(op, register)
            else -> {
                println("error")
            }
        }
        if (ins != 3) {
            newPointer += 2
        }
        return newPointer
    }

    private fun adv(op: Int, register: MutableMap<Char, Int>) {
        val combo = combo(op, register)
        register['A'] = register['A']!! / TWO.pow(combo).toInt()
    }

    private fun bdv(op: Int, register: MutableMap<Char, Int>) {
        val combo = combo(op, register)
        register['B'] = register['A']!! / TWO.pow(combo).toInt()
    }

    private fun cdv(op: Int, register: MutableMap<Char, Int>) {
        val combo = combo(op, register)
        register['C'] = register['A']!! / TWO.pow(combo).toInt()
    }

    private fun bxl(op: Int, register: MutableMap<Char, Int>) {
        register['B'] = register['B']!!.xor(op)
    }

    private fun bxc(register: MutableMap<Char, Int>) {
        register['B'] = register['B']!!.xor(register['C']!!)
    }

    private fun bst(op: Int, register: MutableMap<Char, Int>) {
        val combo = combo(op, register)
        register['B'] = combo % 8
    }

    private fun jnz(op: Int, register: MutableMap<Char, Int>, pointer: Int): Int {
        if (register['A']!! == 0)
            return pointer + 2
        return op
    }

    private fun out(op: Int, register: MutableMap<Char, Int>, output: MutableList<Int>) {
        val combo = combo(op, register)
        output.add(combo % 8)
    }

    private fun combo(op: Int, register: Map<Char, Int>): Int {
        return when (op) {
            4 -> register['A']!!
            5 -> register['B']!!
            6 -> register['C']!!
            7 -> {
                println("error")
                -1
            }

            else -> op
        }
    }

    private fun readAndSplitFile(filename: String): Pair<List<Int>, List<Int>> {
        val (reg, instr) = File("src/main/resources/$filename").readText(Charsets.UTF_8).split("\n\n")
        val regs = reg.split("\n").map { it.split(": ").last().toInt() }
        val ins = instr.split(": ").last().split(",").map { it.toInt() }
        return regs to ins
    }
}
