package com.example.dicecreation

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast

class DiceView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : super(context) {
        initDiceView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initDiceView()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initDiceView()
    }

    internal val dice = Dice()

    private fun initDiceView() {
        updateDiceImage()
        this.setOnClickListener {
            toggleLock()
        }
    }

    private fun getDiceImages(value: Int): Int {
        return when (value) {
            1 -> R.drawable.dice1
            2 -> R.drawable.dice2
            3 -> R.drawable.dice3
            4 -> R.drawable.dice4
            5 -> R.drawable.dice5
            else -> R.drawable.dice6

        }
    }

    private fun updateDiceImage() {
        this.setImageResource(getDiceImages(dice.value))
    }

    fun unlockDice() {
        dice.unlock()
        this.alpha = 1.0f
    }

    fun toggleLock() {
        dice.toggleLock()
        this.alpha = if (dice.isLocked) 0.5f else 1.0f
    }

    fun rollDice() {
        dice.roll()
        updateDiceImage()
    }
}