package com.tfesenko.advent2019

import kotlin.random.Random

/**
 * Solution for Day 13: Care Package
 * https://adventofcode.com/2019/day/13
 */
fun main() {
    Day13(Resources.getResource("Day13.txt").readText().trim())
        .solve2()
}

class Day13(private val rawInput: String) {
    private val memory =
        rawInput.split(",")
            .map { it.toLong() }
            .toLongArray()

    fun solve1() {
        val brickBreaker = BrickBreaker(memory)
        brickBreaker.run()
        println("-------")
        brickBreaker.printBoard()
        println("-------")
        println(brickBreaker.blockCount())
    }

    fun solve2() {
        memory[0] = 2
        println(playAndLearn())
    }

    private fun playAndLearn(iterationsBeforeReset: Int = 100_000): Int {
        var bestScoreSoFar = -1
        var bestInitialMovesSoFar = listOf<Int>()
        var numIterationsFromLastImprovement = 0
        while (true) {

            if (numIterationsFromLastImprovement > iterationsBeforeReset) {
                // the current steps don't work, start from scratch
                println("Resetting")
                bestScoreSoFar = -1
                numIterationsFromLastImprovement = 0
                bestInitialMovesSoFar = listOf()
            }
            numIterationsFromLastImprovement++
            if (numIterationsFromLastImprovement > 0 && numIterationsFromLastImprovement % 10_000 == 0) println("...$numIterationsFromLastImprovement")

            val brickBreaker = BrickBreaker(memory, player = BrickBreakerPlayer(bestInitialMovesSoFar.iterator()))
            brickBreaker.run()

            if (brickBreaker.maxScoreDuringGame > 0 && brickBreaker.maxScoreDuringGame >= bestScoreSoFar) {
                if (brickBreaker.maxScoreDuringGame > bestScoreSoFar) {
                    numIterationsFromLastImprovement = 0
                    println("maxScore: ${brickBreaker.maxScoreDuringGame}; blocks: ${brickBreaker.blockCount()}; moveCount: ${brickBreaker.successfulMoves().size}; moves: ${brickBreaker.successfulMoves()}")
                }
                bestScoreSoFar = brickBreaker.maxScoreDuringGame
                fun updateBestInitialMoves(): List<Int> {
                    val skipStepsNum = if (brickBreaker.successfulMoves().size < 1000) {
                        val ratioToForget = when {
                            numIterationsFromLastImprovement > 0.4 * iterationsBeforeReset -> 0.5
                            numIterationsFromLastImprovement > 0.3 * iterationsBeforeReset -> 0.4
                            numIterationsFromLastImprovement > 0.2 * iterationsBeforeReset -> 0.3
                            numIterationsFromLastImprovement > 0.1 * iterationsBeforeReset -> 0.2
                            else -> 0.2
                        }
                        if (ratioToForget == 0.0) 0 else
                            Random.nextInt((ratioToForget * brickBreaker.successfulMoves().size).toInt())
                                .coerceAtLeast(5)
                    } else when {
                        numIterationsFromLastImprovement > 0.7 * iterationsBeforeReset -> 150
                        numIterationsFromLastImprovement > 0.6 * iterationsBeforeReset -> 100
                        numIterationsFromLastImprovement > 0.5 * iterationsBeforeReset -> 50
                        numIterationsFromLastImprovement > 0.4 * iterationsBeforeReset -> 40
                        numIterationsFromLastImprovement > 0.3 * iterationsBeforeReset -> 30
                        numIterationsFromLastImprovement > 0.2 * iterationsBeforeReset -> 20
                        numIterationsFromLastImprovement > 0.1 * iterationsBeforeReset -> 10
                        else -> 5
                    }
                    return brickBreaker.successfulMoves().subList(0, brickBreaker.successfulMoves().size - skipStepsNum)
                }
                bestInitialMovesSoFar = updateBestInitialMoves()
            }

            if (brickBreaker.score > 0) {
                println("======================")
                println("WE WON!!!!")
                println("Steps: ${brickBreaker.successfulMoves()}")
                println("Score: ${brickBreaker.score}")
                return brickBreaker.score
            }
        }
    }

}

class BrickBreakerPlayer(private val initialSteps: Iterator<Int> = listOf<Int>().iterator()) {
    fun move(): Int =
        if (initialSteps.hasNext()) {
            initialSteps.next()
        } else {
            Random.nextInt(-1, 2)
        }
}


open class BrickBreaker(
    originalMemory: LongArray,
    private val player: BrickBreakerPlayer? = null
) :
    IntcodeComputer(originalMemory) {
    private val tiles: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()

    var outputBuffer = mutableListOf<Int>()
    private val moves = mutableListOf<Int>()
    var score = 0
    var maxScoreDuringGame = score
    var lastSuccessfulMove = 0


    override fun outputValue(value: Long) {
        outputBuffer.add(value.toInt())
        if (outputBuffer.size == 3) {
            val (x, y, param3) = outputBuffer
            if (x == -1 && y == 0) {
                saveAndDisplayScore(param3)
            } else {
                tiles.put(x to y, param3)
            }

            outputBuffer.clear()
        }
    }

    override suspend fun inputValue(): Int {
        val value = if (player != null) {
            player.move()
        } else {
            println()
            printBoard()
            super.inputValue()
        }
        moves.add(value)
        return value
    }

    private fun saveAndDisplayScore(value: Int) {
        score = value
        lastSuccessfulMove = moves.size
        maxScoreDuringGame = maxScoreDuringGame.coerceAtLeast(score)
    }

    fun blockCount() =
        tiles.values.filter { it == TileID.BLOCK.ordinal }.count()


    fun printBoard() {

        val maxX = tiles.keys.maxBy { it.first }!!.first
        val maxY = tiles.keys.maxBy { it.second }!!.second

        val result = List(maxY + 1) { CharArray(maxX + 1) { ' ' } }
        tiles.forEach { (x, y), tile -> result[y][x] = tile.char() }
        val resultString = result.joinToString("\n") { String(it) }
        println(resultString)
    }

    fun successfulMoves() = moves.subList(0, lastSuccessfulMove)


    fun Int.char() = TileID.values()[this].char

    enum class TileID(val char: Char) {
        EMPTY('.'), // 0
        WALL('#'),
        BLOCK('X'),
        PADDLE('_'),
        BALL('o')
    }

}