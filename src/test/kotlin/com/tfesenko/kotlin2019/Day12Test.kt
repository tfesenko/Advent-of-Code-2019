package com.tfesenko.kotlin2019

import com.tfesenko.advent2019.Day12
import com.tfesenko.advent2019.Moon
import com.tfesenko.advent2019.MoonSystem
import com.tfesenko.advent2019.Point3D
import org.junit.Assert.assertEquals
import org.junit.Test

class Day12Test {

    @Test
    fun testPoint3DFromString() {
        val input = "<x=-1, y=0, z=2>"
        assertEquals(Point3D(-1, 0, 2), Point3D.fromString(input))
    }

    @Test
    fun testPositionsAndVelocityAfter10Steps() {
        val rawInput =
            """
                <x=-1, y=0, z=2>
                <x=2, y=-10, z=-7>
                <x=4, y=-8, z=8>
                <x=3, y=5, z=-1>
            """.trimIndent().split("\n")
        val moonSystem = MoonSystem(rawInput.map { Moon(Point3D.fromString(it), Point3D(0, 0, 0)) })
        moonSystem.step(10)
        // Expected:
//        pos=<x= 2, y= 1, z=-3>, vel=<x=-3, y=-2, z= 1>
//        pos=<x= 1, y=-8, z= 0>, vel=<x=-1, y= 1, z= 3>
//        pos=<x= 3, y=-6, z= 1>, vel=<x= 3, y= 2, z=-3>
//        pos=<x= 2, y= 0, z= 4>, vel=<x= 1, y=-1, z=-1>
        assertEquals(Point3D(2, 1, -3), moonSystem.moons[0].position)
        assertEquals(Point3D(1, -8, 0), moonSystem.moons[1].position)
        assertEquals(Point3D(3, -6, 1), moonSystem.moons[2].position)
        assertEquals(Point3D(2, 0, 4), moonSystem.moons[3].position)

        assertEquals(Point3D(-3, -2, 1), moonSystem.moons[0].velocity)
        assertEquals(Point3D(-1, 1, 3), moonSystem.moons[1].velocity)
        assertEquals(Point3D(3, 2, -3), moonSystem.moons[2].velocity)
        assertEquals(Point3D(1, -1, -1), moonSystem.moons[3].velocity)
    }

    @Test
    fun testEnergyAfter100Steps() {
        val rawInput =
            """
                <x=-8, y=-10, z=0>
                <x=5, y=5, z=10>
                <x=2, y=-7, z=3>
                <x=9, y=-8, z=-3>
            """.trimIndent().split("\n")
        val moonSystem = MoonSystem(rawInput.map { Moon(Point3D.fromString(it), Point3D(0, 0, 0)) })
        moonSystem.step(100)
        assertEquals(1940, moonSystem.energy())
    }

    @Test
    fun testStepsToReturnToInitialState() {
        val rawInput =
            """
                <x=-8, y=-10, z=0>
                <x=5, y=5, z=10>
                <x=2, y=-7, z=3>
                <x=9, y=-8, z=-3>
            """.trimIndent().split("\n")

        assertEquals(4_686_774_924, Day12(rawInput).solve2())
    }

}