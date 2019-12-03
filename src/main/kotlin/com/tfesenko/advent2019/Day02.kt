package com.tfesenko.advent2019

import java.lang.IllegalArgumentException

fun main() {
    println(Day02(Resources.getResource("Day02.txt").readText().trim())
        .solve2())
}

class Day02(private val rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toInt() }
            .toIntArray()

    fun solve1(): Int = Program(values).run()

    fun solve2(desiredOutput: Int = 19690720): Int {
        for (noun in 0 until 99) {
            for (verb in 0 until 99) {
                val result = Program(values).run(noun, verb)
                if (result == desiredOutput) {
                    return 100 * noun + verb
                }
            }
        }
        // (49, 25)
        return -1
    }

    internal class Program(private val _memory: IntArray) {
        private val memory = _memory.copyOf()
        private var instructionPointer: Int = 0

        fun run(noun: Int = 12, verb: Int = 2): Int {
            // restore gravity
            memory[1] = noun
            memory[2] = verb

            while (executeInstruction()) { }
            return memory[0]
        }

        private fun executeInstruction(): Boolean {
            return when (memory[instructionPointer]) {
                1 -> add()
                2 -> multiply()
                99 -> halt()
                else -> throw IllegalArgumentException("Illegal command at index $instructionPointer: ${memory[instructionPointer]}")
            }
        }

        private fun halt() = false

        private fun add(): Boolean {
            val (param1, param2, target) = getBinaryParameters()
            memory[target] = memory[param1] + memory[param2]
            // println("memory[$target] = memory[$param1] + memory[$param2]")
            movePointer(4)
            return true
        }

        private fun multiply(): Boolean {
            val (param1, param2, target) = getBinaryParameters()
            memory[target] = memory[param1] * memory[param2]
            // println("memory[$target] = memory[$param1] * memory[$param2]")
            movePointer(4)
            return true
        }

        private fun movePointer(shift: Int) {
            this.instructionPointer += shift
        }

        private fun getBinaryParameters(): Triple<Int, Int, Int> {
            val param1 = memory[instructionPointer + 1]
            val param2 = memory[instructionPointer + 2]
            val target = memory[instructionPointer + 3]
            return Triple(param1, param2, target)
        }
    }

}

