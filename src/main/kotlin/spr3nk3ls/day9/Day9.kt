package spr3nk3ls.day9

import spr3nk3ls.util.Utils

fun main() {
    getSolutionA("day9/example.txt")
    getSolutionA("day9/input.txt")
    getSolutionB("day9/example.txt")
    getSolutionB("day9/input.txt")
}

private fun getSolutionA(filename: String) {
    val line = Utils.readLines(filename).first()
    val files = line.indices.map { index ->
        if (index % 2 == 0) File(index / 2, line[index].digitToInt()) else File(
            -1, line[index].digitToInt()
        )
    }
    var unwrappedLines = unwrap(files)
    while (unwrappedLines.any { it.idNumber == -1 }) {
        val last = unwrappedLines.removeLast()
        if (last.idNumber != -1) {
            insertInto(unwrappedLines, last)
        }
    }
    println(unwrappedLines.indices.sumOf {
        (it * unwrappedLines[it].idNumber).toLong()
    })
}

private fun unwrap(files: List<File>): MutableList<File> {
    return files.indices.flatMap { index ->
        val diskSpace = files[index].diskSpace
        (0..<diskSpace).map { files[index] }
    }.toMutableList()
}

private fun insertInto(unwrappedLines: MutableList<File>, last: File) {
    val index = unwrappedLines.indexOfFirst { it.idNumber == -1 }
    unwrappedLines[index] = last
}

private fun getSolutionB(filename: String) {
    val line = Utils.readLines(filename).first()
    val immutableFiles = line.indices.map { index ->
        if (index % 2 == 0) File(index / 2, line[index].digitToInt()) else File(
            -1, line[index].digitToInt()
        )
    }
    var files = immutableFiles.toMutableList()
    val reversed = files.reversed().filter { it.idNumber != -1 }

    reversed.forEach {
        putInto(files, it)
    }

    val unwrapped = unwrap(files)
    println(unwrapped.indices.filter { unwrapped[it].idNumber != -1 }.sumOf {
        (it * unwrapped[it].idNumber).toLong()
    })
}

private fun putInto(files: MutableList<File>, fileToInsert: File): MutableList<File> {
    files.indices.forEach { index ->
        val file = files[index]
        val toInsert = file.idNumber == -1 && (file.diskSpace >= fileToInsert.diskSpace) && index < files.lastIndexOf(fileToInsert)
        if(toInsert) {
            val emptySpace = files[index]
            val removedIndex = files.indexOfLast { it == fileToInsert }
            files[removedIndex] = File(-1, fileToInsert.diskSpace)
            files[index] = fileToInsert
            files.add(index + 1, File(-1, emptySpace.diskSpace - fileToInsert.diskSpace))
            return files
        }
    }
    return files
}

private data class File(val idNumber: Int, val diskSpace: Int)


