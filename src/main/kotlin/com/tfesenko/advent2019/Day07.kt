package com.tfesenko.advent2019

import kotlinx.coroutines.*
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel


/**
 * Solution for Day 7: Amplification Circuit
 * https://adventofcode.com/2019/day/7
 */
fun main() {
    // 3321777
    println(
        Day07(Resources.getResource("Day07.txt").readText().trim())
            .solve2()
    )
}

class Day07(private val rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toLong() }
            .toLongArray()

    fun solve2() = runBlocking<Long> {
        var result: Long = Long.MIN_VALUE
        val allPossiblePhaseSettings = (5L..9).toList().permutations()
        for (phaseSettings in allPossiblePhaseSettings) {
            result = result.coerceAtLeast(runAmplifiers(phaseSettings))
        }

        result
    }

    private suspend fun CoroutineScope.runAmplifiers(phaseSettings: List<Long>) = runBlocking<Long> {
        val amplifierCount = 5
        val channels = List(amplifierCount) { Channel<Long>(10) }
        val amplifiers = List(amplifierCount) { i ->
            Amplifier(
                "" + ('A' + 1),
                values,
                channels[i],
                channels[(i + 1) % amplifierCount]
            )
        }

        (0 until amplifierCount).forEach { i -> channels[i].send(phaseSettings[i]) }
        val inputForFirstAmplifier = 0L
        channels[0].send(inputForFirstAmplifier)

        awaitAll(
            async { amplifiers[0].run() },
            async { amplifiers[1].run() },
            async { amplifiers[2].run() },
            async { amplifiers[3].run() },
            async { amplifiers[4].run() }
        )
        channels[0].receive()
    }


    inner class Amplifier(
        private val id: String,
        program: LongArray,
        private val inChannel: Channel<Long>,
        private val outChannel: Channel<Long>
    ) :
        IntcodeComputer(program) {

        override suspend fun inputValue(): Int {
            //println("$id ...   ")
            val result = inChannel.receive()
            //println("$id << $result")
            return result.toInt()
        }

        override fun outputValue(value: Long) {
            //println("$id >> $value")
            outChannel.offer(value)
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
