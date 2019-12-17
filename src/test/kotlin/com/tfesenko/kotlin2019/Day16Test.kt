package com.tfesenko.kotlin2019

import com.tfesenko.advent2019.*
import org.junit.Assert.assertEquals
import org.junit.Test

class Day16Test {

    @Test
    fun testFourPhasesForSimpleInput() {
        val rawInput = "12345678"

        val afterPhase1 = Day16(rawInput).nthPhase(n=1)
        assertEquals("48226158", afterPhase1.joinToString(""))

        val afterPhase2 = Day16(rawInput).nthPhase(n=2)
        assertEquals("34040438", afterPhase2.joinToString(""))

        val afterPhase3 = Day16(rawInput).nthPhase(n=3)
        assertEquals("03415518", afterPhase3.joinToString(""))

        val afterPhase4 = Day16(rawInput).nthPhase(n=4)
        assertEquals("01029498", afterPhase4.joinToString(""))

    }

    @Test
    fun test100phasesForLargerInput() {
        "80871224585914546619083218645595".assertBecomesAfter100Phases("24176176")
        "19617804207202209144916044189917".assertBecomesAfter100Phases("73745418")
        "69317163492948606335995924319873".assertBecomesAfter100Phases("52432133")
    }

    private fun String.assertBecomesAfter100Phases(expected: String) {
        val afterPhase100 = Day16(this).nthPhase(n=100)
        assertEquals(expected, afterPhase100.subList(0, 8).joinToString(""))
    }

}