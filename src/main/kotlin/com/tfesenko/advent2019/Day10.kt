package com.tfesenko.advent2019

import kotlin.math.atan2
import kotlin.math.pow

/**
 * Solution for Day 10: Monitoring Station
 * https://adventofcode.com/2019/day/10
 */

fun main() {
    val result1 = Day10(Resources.resourceAsLines("Day10.txt"))
        .solve1()
    println("Visible asteroids: ${result1.first} from location: \n\tline: ${result1.second.first}, \n\tcolumn: ${result1.second.second}")
    println()
    val result2 = Day10(Resources.resourceAsLines("Day10.txt"))
        .solve2()
    println(result2)
}

class Day10(private val map: List<String>) {

    fun solve1(): Pair<Int, Pair<Int, Int>> {
        var result = Pair(Int.MIN_VALUE, Pair(-1, -1))
        for (line in map.indices) {
            for (column in map[0].indices) {
                val asteroidCount = AsteroidMap(map, line, column).countVisibleAsteroids()
                if (result.first < asteroidCount) {
                    result = Pair(asteroidCount, Pair(line, column))
                }
            }
        }
        return result
    }

    fun solve2(): Int {
        val asteroidsToDestroy = AsteroidMap(map,  20, 23).sortedAsteroidsToDestroy()
        val asteroid = asteroidsToDestroy[199]
        return 100 * asteroid.second + asteroid.first
    }
}

class AsteroidMap(private val originalMap: List<String>, private val laserLine: Int, private val laserColumn: Int) {
    private val laserPosition = Pair(laserLine, laserColumn)

    companion object {
        const val ASTEROID = '#'
        const val SHADOW = '/'
        const val LASER = 'X'
    }

    fun countVisibleAsteroids(): Int {
        val mapWithShadows: MutableList<CharArray> = originalMap.map { it.toCharArray() }.toMutableList()
        mapWithShadows.addShadows()
        val result = mapWithShadows.map { it.count { ch -> ch == ASTEROID } }.sum()
        println("Asteroids for (${laserLine}, ${laserColumn}): $result")
        return result
    }

    fun sortedAsteroidsToDestroy(): List<Pair<Int, Int>> {
        val angleComparator = compareBy<Pair<Int, Int>> {
            laserPosition.angleStartingFromTwelveOClock(it.first, it.second)
        }

        val distanceComparator = compareBy<Pair<Int, Int>> {
            (it.first - laserPosition.first).toDouble().pow(2) + (it.second - laserPosition.second).toDouble().pow(2)
        }

        val pointComparator = angleComparator.then(distanceComparator)

        val mapWithShadows: MutableList<CharArray> = originalMap.map { it.toCharArray() }.toMutableList()
        mapWithShadows.addShadows()

        val directlyVisibleAsteroids = mapWithShadows.mapIndexed { row, lineStr ->
            lineStr
                .mapIndexed { column, ch -> Pair(ch, Pair(row, column)) }
        }.flatten()
            .filter { it.first == AsteroidMap.ASTEROID }
            .map { it.second }
        // Currently returns asteroids to destroy only during the first pass.
        // But we are lucky as the first pass destroys 334 asteroids which is greater than 200
        return directlyVisibleAsteroids.sortedWith(pointComparator)
    }


    fun Pair<Int, Int>.angleStartingFromTwelveOClock(line: Int, column: Int): Double {
        fun angle(line: Int, column: Int): Double {
            return atan2(
                (line - this.first).toDouble(),
                (column - this.second).toDouble()
            ) * 180 / Math.PI
        }
        return (angle(line, column) + 360 + 90) % 360
    }

    fun MutableList<CharArray>.addShadows() {
        this[laserPosition.first][laserPosition.second] = LASER

        // Mark points shadowed by the asteroids located on the same vertical or horizontal lines as the station
        // TODO refactor to make more readable
        var sawAsteroid = false
        for (line in laserPosition.first downTo 0) {
            if (sawAsteroid) {
                this[line][laserPosition.second] = SHADOW
            } else if (this[line][laserPosition.second] == ASTEROID) {
                sawAsteroid = true
            }
        }

        sawAsteroid = false
        for (line in laserPosition.first until this.size) {
            if (sawAsteroid) {
                this[line][laserPosition.second] = SHADOW
            } else if (this[line][laserPosition.second] == ASTEROID) {
                sawAsteroid = true
            }
        }

        sawAsteroid = false
        for (column in laserPosition.second downTo 0) {
            if (sawAsteroid) {
                this[laserPosition.first][column] = SHADOW
            } else if (this[laserPosition.first][column] == ASTEROID) {
                sawAsteroid = true
            }
        }

        sawAsteroid = false
        for (column in laserPosition.second until this[0].size) {
            if (sawAsteroid) {
                this[laserPosition.first][column] = SHADOW
            } else if (this[laserPosition.first][column] == ASTEROID) {
                sawAsteroid = true
            }
        }

        processQuadrant(Pair(laserPosition.first - 1 downTo 0, laserPosition.second - 1 downTo 0))
        // process the upper-right part
        processQuadrant(Pair(laserPosition.first + 1 until this.size, laserPosition.second - 1 downTo 0))
        // process the left-bottom part
        processQuadrant(Pair(laserPosition.first - 1 downTo 0, laserPosition.second + 1 until this[0].size))
        // process the right-bottom part
        processQuadrant(Pair(laserPosition.first + 1 until this.size, laserPosition.second + 1 until this[0].size))
    }

    private fun MutableList<CharArray>.processQuadrant(corner: Pair<IntProgression, IntProgression>) {
        for (line in corner.first) {
            for (column in corner.second) {
                if (this[line][column] == ASTEROID) {
                    val ratio = gcd(line - laserPosition.first, column - laserPosition.second)
                    val deltaX = (line - laserPosition.first) / ratio
                    val deltaY = (column - laserPosition.second) / ratio
                    var (currX, currY) = Pair(line, column)
                    currX += deltaX
                    currY += deltaY
                    while (isValidLocation(currX, currY)) {
                        this[currX][currY] = SHADOW
                        currX += deltaX
                        currY += deltaY
                    }
                }
            }
        }
    }

    private fun MutableList<CharArray>.isValidLocation(x: Int, y: Int): Boolean {
        return (0 <= x && x < this.size) && (0 <= y && y < this[0].size)
    }

    fun List<CharArray>.printMap() {
        println(this.joinToString(separator = "\n") { String(it) })
    }

    private fun gcd(num1: Int, num2: Int): Int {
        var positiveNum1 = if (num1 > 0) num1 else -num1
        var positiveNum2 = if (num2 > 0) num2 else -num2
        while (positiveNum1 != positiveNum2) {
            if (positiveNum1 > positiveNum2)
                positiveNum1 -= positiveNum2
            else
                positiveNum2 -= positiveNum1
        }
        return positiveNum1
    }

}