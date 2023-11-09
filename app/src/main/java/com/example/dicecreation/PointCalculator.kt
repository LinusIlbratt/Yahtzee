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
}