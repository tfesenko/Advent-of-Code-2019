package com.tfesenko.advent2019

/**
 * Solution for Day 4: Secure Container
 * https://adventofcode.com/2019/day/4
 */

//fun main() {
//    println(Day04().solve2())
//}

class Day04(private val range: IntRange = 109165..576723) {

    private fun nonDescending(password: String) =
        password.zipWithNext().all { (a, b) -> a <= b }

    fun solve1(): Int {
        fun hasSameAdjacentDigits(password: String) = password.zipWithNext().any { (a, b) -> a == b }
        return range.map { it.toString() }.count { nonDescending(it) && hasSameAdjacentDigits(it) }
    }

    fun solve2(): Int {
        fun hasExactlyTwoSameAdjacentDigits(password: String) =
            password.groupingBy { it }.eachCount().any { it.value == 2 }
        return range.map { it.toString() }.count { nonDescending(it) && hasExactlyTwoSameAdjacentDigits(it) }
    }

}