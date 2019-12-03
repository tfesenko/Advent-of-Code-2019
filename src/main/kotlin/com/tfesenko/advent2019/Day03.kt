package com.tfesenko.advent2019

import kotlin.math.abs

/**
 * Solution for https://adventofcode.com/2019/day/3
 */

//fun main() {
//    println(
//        Day03(Resources.resourceAsLines("Day03.txt"))
//            .solve2()
//    )
//}

class Day03(private val wiresAsString: List<String>) {
    private val wires: List<List<String>> = wiresAsString.map { it.split(',') }

    private fun MutableList<Pair<Int, Int>>.stepTo(step: String) {
        val direction = step[0]
        val stepCount = step.substring(1).toInt()
        when (direction) {
            'U' -> {
                repeat(stepCount) { this.add(currentPosition().stepUp()) }
            }
            'D' -> {
                repeat(stepCount) { this.add(currentPosition().stepDown()) }
            }
            'R' -> {
                repeat(stepCount) { this.add(currentPosition().stepRight()) }
            }
            'L' -> {
                repeat(stepCount) { this.add(currentPosition().stepLeft()) }
            }
        }
    }

    private fun MutableList<Pair<Int, Int>>.currentPosition() = if (this.isEmpty()) Pair(0, 0) else this.last()

    private fun Pair<Int, Int>.stepUp() = Pair(this.first, this.second + 1)
    private fun Pair<Int, Int>.stepDown() = Pair(this.first, this.second - 1)
    private fun Pair<Int, Int>.stepRight() = Pair(this.first + 1, this.second)
    private fun Pair<Int, Int>.stepLeft() = Pair(this.first - 1, this.second)

    fun solve1(): Int {
        val path1 = mutableListOf<Pair<Int, Int>>()
        wires[0].forEach { path1.stepTo(it) }
        println("Built the first path of size: ${path1.size}")

        val path2 = mutableListOf<Pair<Int, Int>>()
        wires[1].forEach { path2.stepTo(it) }
        println("Built the first path of size: ${path2.size}")

        val intersections = path2.intersect(path1)
        return intersections.map { (x, y) -> abs(x) + abs(y) }.min()!!
    }

    fun solve2(): Int {
        val path1 = mutableListOf<Pair<Int, Int>>()
        wires[0].forEach { path1.stepTo(it) }

        val path2 = mutableListOf<Pair<Int, Int>>()
        wires[1].forEach { path2.stepTo(it) }

        val intersections = path2.intersect(path1)
        println("Intersections: $intersections")
        return intersections
            .map { path1.indexOf(it) + 1 /* adding 1 as the list is indexed from 0*/ + path2.indexOf(it) + 1 }
            .min()!!
    }

}