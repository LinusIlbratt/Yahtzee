package com.example.dicecreation

class PointCalculator {

    // Calculate points for ones, twos, threes, fours, fives and sixes
    fun pointsForSingles(diceValue: IntArray): IntArray {
        val points = IntArray(6) { 0 }

        // Count occurrences for each value 1-6
        for (value in diceValue) {
            if (value in 1..6) {
                points[value - 1] += value
            }
        }

        return points
    }
    fun pointsForTriple(diceValues: IntArray): Int {
        val counts = diceValues.groupBy { it }.mapValues { it.value.size }
        val tripleValue = counts.filter { it.value >= 3 }.keys.firstOrNull()
        return tripleValue?.let { it * 3 } ?: 0
    }

    fun pointsForQuads(diceValues: IntArray): Int {
        val counts = diceValues.groupBy { it }.mapValues { it.value.size }
        val quadsValue = counts.filter { it.value >= 4 }.keys.firstOrNull()
        return quadsValue?.let { it * 4 } ?: 0
    }
    fun pointsForFullHouse(diceValue: IntArray): Int {
        val counts = diceValue.groupBy { it }.map { it.value.size }
        return if (counts.contains(3) && counts.contains(2)) 25 else 0
    }
    fun pointsForSmallStraight(diceValue: IntArray): Int {
        val sortedValues = diceValue.distinct().sorted()
        return if (sortedValues == listOf(1, 2, 3, 4, 5)) 40 else 0
    }
    fun pointsForLargeStraight(diceValue: IntArray): Int {
        val sortedValues = diceValue.distinct().sorted()
        return if (sortedValues == listOf(2, 3, 4, 5, 6)) 40 else 0
    }
    fun pointsForYahtzee(diceValue: IntArray): Int {
        return if (diceValue.distinct().size == 1) 50 else 0
    }
    fun pointsForChance(diceValue: IntArray): Int {
        return diceValue.sum()
    }
    private fun hasNoOfAKind(diceValue: IntArray, i: Int): Boolean {
        return diceValue.groupBy { it }.any { it.value.size >= i}
    }
}