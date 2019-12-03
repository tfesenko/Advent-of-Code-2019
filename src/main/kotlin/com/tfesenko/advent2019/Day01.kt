package com.tfesenko.advent2019

fun main() {
    println(Day01(Resources.resourceAsLines("Day01.txt"))
        .solve2())
}

class Day01(private val rawInput: List<String>) {

    private val masses = rawInput.map { it.toInt() }

    fun solve1(): Int {
        return masses
            .map { computeFuel(it) }
            .sum()
    }

    //take its mass, divide by three, round down, and subtract 2.
    private fun computeFuel(mass: Int): Int = mass / 3 - 2


    fun solve2(): Int {
        fun allFuelForModule(mass: Int) = generateSequence(computeFuel(mass)) {
            val fuelForFuel = computeFuel(it);
            if (fuelForFuel > 0) fuelForFuel else null
        }

        return masses
            .map { allFuelForModule(it).sum() }
            .sum()
    }

}
