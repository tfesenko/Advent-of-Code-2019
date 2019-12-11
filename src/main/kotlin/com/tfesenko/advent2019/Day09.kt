package com.tfesenko.advent2019

/**
 * Solution for Day 9: Sensor Boost
 * https://adventofcode.com/2019/day/9
 */

fun main() {
    Day09(Resources.getResource("Day09.txt").readText().trim())
        .solve2()
}

class Day09(private val rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toLong() }
            .toLongArray()

    fun solve1() = IntcodeComputer(values).run() // use 1 for input

    fun solve2() = IntcodeComputer(values).run() // use 2 for input

}