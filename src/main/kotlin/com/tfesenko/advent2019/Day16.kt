package com.tfesenko.advent2019

import kotlin.math.abs

fun main() {
    Day16(Resources.getResource("Day16.txt").readText().trim())
        .solve2()
}

class Day16(val rawInput: String) {
    private val inputSignal = rawInput.toCharArray().map { (it - '0') }

    fun solve1() {
        val afterPhase100 = nthPhase(n = 100)
        val result = afterPhase100.subList(0, 8).joinToString("")
        println(result)
    }

    fun solve2() {
        // size: 6500000
        // off   5976963
        val inputSignalRepeated10000Times =  sequenceOf(*inputSignal.toTypedArray()).repeat().take(10_000*inputSignal.size).toList()
        val messageOffset = rawInput.substring(0, 7).toInt()
        println("offset $messageOffset")
        println("inputSignal " + inputSignal.size)
        println("inputSignalRepeated10000Times.size " + inputSignalRepeated10000Times.size)
        val afterPhase100 = nthPhase(inputSignalRepeated10000Times, n = 100)
        val result = afterPhase100.subList(messageOffset, messageOffset + 8).joinToString("")
        println(result)
    }

    fun nthPhase(input: List<Int> = inputSignal, n: Int): List<Int> {
        var nextPhase = input
        repeat(n) {
            println("Starting $it-th phase")
            nextPhase = nextPhase(nextPhase)
        }
        return nextPhase
    }

    private fun nextPhase(currPhase: List<Int>): List<Int> {
        val basePattern = listOf(0, 1, 0, -1)
        fun patternFor(outputIndex: Int) =
            List(currPhase.size) { i -> basePattern[(i + 1) / (outputIndex + 1) % basePattern.size] }

        return List(currPhase.size) { i ->
            abs(currPhase.zip(patternFor(i)).map { (a, b) -> (a * b) % 10 }.sum() % 10)
        }
    }

    fun <T> Sequence<T>.repeat() : Sequence<T> = sequence {
        while(true) yieldAll(this@repeat)
    }
}