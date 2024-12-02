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

    val safe = reports.map { report ->
        // Ascends with at least one
        safe(report)
    }.count { it }

    println(safe)
}

private fun getSolutionB(filename: String) {
    val reports = getReports(filename)

    val safe = reports.map { report ->
        if (safe(report)) {
            return@map true
        } else {
            for (i in 0..reports.size - 1)
                if (safe(report.filterIndexed{index, _ -> index != i}))
                    return@map true
        }
        false
    }.count { it }

    println(safe)
}

private fun safe(report: List<Int>) = (report.zipWithNext { a, b -> a < b }.all { it } &&
        // Ascends with at least one
        report.zipWithNext { a, b -> a + 3 >= b }.all { it }) ||
        //Descends with at least one
        (report.zipWithNext { a, b -> a > b }.all { it } &&
                report.zipWithNext { a, b -> a <= b + 3 }.all { it })

private fun getReports(filename: String) = Utils.readLines(filename).map { line ->
    line.split(" ").map { it.toInt() }
}