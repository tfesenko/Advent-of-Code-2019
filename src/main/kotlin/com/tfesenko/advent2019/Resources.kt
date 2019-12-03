package com.tfesenko.advent2019

import java.io.File
import java.net.URL

internal object Resources {

    fun getResource(fileName: String): URL =
        Resources.javaClass.classLoader.getResource(fileName)

    fun resourceAsLines(fileName: String): List<String> =
        File(getResource(fileName).toURI()).readLines()
}