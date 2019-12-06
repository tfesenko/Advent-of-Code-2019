package com.tfesenko.advent2019

/**
 * Solution for Day 6: Universal Orbit Map
 * https://adventofcode.com/2019/day/6
 */

//fun main() {
//    println(Day06(Resources.resourceAsLines("Day06.txt")).solve2())
//}

class Day06(private val input: List<String>) {
    private val namesToObjects = mutableMapOf<String, SpaceObject>()

    init {
        input.map { it.split(')') }
            .map { Pair(getPlanetForName(it[0]), getPlanetForName(it[1])) }
            .forEach { it.second.orbitsAround = it.first }
    }

    fun solve1(): Int =
        namesToObjects.values.map { it.orbitCount }.sum()

    fun solve2(): Int {
        fun pathToRoot(planet: SpaceObject) = generateSequence(planet) { it.orbitsAround }

        val pathToRootForYOU = pathToRoot(getPlanetForName("YOU")!!.orbitsAround!!).toList()
        val pathToRootForSAN = pathToRoot(getPlanetForName("SAN")!!.orbitsAround!!).toList()

        // path to the root minus the shared segment for YOU + path to the root minus the shared segment for SAN
        return pathToRootForYOU.size + pathToRootForSAN.size - 2 * pathToRootForYOU.intersect(pathToRootForSAN).size
    }

    private fun getPlanetForName(name: String) = namesToObjects.getOrPut(name) { SpaceObject(name) }

    inner class SpaceObject(private val name: String) {
        var orbitsAround: SpaceObject? = null
        val orbitCount: Int by lazy {
            if (orbitsAround != null) orbitsAround!!.orbitCount + 1 else 0
        }
    }
}