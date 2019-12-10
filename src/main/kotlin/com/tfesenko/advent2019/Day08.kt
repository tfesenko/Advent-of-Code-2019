import com.tfesenko.advent2019.Resources

/**
 * Solution for Day 8: Space Image Format
 * https://adventofcode.com/2019/day/8
 */

fun main() {
    println(
        Day08(Resources.getResource("Day08.txt").readText().trim(), Pair(25, 6))
            .solve2()
    )
}

class Day08(rawInput: String, private val dimensions: Pair<Int, Int>) {

    private val layers: List<List<Int>> =
        rawInput.map { it.toInt() - '0'.toInt() }
            .chunked(dimensions.first * dimensions.second)

    fun solve1(): Int {
        val layerWithFewestZeros = layers.minBy { layer -> layer.count { it == 0 } }!!
        return layerWithFewestZeros.count { it == 1 } * layerWithFewestZeros.count { it == 2 }
    }

    fun solve2() {
        Image(layers, dimensions).printImage()
    }

}

class Image(private val layers: List<List<Int>>, private val dimension: Pair<Int, Int>) {

    companion object {
        const val TRANSPARENT = 2
        const val BLACK = 0
        const val WHITE = 1
    }

    fun printImage() {
        println(render().map {
            when (it) {
                TRANSPARENT -> " ";
                BLACK -> "X";
                WHITE -> " ";
                else -> "?"
            }
        }
            .chunked(dimension.first)
            .joinToString(separator = "\n") { it.joinToString(separator = " ") })
    }

    fun render(): List<Int> =
        layers.reduce { topLayer, bottomLayer -> overlay(topLayer, bottomLayer) }

    private fun overlay(topLayer: List<Int>, bottomLayer: List<Int>): List<Int> {
        fun overlayPixel(topPixel: Int, bottomPixel: Int)
                = if (topPixel == TRANSPARENT) bottomPixel else topPixel

        return topLayer.zip(bottomLayer).map { overlayPixel(it.first, it.second) }
    }

}