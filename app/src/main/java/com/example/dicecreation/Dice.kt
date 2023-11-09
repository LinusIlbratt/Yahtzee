package com.example.dicecreation

class Dice {
    var value = 1
    var isLocked = false

    val drawableResource: Int
        get() = when (value) {
            1 -> R.drawable.dice1
            2 -> R.drawable.dice2
            3 -> R.drawable.dice3
            4 -> R.drawable.dice4
            5 -> R.drawable.dice5
            else -> R.drawable.dice6
        }

    fun roll() {
        if (!isLocked) {
            value = (1..6).random()
        }
    }

    fun toggleLock() {
        isLocked = !isLocked
    }

    fun toggleUnlock() {
        isLocked = false
    }
}