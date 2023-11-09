package com.example.dicecreation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    private val diceArray = Array(6) { Dice() }
    private val pointCalculator = PointCalculator()
    private val lockedPoints = BooleanArray(6) { false }
    private var diceRollsLeft = 3
    private var lockTurnScore = false
    private lateinit var pointSingles: Array<Button>
    private lateinit var diceCounter: TextView
    private lateinit var diceImages: Array<ImageView>
    private lateinit var rollDiceButton: Button
    private lateinit var newTurnButton: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceImages = arrayOf(
            findViewById(R.id.imageView),
            findViewById(R.id.imageView2),
            findViewById(R.id.imageView3),
            findViewById(R.id.imageView4),
            findViewById(R.id.imageView5),
            findViewById(R.id.imageView6)
        )

        pointSingles = arrayOf(
            findViewById(R.id.btn_ones),
            findViewById(R.id.btn_twos),
            findViewById(R.id.btn_threes),
            findViewById(R.id.btn_fours),
            findViewById(R.id.btn_fives),
            findViewById(R.id.btn_sixes)
        )

        diceImages.forEachIndexed { dice, imageView ->
            imageView.setOnClickListener {
                diceArray[dice].toggleLock()
                updateDiceImage(dice)
            }
        }

        pointSingles.forEachIndexed { pointIndex, button ->
            button.setOnClickListener {
                togglePointLock(pointIndex)
            }
        }

        diceCounter = findViewById(R.id.diceCounter)
        diceCounter.text = diceRollsLeft.toString()

        rollDiceButton = findViewById(R.id.rollDiceButton)
        rollDiceButton.setOnClickListener {
            rollDice()
        }

        newTurnButton = findViewById(R.id.btn_newTurn)
        newTurnButton.setOnClickListener {
            if (lockTurnScore) {
                startNewRound()
            } else {
                Toast.makeText(this, "Please lock in a score before ending the round", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun rollDice() {
            if (diceRollsLeft > 0) {
                diceArray.forEachIndexed { diceIndex, _ ->
                    if (!diceArray[diceIndex].isLocked) {
                        diceArray[diceIndex].roll()
                        updateDiceImage(diceIndex)
                        diceImages[diceIndex].visibility = View.VISIBLE
                    }
                }
                diceRollsLeft--
                diceCounter.text = diceRollsLeft.toString()

                val diceValues = diceArray.map { it.value }.toIntArray()
                val singlePoints = pointCalculator.pointsForSingles(diceValues)
                updatePointViews(singlePoints)
            } else {
                Toast.makeText(this, "Lock in a score", Toast.LENGTH_SHORT).show()
            }
    }

    private fun resetRolls() {
        diceRollsLeft = 3
        diceCounter.text = diceRollsLeft.toString()
    }

    private fun unlockAllDices() {
        diceArray.forEach { it.toggleUnlock() }
        diceImages.forEach { it.alpha = 1.0f }
        diceArray.indices.forEach { updateDiceImage(it) }
    }

    private fun updateDiceImage(diceIndex: Int) {
        val die = diceArray[diceIndex]
        diceImages[diceIndex].setImageResource(die.drawableResource)
        diceImages[diceIndex].alpha = if (die.isLocked) 0.5f else 1.0f
    }

    private fun updatePointViews(singlePoints: IntArray) {
        singlePoints.forEachIndexed { index, points ->
            if (!lockedPoints[index]) {
                pointSingles[index].text = points.toString()
            }
        }
    }

    private fun togglePointLock(pointIndex: Int) {
        if (!lockedPoints[pointIndex] && !lockTurnScore) {
            lockedPoints[pointIndex] = true
            pointSingles[pointIndex].alpha = 0.5f
            resetPointsAfterLock(pointIndex)
            lockTurnScore = true
        } else {
            Toast.makeText(this, "You can only lock one score per round.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun prepareNewRound() {
        resetRolls()
        unlockAllDices()
    }

    private fun resetPointsAfterLock(lockedPointIndex: Int) {
        pointSingles.forEachIndexed { index, button ->
            if (index != lockedPointIndex && !lockedPoints[index]) {
                button.text = "0"
                if (!lockedPoints[index]) {
                    button.text = "0"
                }
            }
        }
    }

    private fun startNewRound() {
        prepareNewRound()
        unlockAllPoints()
        lockTurnScore = false
        makeDiceInvisible()
    }

    private fun makeDiceInvisible() {
        diceImages.forEach { it.visibility = View.INVISIBLE }
    }

    private fun unlockAllPoints() {
        pointSingles.forEachIndexed { index, button ->
            if (!lockedPoints[index]) {
                button.alpha = 1.0f
                button.text = "0"
            }
        }
    }
}