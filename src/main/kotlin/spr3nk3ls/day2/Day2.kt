package spr3nk3ls.day2

import spr3nk3ls.util.Utils

fun main() {
    getSolutionA("day2/example.txt")
    getSolutionA("day2/input.txt")
    getSolutionB("day2/example.txt")
    getSolutionB("day2/input.txt")
}

private fun getSolutionA(filename: String) {
    val reports = getReports(filename)

    val safe = reports.count { report ->
        isSafe(report)
    }

    println(safe)
}

private fun getSolutionB(filename: String) {
    val reports = getReports(filename)

    val safe = reports.count { report ->
        isSafe(report) ||
                reports.indices.any { i -> isSafe(report.filterIndexed { index, _ -> index != i })}
    }

    println(safe)
}

private fun isSafe(report: List<Int>) = (report.zipWithNext { a, b -> a < b }.all { it } &&
        // Ascends with at least one
        report.zipWithNext { a, b -> a + 3 >= b }.all { it }) ||
        //Descends with at least one
        (report.zipWithNext { a, b -> a > b }.all { it } &&
                report.zipWithNext { a, b -> a <= b + 3 }.all { it })

private fun getReports(filename: String) = Utils.readLines(filename).map { line ->
    line.split(" ").map { it.toInt() }
}