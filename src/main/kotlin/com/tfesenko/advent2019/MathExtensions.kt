package com.tfesenko.advent2019

fun gcd(num1: Long, num2: Long): Long {
    var positiveNum1 = if (num1 > 0) num1 else -num1
    var positiveNum2 = if (num2 > 0) num2 else -num2
    while (positiveNum1 != positiveNum2) {
        if (positiveNum1 > positiveNum2)
            positiveNum1 -= positiveNum2
        else
            positiveNum2 -= positiveNum1
    }
    return positiveNum1
}

fun gcd(num1: Int, num2: Int): Int {
   return gcd(num1.toLong(), num2.toLong()).toInt()
}