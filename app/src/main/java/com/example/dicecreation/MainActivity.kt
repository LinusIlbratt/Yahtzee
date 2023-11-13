package com.example.dicecreation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), ScoreFragment.ScoreFragmentListener {

    private val diceViewIds = arrayOf(
        R.id.diceView1,
        R.id.diceView2,
        R.id.diceView3,
        R.id.diceView4,
        R.id.diceView5
    )

    private lateinit var diceViews: Array<DiceView>
    private var diceRollsLeft = 3
    private var lockTurnScore = false
    private lateinit var diceCounter: TextView
    private lateinit var rollDiceButton: Button
    private lateinit var newTurnButton: Button
    private var isScoreLocked = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ScoreFragment())
                .commit()
        }

        diceViews = Array(diceViewIds.size) { i ->
            findViewById(diceViewIds[i])
        }

        diceViews.forEach { it.visibility = View.GONE }

        diceCounter = findViewById(R.id.diceCounter)
        diceCounter.text = diceRollsLeft.toString()

        rollDiceButton = findViewById(R.id.rollDiceButton)
        rollDiceButton.setOnClickListener {
            rollAllDices()
        }

        newTurnButton = findViewById(R.id.btn_newTurn)
        newTurnButton.setOnClickListener {
            val scoreFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? ScoreFragment
            if (scoreFragment?.isScoreLockedThisRound() == true) {
                startNewRound()
            } else {
                Toast.makeText(this, "Please lock in a score before ending the round", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun rollAllDices() {
        // Show the dices if they arent visible
        if (diceViews.any { it.visibility == View.GONE }) {
            diceViews.forEach { it.visibility = View.VISIBLE }
        }

        if (diceRollsLeft > 0) {
            diceViews.forEach { diceView ->
                if (!diceView.dice.isLocked) {
                    diceView.visibility = View.VISIBLE
                    diceView.rollDice()
                }
            }
            val diceValues = diceViews.map { it.dice.value }.toIntArray()

            val scoreFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? ScoreFragment
            scoreFragment?.updateScores(diceValues)

            diceRollsLeft--
            diceCounter.text = diceRollsLeft.toString()

        } else {
            Toast.makeText(this, "Lock in a score", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetRolls() {
        diceRollsLeft = 3
        diceCounter.text = diceRollsLeft.toString()
    }

    private fun prepareNewRound() {
        resetRolls()
    }

    private fun startNewRound() {
        prepareNewRound()
        unlockAllDices()
        lockTurnScore = false
        diceViews.forEach { diceView ->
            diceView.visibility = View.GONE
        }
        val scoreFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? ScoreFragment
        scoreFragment?.resetLock()
    }

    private fun unlockAllDices() {
        diceViews.forEach { diceView ->
            diceView.unlockDice()
        }
    }

    override fun getDiceValues(): IntArray {
        return diceViews.map { it.dice.value }.toIntArray()
    }

    override fun resetDiceViews() {
        diceViews.forEach { diceView ->

        }
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun updateDiceRollsLeft(rollsLeft: Int) {
        diceRollsLeft = rollsLeft
        diceCounter.text = diceRollsLeft.toString()
    }
    override fun isScoreLockedThisRound(): Boolean {
        return isScoreLocked
    }
}