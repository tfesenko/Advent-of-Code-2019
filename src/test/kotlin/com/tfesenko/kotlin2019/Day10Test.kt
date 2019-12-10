package com.tfesenko.kotlin2019

import com.tfesenko.advent2019.AsteroidMap
import com.tfesenko.advent2019.Day10
import com.tfesenko.advent2019.Resources
import org.junit.Assert.assertEquals
import org.junit.Test

class Day10Test {

    @Test
    fun testFirstNineAsteroidsToDestroy() {
        val input =
            """
                .#....#####...#..
                ##...##.#####..##
                ##...#...#.#####.
                ..#.....X...###..
                ..#.#.....#....##
            """.trimIndent().split("\n")
        val asteroidMap = AsteroidMap(input, 3, 8)
        val asteroidsToDestroy = asteroidMap.sortedAsteroidsToDestroy()

        val expected = """
            .#....###24...#..
            ##...##.13#67..9#
            ##...#...5.8####.
            ..#.....X...###..
            ..#.#.....#....##
        """.trimIndent()
        assertEquals(Pair(1, 8), asteroidsToDestroy[0])
        assertEquals(Pair(0, 9), asteroidsToDestroy[1])
        assertEquals(Pair(1, 9), asteroidsToDestroy[2])
    }

}