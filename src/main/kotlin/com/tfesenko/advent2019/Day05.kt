package com.tfesenko.advent2019

import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*

/**
 * Solution for Day 5: Sunny with a Chance of Asteroids
 * https://adventofcode.com/2019/day/5
 */
fun main() {
    Day05(Resources.getResource("Day05.txt").readText().trim())
        .solve2()
}

class Day05(private val rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toInt() }
            .toIntArray()

    fun solve1(): Int = IntcodeComputer(values).run() // use 1 for input

    fun solve2(): Int = IntcodeComputer(values).run() // use 5 for input

}

open class IntcodeComputer(originalMemory: IntArray) {
    private val memory = originalMemory.copyOf()

    private var instructionPointer: Int = 0
    private var startOfCurrentInstruction: Int = instructionPointer

    fun run(): Int {
        while (executeInstruction()) {
        }
        return memory[0]
    }

    private fun executeInstruction(): Boolean {
        startOfCurrentInstruction = instructionPointer
        val instruction = nextValue()
        val code = instruction % 100
        // Pass parameters to commands
        // create a stream for modes
        val modes = instruction / 100
        return when (code) {
            1 -> add(modes)
            2 -> multiply(modes)
            3 -> input(modes)
            4 -> output(modes)
            5 -> jumpIfTrue(modes)
            6 -> jumpIfFalse(modes)
            7 -> lessThan(modes)
            8 -> equals(modes)
            99 -> halt(modes)
            else -> throw IllegalArgumentException("Illegal instruction at index $instructionPointer: $instruction")
        }
    }

    private fun add(mode: Int): Boolean {
        var modes = mode
        val param1 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param2 = resolveParameter(nextValue(), modes % 10)
        val target = nextValue()
        writeTo(target, param1 + param2)
        return true
    }

    private fun multiply(mode: Int): Boolean {
        var modes = mode
        val param1 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param2 = resolveParameter(nextValue(), modes % 10)
        val target = nextValue()
        writeTo(target, param1 * param2)
        return true
    }

    private fun input(mode: Int): Boolean {
        val position = nextValue()
        val input = inputValue()
        writeTo(position, input)
        return true
    }

    protected open fun inputValue(): Int {
        print("Please enter value: ")
        val input = readLine()!!.toInt()
        println(" <input: $input>")
        return input
    }

    private fun output(mode: Int): Boolean {
        val output = resolveParameter(nextValue(), mode % 10)
        outputValue(output)
        return true
    }

    protected open fun outputValue(value: Int) {
        println(value)
    }

    private fun jumpIfTrue(mode: Int): Boolean {
        var modes = mode
        val param1 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param2 = resolveParameter(nextValue(), modes % 10)
        if (param1 != 0) {
            instructionPointer = param2
        }
        return true
    }

    private fun jumpIfFalse(mode: Int): Boolean {
        var modes = mode
        val param1 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param2 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        if (param1 == 0) {
            instructionPointer = param2
        }
        return true
    }

    private fun lessThan(mode: Int): Boolean {
        var modes = mode
        val param1 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param2 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param3 = nextValue()
        writeTo(param3, if (param1 < param2) 1 else 0)
        return true
    }

    private fun equals(mode: Int): Boolean {
        var modes = mode
        val param1 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param2 = resolveParameter(nextValue(), modes % 10)
        modes /= 10
        val param3 = nextValue()
        writeTo(param3, if (param1 == param2) 1 else 0)
        return true
    }

    private fun halt(mode: Int) = false

    private fun writeTo(index: Int, value: Int) {
        memory[index] = value
        // restore pointer
        if (index == startOfCurrentInstruction) instructionPointer = startOfCurrentInstruction
    }

    private fun nextValue(): Int {
        val value = memory[instructionPointer]
        instructionPointer++
        return value
    }

    private fun resolveParameter(rawValue: Int, mode: Int = 0): Int {
        return if (mode == 0) memory[rawValue] else rawValue
    }
}

