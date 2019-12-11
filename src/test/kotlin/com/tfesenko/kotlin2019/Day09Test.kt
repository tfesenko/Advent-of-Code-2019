package com.tfesenko.kotlin2019

import com.tfesenko.advent2019.AsteroidMap
import com.tfesenko.advent2019.Day09
import org.junit.Assert
import org.junit.Test

class Day09Test {

    @Test
    fun testProducesACopyOfItself() {
        val input = """109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99"""
        Day09(input).solve1()
    }

    @Test
    fun testOutputs16DigitNumber() {
        val input =  """1102,34915192,34915192,7,4,7,99,0"""
        Day09(input).solve1()
    }

    @Test
    fun testOutputsLargeNumberInTheMiddle() {
        val input =  """104,1125899906842624,99"""
        Day09(input).solve1()
    }


}