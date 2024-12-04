package spr3nk3ls.day4

import spr3nk3ls.util.Utils

fun main() {
    getSolutionA("day4/example.txt")
    getSolutionA("day4/input.txt")
    getSolutionB("day4/example.txt")
    getSolutionB("day4/input.txt")
}


private fun getSolutionA(filename: String) {
    val rows = Utils.readLines(filename)
    val rowCount = rows.sumOf { countXmas(it) }
    val colCount = getCols(rows).sumOf { countXmas(it) }
    val diag1Count = getDiag1(rows).sumOf { countXmas(it) }
    val diag2Count = getDiag2(rows).sumOf { countXmas(it) }
    val diag3Count = getDiag3(rows).sumOf { countXmas(it) }
    val diag4Count = getDiag4(rows).sumOf { countXmas(it) }
    println(rowCount + colCount + diag1Count + diag2Count + diag3Count + diag4Count)
}

private fun countXmas(line: String): Int {
    return line.windowed(4).count { "XMAS".equals(it) || "SAMX".equals(it) }
}

private fun getCols(lines: List<String>): List<String> {
    return lines.indices.map { i -> lines.map { line -> line[i] }.joinToString("") }
}

private fun getDiag1(lines: List<String>): List<String> {
    val max = lines.size - 1
    return (0..max).map { n -> (0..n).map { i -> lines[i][n - i] }.joinToString("") }
}

private fun getDiag2(lines: List<String>): List<String> {
    val max = lines.size - 1
    return (0..max - 1).map { n -> (0..n).map { i -> lines[max - i][max - n + i] }.joinToString("").reversed() }
}

private fun getDiag3(lines: List<String>): List<String> {
    val max = lines.size - 1
    return (0..max).map { n -> (0..n).map { i -> lines[max - i][n - i] }.joinToString("") }
}

private fun getDiag4(lines: List<String>): List<String> {
    val max = lines.size - 1
    return (0..max - 1).map { n -> (0..n).map { i -> lines[i][max - n + i] }.joinToString("").reversed() }
}

private fun getSolutionB(filename: String) {
    val rows = Utils.readLines(filename)
    val windows = rows.map { row -> row.windowed(3) }
    val range = 0..windows.size - 3
    val squares = range.flatMap { j ->
        range.map { i ->
            windows[i][j] + windows[i + 1][j] + windows[i + 2][j]
        }
    }

    val regexPatterns = listOf(
        Regex("M.M.A.S.S"),
        Regex("M.S.A.M.S"),
        Regex("S.M.A.S.M"),
        Regex("S.S.A.M.M")
    )

    val count = squares.count {
        regexPatterns.any { pattern -> pattern.matches(it) }
    }
    println(count)
}

