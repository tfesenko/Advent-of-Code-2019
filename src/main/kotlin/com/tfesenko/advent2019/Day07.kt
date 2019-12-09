package com.tfesenko.advent2019

import java.util.*


/**
 * Solution for Day 7: Amplification Circuit
 * https://adventofcode.com/2019/day/7
 */
fun main() {
    println(
        Day07(Resources.getResource("Day07.txt").readText().trim())
            .solve1()
    )
}

class Day07(private val rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toInt() }
            .toIntArray()

    fun solve1(): Int {
        val memoryStack: Deque<Int> = LinkedList<Int>()
        var result: Int = Int.MIN_VALUE
        val phasesPermutations = (0..4).toList().permutations()
        for (phases in phasesPermutations) {
            val amplifiers = arrayOf(
                Amplifier("A", values, memoryStack),
                Amplifier("B", values, memoryStack),
                Amplifier("C", values, memoryStack),
                Amplifier("D", values, memoryStack),
                Amplifier("E", values, memoryStack)
            )
            val inputForFirstAmplifier = 0
            memoryStack.add(inputForFirstAmplifier)
            for ((i, phase) in phases.withIndex()) {
                memoryStack.add(phase)
                amplifiers[i].run()
            }
            result = result.coerceAtLeast(memoryStack.removeLast())
        }

        return result
    }

    inner class Amplifier(val id: String, program: IntArray, private val memoryStack: Deque<Int> = LinkedList<Int>()) :
        IntcodeComputer(program) {

        override fun inputValue(): Int {
            val result = memoryStack.removeLast()
            println("$id << $result")
            return result
        }

        override fun outputValue(value: Int) {
            println("$id >> $value")
            memoryStack.addLast(value)
        }

    }
}

fun <T> List<T>.permutations(): List<List<T>> = when {
    size <= 1 -> listOf(this)
    else -> {
        val elementToInsert = this.first()
        drop(1).permutations()
            .flatMap { subpermutation ->
                (0..subpermutation.size)
                    .map { i -> subpermutation.toMutableList().apply { add(i, elementToInsert) } }
            }
    }
}
