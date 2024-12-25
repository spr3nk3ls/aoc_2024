package spr3nk3ls.day25

import java.io.File

fun main() {
    getSolution("day25/example.txt")
    getSolution("day25/input.txt")
}

private fun getSolution(filename: String) {
    val (keys, locks) = readAndSplitFile(filename)

    val result = keys.flatMap { key ->
        locks.map { lock ->
            key.zip(lock).all { it.first + it.second <= 5 }
        }
    }.count { it }

    println(result)
}

private fun readAndSplitFile(filename: String): Pair<List<List<Int>>, List<List<Int>>> {
    val keysAndLocks = File("src/main/resources/$filename")
        .readText(Charsets.UTF_8).split("\n\n")
    val k = keysAndLocks.filter { it.startsWith("#") }
    val keys = k.map { key ->
        (0..4)
            .map { x ->
                (0..6)
                    .map { y -> key.split("\n")[y][x] }.count { it == '#' } - 1
            }
    }

    val l = keysAndLocks.filter { it.startsWith(".") }

    val locks = l.map { key ->
        (0..4)
            .map { x ->
                (0..6)
                    .map { y -> key.split("\n")[y][x] }.count { it == '#' } - 1
            }
    }

    return keys to locks
}
