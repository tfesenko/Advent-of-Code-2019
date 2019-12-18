package com.tfesenko.kotlin2019

import com.tfesenko.advent2019.AftScaffoldingControlAndInformationInterface
import com.tfesenko.advent2019.Day16
import org.junit.Assert
import org.junit.Test

class Day17Test {

    @Test
    fun testAlignmentParameters() {
        val rawInput = """
            ..#..........
            ..#..........
            #######...###
            #.#...#...#.#
            #############
            ..#...#...#..
            ..#####...^..
        """.trimIndent()

        val lines = rawInput.split("\n")
        val ascii = AftScaffoldingControlAndInformationInterface(lines)

        val intersections = ascii.findIntersections()
        Assert.assertEquals(4, intersections.size)
        Assert.assertEquals(76, ascii.getAlignmentParameters().sum())
    }

}