package com.example.dicecreation

class Dice {
    var value = 1
    var isLocked = false

    fun roll() {
        if (!isLocked) {
            value = (1..6).random()
        }
    }

    fun toggleLock() {
        isLocked = !isLocked
    }

    fun unlock() {
        isLocked = false
    }
}