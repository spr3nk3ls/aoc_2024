package spr3nk3ls.util

import java.io.File

object Utils {
    fun readLines(filename: String): List<String> {
        return File("src/main/resources/$filename").readLines()
    }
}