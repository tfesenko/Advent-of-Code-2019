package com.tfesenko.advent2019

import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Solution for Day 5: Sunny with a Chance of Asteroids
 * https://adventofcode.com/2019/day/5
 */
//fun main() {
//    Day05(Resources.getResource("Day05.txt").readText().trim())
//        .solve2()
//}

class Day05(private val rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toLong() }
            .toLongArray()

    fun solve1() = IntcodeComputer(values).runAndGetFirstCell() // use 1 for input

    fun solve2() = IntcodeComputer(values).runAndGetFirstCell() // use 5 for input

}

open class IntcodeComputer(originalMemory: LongArray) {
    private val memory = originalMemory.copyOf().toMutableList()

    private var instructionPointer: Int = 0
    private var startOfCurrentInstruction: Int = instructionPointer
    private var relativeBase: Int = 0

    fun run() {
        runBlocking {
            while (executeInstruction()) {
            }
        }
    }

    suspend fun runSuspended() {
        println("Started")
        while (executeInstruction()) {
        }
    }

    fun runAndGetFirstCell(): Long {
        run()
        return memory[0]
    }

    private suspend fun executeInstruction(): Boolean {
        startOfCurrentInstruction = instructionPointer
        val instruction = nextValue()
        val code = (instruction % 100).toInt()
        // Pass parameters to commands
        // create a stream for modes
        val modes = AtomicInteger((instruction / 100).toInt())
        //println("Executing code $code in mode $modes")
        return when (code) {
            1 -> add(modes)
            2 -> multiply(modes)
            3 -> input(modes)
            4 -> output(modes)
            5 -> jumpIfTrue(modes)
            6 -> jumpIfFalse(modes)
            7 -> lessThan(modes)
            8 -> equals(modes)
            9 -> adjustRelativeBase(modes)
            99 -> halt(modes)
            else -> throw IllegalArgumentException("Illegal instruction at index $instructionPointer: $instruction")
        }
    }

    private fun add(modes: AtomicInteger): Boolean {
        val param1 = resolveParameter(nextValue(), modes.nextMode())
        val param2 = resolveParameter(nextValue(), modes.nextMode())
        val target = nextValue().toInt()
        writeTo(target, param1 + param2, modes.nextMode())
        return true
    }

    private fun multiply(modes: AtomicInteger): Boolean {
        val param1 = resolveParameter(nextValue(), modes.nextMode())
        val param2 = resolveParameter(nextValue(), modes.nextMode())
        val target = nextValue().toInt()
        writeTo(target, param1 * param2, modes.nextMode())
        return true
    }

    private suspend fun input(modes: AtomicInteger): Boolean {
        val position = nextValue().toInt()
        val input = inputValue()
        writeTo(position, input.toLong(), modes.nextMode())
        return true
    }

    protected open suspend fun inputValue(): Int {
        print("Please enter value: ")
        val input = readLine()!!.toInt()
        println(" <input: $input>")
        return input
    }

    private fun output(modes: AtomicInteger): Boolean {
        val output = resolveParameter(nextValue(), modes.nextMode())
        outputValue(output)
        return true
    }

    protected open fun outputValue(value: Long) {
        println(value)
    }

    private fun jumpIfTrue(modes: AtomicInteger): Boolean {
        val param1 = resolveParameter(nextValue(), modes.nextMode())
        val param2 = resolveParameter(nextValue(), modes.nextMode())
        if (param1 != 0L) {
            instructionPointer = param2.toInt()
        }
        return true
    }

    private fun jumpIfFalse(modes: AtomicInteger): Boolean {
        val param1 = resolveParameter(nextValue(), modes.nextMode())
        val param2 = resolveParameter(nextValue(), modes.nextMode())
        if (param1 == 0L) {
            instructionPointer = param2.toInt()
        }
        return true
    }

    private fun lessThan(modes: AtomicInteger): Boolean {
        val param1 = resolveParameter(nextValue(), modes.nextMode())
        val param2 = resolveParameter(nextValue(), modes.nextMode())
        val param3 = nextValue().toInt()
        writeTo(param3, if (param1 < param2) 1 else 0, modes.nextMode())
        return true
    }

    private fun equals(modes: AtomicInteger): Boolean {
        val param1 = resolveParameter(nextValue(), modes.nextMode())
        val param2 = resolveParameter(nextValue(), modes.nextMode())
        val param3 = nextValue().toInt()
        writeTo(param3, if (param1 == param2) 1 else 0, modes.nextMode())
        return true
    }

    private fun adjustRelativeBase(modes: AtomicInteger): Boolean {
        val param1 = resolveParameter(nextValue(), modes.nextMode()).toInt()
        relativeBase += param1
        return true
    }

    private fun halt(modes: AtomicInteger) = false

    private fun writeTo(index: Int, value: Long, mode: Int) {
        val resolvedIndex = when (mode) {
            POSITION -> index
            RELATIVE -> index + relativeBase
            else -> throw java.lang.IllegalArgumentException("Illegal mode for writing: $mode")
        }
        if (resolvedIndex >= memory.size) {
            val paddingZeros = List(resolvedIndex - memory.size + 1){0L}
            memory.addAll(paddingZeros)
        }
        if (resolvedIndex == -1) {
            println("Here")
        }
        memory[resolvedIndex] = value
        // restore pointer
        if (resolvedIndex == startOfCurrentInstruction) instructionPointer = startOfCurrentInstruction
    }

    private fun nextValue(): Long {
        val value = memory[instructionPointer]
        instructionPointer++
        return value
    }

    private fun resolveParameter(rawValue: Long, mode: Int = 0): Long {
        return when (mode) {
            POSITION -> readFromMemory(rawValue.toInt())
            RELATIVE -> readFromMemory(rawValue.toInt() + relativeBase)
            else -> rawValue // 1 - immediate mode
        }
    }

    private fun readFromMemory(index: Int): Long {
        return if (index >= memory.size) 0 else memory[index]
    }

    private fun AtomicInteger.nextMode(): Int {
        val nextMode = get() % 10
        this.set(get() / 10)
        return nextMode
    }
    companion object Modes {
        const val POSITION = 0
        const val IMMEDIATE = 1
        const val RELATIVE = 2
    }
}

