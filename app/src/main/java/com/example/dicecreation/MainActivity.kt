package com.example.dicecreation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val diceViewIds = arrayOf(
        R.id.diceView1,
        R.id.diceView2,
        R.id.diceView3,
        R.id.diceView4,
        R.id.diceView5,
        R.id.diceView6
    )

    private lateinit var diceViews: Array<DiceView>
    private val pointCalculator = PointCalculator()
    private val lockedPoints = BooleanArray(6) { false }
    private var diceRollsLeft = 3
    private var lockTurnScore = false
    private lateinit var pointSingles: Array<Button>
    private lateinit var diceCounter: TextView
    private lateinit var rollDiceButton: Button
    private lateinit var newTurnButton: Button




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diceViews = Array(diceViewIds.size) { i ->
            findViewById(diceViewIds[i])
        }

        pointSingles = arrayOf(
            findViewById(R.id.btn_ones),
            findViewById(R.id.btn_twos),
            findViewById(R.id.btn_threes),
            findViewById(R.id.btn_fours),
            findViewById(R.id.btn_fives),
            findViewById(R.id.btn_sixes)
        )

        pointSingles.forEachIndexed { pointIndex, button ->
            button.setOnClickListener {
                togglePointLock(pointIndex)
            }
        }

        diceCounter = findViewById(R.id.diceCounter)
        diceCounter.text = diceRollsLeft.toString()

        rollDiceButton = findViewById(R.id.rollDiceButton)
        rollDiceButton.setOnClickListener {
            rollAllDices()
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
    private fun rollAllDices() {
        if (diceRollsLeft > 0) {
            diceViews.forEach { diceView ->
                if (!diceView.dice.isLocked) {
                    diceView.visibility = View.VISIBLE
                    diceView.rollDice()
                }
            }
            diceRollsLeft--
            diceCounter.text = diceRollsLeft.toString()

            val diceValues = diceViews.map { it.dice.value }.toIntArray()
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
        unlockAllDices()
        unlockAllPoints()
        lockTurnScore = false

        diceViews.forEach { diceView ->
            diceView.visibility = View.GONE
        }
    }
    private fun unlockAllPoints() {
        pointSingles.forEachIndexed { index, button ->
            if (!lockedPoints[index]) {
                button.alpha = 1.0f
                button.text = "0"
            }
        }
    }
    private fun unlockAllDices() {
        diceViews.forEach { diceView ->
            diceView.unlockDice()
        }
    }
}