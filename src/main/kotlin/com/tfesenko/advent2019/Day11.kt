package com.tfesenko.advent2019

import java.lang.IllegalArgumentException

/**
 * Solution for Day 11: Space Police
 * https://adventofcode.com/2019/day/11
 */
//fun main() {
//    Day11(Resources.getResource("Day11.txt").readText().trim())
//        .solve2()
//}

class Day11(rawInput: String) {
    private val values =
        rawInput.split(",")
            .map { it.toLong() }
            .toLongArray()

    fun solve1(): Int {
        val hullPaintingRobot = HullPaintingRobot(values)
        hullPaintingRobot.run()
        return hullPaintingRobot.board.paintedPanels.size
    }

    fun solve2() {
        val hullPaintingRobot = HullPaintingRobot(values)
        hullPaintingRobot.run()
        println(hullPaintingRobot.board.asPrintableString())
    }
}

class HullPaintingRobot(
    program: LongArray,
    internal val board: Board = Board()
) :
    IntcodeComputer(program) {

    private var inPaintMode = true

    override suspend fun inputValue(): Int {
        val result = board.colorOfCurrentPanel()
        return result
    }

    override fun outputValue(value: Long) {
        val intValue = value.toInt()
        if (inPaintMode) paint(intValue) else turnAndMove(intValue)
        inPaintMode = !inPaintMode
    }

    private fun paint(paintColor: Int) {
        board.paint(paintColor)
    }

    private fun turnAndMove(direction: Int) {
        when (direction) {
            0 -> board.turnLeft()
            1 -> board.turnRight()
            else -> throw IllegalArgumentException("Unsupported direction: $direction")
        }
        board.step()
    }

}

class Board(initialPosition: Point = Point(0, 0), initialDirection: Direction = Direction.UP) {
    private var currentPosition: Point = initialPosition
    private var currentDirection = initialDirection
    private val panels: MutableMap<Point, Int> = mutableMapOf(currentPosition to BLACK)

    internal val paintedPanels = mutableSetOf<Point>()

    companion object Color {
        const val BLACK = 0
        const val WHITE = 1
    }

    enum class Direction {
        UP,
        LEFT,
        DOWN,
        RIGHT
    }

    init {
        paint(WHITE) //Part 2: start with a white panel
    }

    fun step(): Point {
        when (currentDirection) {
            Direction.UP -> currentPosition += Point(0, 1)
            Direction.DOWN -> currentPosition += Point(0, -1)
            Direction.LEFT -> currentPosition += Point(-1, 0)
            Direction.RIGHT -> currentPosition += Point(1, 0)
        }
        return currentPosition
    }

    fun turnLeft() {
        currentDirection = Direction.values()[(currentDirection.ordinal + 1) % 4]
    }

    fun turnRight() {
        currentDirection = Direction.values()[(currentDirection.ordinal + 3) % 4]
    }

    fun paint(color: Int) {
        if (panels.getOrDefault(currentPosition, BLACK) != color) {
            panels[currentPosition] = color
            paintedPanels.add(currentPosition)
        }
    }

    fun colorOfCurrentPanel(): Int {
        return panels.getOrDefault(currentPosition, BLACK);
    }

    fun asPrintableString(): String {
        val whitePanels = panels.filter { it.value == WHITE }.map { it.key }
        val minX = whitePanels.minBy { it.x }!!.x
        val minY = whitePanels.minBy { it.y }!!.y
        val normalizedWhitePoints = whitePanels.map { it - Point(minX, minY) }

        val lineLength = normalizedWhitePoints.map { it.x }.max()!! + 1
        val numLines = normalizedWhitePoints.map { it.y }.max()!! + 1

        val boardAsChars = List(numLines) { CharArray(lineLength) { ' ' } }
        normalizedWhitePoints.forEach { boardAsChars[it.y][it.x] = '#' }
        val boardAsString = boardAsChars.reversed()// when we print it out, the Y coordinate goes top-down
            .joinToString(separator = "\n") { String(it) }
        return boardAsString
    }
}

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y)
}

