package spr3nk3ls.day9

import spr3nk3ls.util.Utils

private val emptyFile = File(0, -1)

fun main() {
    getSolution("day9/example.txt")
    getSolution("day9/input.txt")
}

private fun getSolution(filename: String) {
    val line = Utils.readLines(filename).first()
    var unwrappedLines = unwrap(line)
    while (unwrappedLines.contains(emptyFile)) {
        val last = unwrappedLines.removeLast()
        if (last != emptyFile) {
            insertInto(unwrappedLines, last)
        }
    }
    println(unwrappedLines.indices.sumOf {
        (it * unwrappedLines[it].idNumber).toLong()
    })
}

private fun unwrap(line: String): MutableList<File> {
    return line.indices.flatMap { index ->
        val diskSpace = line[index].digitToInt()
        if (index % 2 == 0) {
            (0..<diskSpace).map { File(index / 2, diskSpace) }
        } else {
            (0..<diskSpace).map { File(0, -1) }
        }
    }.toMutableList()
}

private fun insertInto(unwrappedLines: MutableList<File>, last: File) {
    val index = unwrappedLines.indexOfFirst { it == emptyFile }
    unwrappedLines[index] = last
}

private data class File(val idNumber: Int, val diskSpace: Int)


