package com.example.dicecreation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.lang.RuntimeException

class ScoreFragment : Fragment() {

    private lateinit var scoreButtons: Array<Button>
    private val lockedPoints = BooleanArray(13) { false }
    private val pointCalculator = PointCalculator()
    private var listener: ScoreFragmentListener? = null
    private var isPointLockedThisRound = false

    // Interface to communicate with Main_Activity
    interface ScoreFragmentListener {
        fun getDiceValues(): IntArray
        fun resetDiceViews()
        fun showToast(message: String)
        fun updateDiceRollsLeft(rollsLeft: Int)
        fun isScoreLockedThisRound(): Boolean
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ScoreFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement ScoreFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_score, container, false)

        scoreButtons = arrayOf(
            view.findViewById(R.id.btn_ones),
            view.findViewById(R.id.btn_twos),
            view.findViewById(R.id.btn_threes),
            view.findViewById(R.id.btn_fours),
            view.findViewById(R.id.btn_fives),
            view.findViewById(R.id.btn_sixes),
            view.findViewById(R.id.btn_triple),
            view.findViewById(R.id.btn_quads),
            view.findViewById(R.id.btn_house),
            view.findViewById(R.id.btn_small_straight),
            view.findViewById(R.id.btn_large_straight),
            view.findViewById(R.id.btn_yahtzee),
            view.findViewById(R.id.btn_chance)
        )

        scoreButtons.forEachIndexed { index, button ->
            button.setOnClickListener { lockPoint(index) }
        }

        return view
    }

    private fun lockPoint(index: Int) {
        if (isPointLockedThisRound) {
            listener?.showToast("You can only lock one score per round")
            return
        }

        if (!lockedPoints[index]) {
            lockedPoints[index] = true
            scoreButtons[index].alpha = 0.5f
            isPointLockedThisRound = true
        }
    }
    fun updateScores(diceValues: IntArray) {
        updatePoints(diceValues)
    }

    private fun updatePoints(diceValues: IntArray) {
        val allPointsList = mutableListOf<Int>()

        // Points for singles
        allPointsList.addAll(pointCalculator.pointsForSingles(diceValues).toList())

        // Points for the other categories
        allPointsList.add(pointCalculator.pointsForTriple(diceValues))
        allPointsList.add(pointCalculator.pointsForQuads(diceValues))
        allPointsList.add(pointCalculator.pointsForFullHouse(diceValues))
        allPointsList.add(pointCalculator.pointsForSmallStraight(diceValues))
        allPointsList.add(pointCalculator.pointsForLargeStraight(diceValues))
        allPointsList.add(pointCalculator.pointsForYahtzee(diceValues))
        allPointsList.add(pointCalculator.pointsForChance(diceValues))

        // Convert to an IntArray and update the view
        updatePointViews(allPointsList.toIntArray())
    }

    private fun updatePointViews(points: IntArray) {
        points.forEachIndexed { index, score ->
            if (!lockedPoints[index]) {
                scoreButtons[index].text = score.toString()
            }
        }
    }

    fun resetLock() {
        isPointLockedThisRound = false
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun isScoreLockedThisRound(): Boolean {
        return isPointLockedThisRound
    }
}