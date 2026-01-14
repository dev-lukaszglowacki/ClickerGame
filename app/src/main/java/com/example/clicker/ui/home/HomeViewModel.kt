package com.example.clicker.ui.home

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val _timerText = MutableLiveData("0.00")
    val timerText: LiveData<String> = _timerText

    private val _resultText = MutableLiveData("")
    val resultText: LiveData<String> = _resultText

    private val _targetText = MutableLiveData("")
    val targetText: LiveData<String> = _targetText

    private val _scoreText = MutableLiveData("Score: 0")
    val scoreText: LiveData<String> = _scoreText

    private val _bestScoreText = MutableLiveData("Best score: 0")
    val bestScoreText: LiveData<String> = _bestScoreText

    private var startTime = 0L
    private var running = false
    private var targetTime = 0.0
    private var score = 0
    private var bestScore = 0

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (running) {
                val elapsed = (System.currentTimeMillis() - startTime) / 1000.0
                _timerText.value = String.format(Locale.ROOT, "%.2f", elapsed)
                handler.postDelayed(this, 16)
            }
        }
    }

    fun start() {
        startTime = System.currentTimeMillis()
        running = true
        _resultText.value = ""
        handler.post(timerRunnable)
    }

    fun stop() {
        running = false

        val finalTime = (System.currentTimeMillis() - startTime) / 1000.0
        val diff = kotlin.math.abs(finalTime - targetTime)

        val points = calculatePoints(diff)
        score += points
        if (score > bestScore) bestScore = score

        _resultText.value =
            "Your time: %.2f s\nDifference: %.2f s\n+%d points"
                .format(finalTime, diff, points)

        updateScore()
        generateTarget()
    }

    fun generateTarget() {
        targetTime = kotlin.random.Random.nextDouble(3.0, 8.0)
        _targetText.value = "Target time: %.2f s".format(targetTime)
    }

    private fun updateScore() {
        _scoreText.value = "Score: $score"
        _bestScoreText.value = "Best score: $bestScore"
    }

    private fun calculatePoints(diff: Double): Int =
        when {
            diff <= 0.05 -> 10
            diff <= 0.10 -> 7
            diff <= 0.25 -> 4
            else -> 0
        }

    override fun onCleared() {
        handler.removeCallbacks(timerRunnable)
        super.onCleared()
    }
}
