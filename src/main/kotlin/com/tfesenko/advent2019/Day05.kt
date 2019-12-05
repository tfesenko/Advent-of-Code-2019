package com.tfesenko.advent2019

import java.lang.IllegalArgumentException

fun main() {
    Day05(Resources.getResource("Day05.txt").readText().trim())
        .solve1()
}

class Day05(private val rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toInt() }
            .toIntArray()

    fun solve1(): Int = Program(values).run()


    internal class Program(private val originalMemory: IntArray) {
        private val memory = originalMemory.copyOf()
        private var instructionPointer: Int = 0

        fun run(): Int {
            while (executeInstruction()) { }
            return memory[0]
        }

        private fun executeInstruction(): Boolean {
            val instruction = nextValue()
            val code = instruction % 100
            val modes = instruction / 100
            return when (code) {
                1 -> add(modes)
                2 -> multiply(modes)
                3 -> input(modes)
                4 -> output(modes)
                99 -> halt(modes)
                else -> throw IllegalArgumentException("Illegal command at index $instructionPointer: ${memory[instructionPointer]}")
            }
        }

        private fun halt(mode: Int) = false

        private fun input(mode: Int): Boolean {
            val position = nextValue()
            print("Please enter value: ")
            val input = readLine()?.toInt()!!
            memory[position] = input
            return true
        }

        private fun output(mode: Int): Boolean {
            val output = resolveParameter(nextValue(), mode % 10)
            println(output)
            return true
        }

        private fun add(mode: Int): Boolean {
            var modes = mode
            val param1 = resolveParameter(nextValue(), modes%10)
            modes /= 10
            val param2 = resolveParameter(nextValue(), modes%10)
            modes /= 10
            val target = nextValue()
            memory[target] = param1 + param2
            return true
        }

        private fun multiply(mode: Int): Boolean {
            var modes = mode
            val param1 = resolveParameter(nextValue(), modes%10)
            modes /= 10
            val param2 = resolveParameter(nextValue(), modes%10)
            modes /= 10
            val target = nextValue()
            memory[target] = param1 * param2
            return true
        }

        private fun nextValue(): Int {
            val value  = memory[instructionPointer]
            instructionPointer++
            return value
        }

        private fun resolveParameter(rawValue: Int, mode: Int = 0):Int {
            return if (mode == 0) memory[rawValue] else rawValue
        }
    }

}

