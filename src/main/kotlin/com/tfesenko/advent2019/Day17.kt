package com.tfesenko.advent2019

/**
 * Solution for Day 17: Set and Forget
 * https://adventofcode.com/2019/day/17
 */
fun main() {
    Day17(Resources.getResource("Day17.txt").readText().trim())
        .solve1()
}

class Day17(val rawInput: String) {
    private val intcodeInput =
        rawInput.split(",")
            .map { it.toLong() }
            .toLongArray()

    fun solve1() {
        val cameraView = AftScaffoldingControlAndInformationInterface.getCameraView(intcodeInput)
        val ascii = AftScaffoldingControlAndInformationInterface(cameraView)
        val result = ascii.getAlignmentParameters().sum()
        println(result)
    }

}


class AftScaffoldingControlAndInformationInterface(val map: List<String>) {
    companion object {
        fun getCameraView(programInput: LongArray): List<String> {
            val cameraView = mutableListOf<Char>()
            val intcodeComputer = object : IntcodeComputer(programInput) {
                override fun outputValue(value: Long) {
                    val intValue = value.toInt()
                    val asciiValue = intValue.toChar()
                    cameraView.add(asciiValue)
                }
            }
            intcodeComputer.run()
            println(String(cameraView.toCharArray()))
            return String(cameraView.toCharArray()).trim().split("\n")
        }
    }

    fun findIntersections(): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        fun isIntersection(x: Int, y: Int): Boolean {
            val scaffold = '#'
            return map[y][x] == scaffold
                    && (y == 0 || map[y - 1][x] == scaffold) && (y == map.size - 1 || map[y + 1][x] == scaffold)
                    && (x == 0 || map[y][x - 1] == scaffold) && (x == map[0].length - 1 || map[y][x + 1] == scaffold)
        }
        for (y in map.indices) {
            for (x in map[0].indices) {
                if (isIntersection(x, y)) result.add(x to y)
            }
        }
        return result
    }

    fun getAlignmentParameters() = findIntersections().map { it.toAlignmentParameter() }

    private fun Pair<Int, Int>.toAlignmentParameter() = this.first * this.second
}