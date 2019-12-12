package com.tfesenko.advent2019

import kotlin.math.abs

/**
 * Solution for Day 12: The N-Body Problem
 * https://adventofcode.com/2019/day/12
 */
fun main() {
    Day12(Resources.resourceAsLines("Day12.txt"))
        .solve2()
}

class Day12(rawInput: List<String>) {
    private val moonSystem = MoonSystem(rawInput.map { Moon(Point3D.fromString(it), Point3D(0, 0, 0)) })

    fun solve1() {
        moonSystem.step(1000)
        println("Energy after 1000 steps: ${moonSystem.energy()}")
    }

    fun solve2(): Long {
        val initialState = moonSystem.moons.map { it.copy().position }

        val initialXs = initialState.map { it.x }
        val initialYs = initialState.map { it.y }
        val initialZs = initialState.map { it.z }

        var fullRotationX: Long? = null
        var fullRotationY: Long? = null
        var fullRotationZ: Long? = null

        moonSystem.step()
        var stepCounter = 1L
        while (initialState != moonSystem.moons.map { it.position }) {
            moonSystem.step()
            stepCounter++

            if (stepCounter == 1_000L || stepCounter == 1_000_000L || stepCounter % 10_000_000 == 0L) {
                println("Step $stepCounter...")
            }
            //
            if (initialXs == moonSystem.moons.map { it.position.x } && fullRotationX == null) {
                fullRotationX = stepCounter + 1
                println("cycle X: $fullRotationX")
            }
            if (initialYs == moonSystem.moons.map { it.position.y } && fullRotationY == null) {
                fullRotationY = stepCounter + 1
                println("cycle Y: $fullRotationY")
            }
            if (initialZs == moonSystem.moons.map { it.position.z } && fullRotationZ == null) {
                fullRotationZ = stepCounter + 1
                println("cycle Z: $fullRotationZ")
            }
            if (fullRotationX != null && fullRotationY != null && fullRotationZ != null) {
                // Coordinates X, Y, Y are changing independently from each other
                // If we find individual cycles, we can find a cycle for all three of them
                println()
                println("Detected individual cycles: X: ${fullRotationX}, Y: $fullRotationY, Z: $fullRotationZ")
                break
            }
        }
        val result = fullRotationForAll(fullRotationX!!, fullRotationY!!, fullRotationZ!!)
        println("It takes $result steps")
        return result
    }

    private fun fullRotationForAll(fullRotationX: Long, fullRotationY: Long, fullRotationZ: Long): Long {
        val rotationForXAndY = fullRotationX * fullRotationY / gcd(fullRotationX, fullRotationY)
        return rotationForXAndY * fullRotationZ / gcd(rotationForXAndY, fullRotationZ)
    }
}

class MoonSystem(internal val moons: List<Moon>) {

    fun step(numIterations: Int) {
        (1..numIterations).forEach { step() }
    }

    fun step() {
        moons.forEach { it.applyGravity(moons) }
        moons.forEach { it.applyVelocity() }
    }

    fun energy(): Int =
        moons.map { it.position.energy() * it.velocity.energy() }.sum()

}

data class Moon(var position: Point3D, var velocity: Point3D) {

    fun applyGravity(otherMoons: List<Moon>) {
        // otherMoons can contain the current moon - it will not influence the result as the coordinates are the same
        otherMoons.forEach { applyGravity(it) }
    }

    private fun applyGravity(otherMoon: Moon) {
        fun calculateGravity(ourValue: Int, theirValue: Int) = when {
            ourValue < theirValue -> 1
            ourValue > theirValue -> -1
            else -> 0
        }

        val gravity = Point3D(
            calculateGravity(position.x, otherMoon.position.x),
            calculateGravity(position.y, otherMoon.position.y),
            calculateGravity(position.z, otherMoon.position.z)
        )
        velocity += gravity
    }

    fun applyVelocity() {
        position += velocity
    }
}

data class Point3D(val x: Int, val y: Int, val z: Int) {
    companion object {
        fun fromString(str: String): Point3D {
            // Example: <x=12, y=0, z=-15>
            val coordinates = str.substring(1, str.length - 1).split(',')
                .map { it.trim().split('=') }.map { it[1].toInt() }
            return Point3D(coordinates[0], coordinates[1], coordinates[2])
        }
    }

    operator fun plus(another: Point3D) = Point3D(x + another.x, y + another.y, z + another.z)

    fun energy() = abs(x) + abs(y) + abs(z)

}