package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.CompletableFuture

class MainActivity : AppCompatActivity() {
    internal lateinit var button: Button
    internal lateinit var scoreLabel: TextView
    internal lateinit var timeLabel: TextView

    private var score = 0
    private var gameStarted = false
    private val GAME_TIME = 5000
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.buttonLabel)
        scoreLabel = findViewById(R.id.scoreLabel)
        timeLabel = findViewById(R.id.timeLabel)

        initDisplay()

        timer = object: CountDownTimer(GAME_TIME.toLong(), 1000){
            override fun onTick(millisUntilFinish: Long) {
                setTime(millisUntilFinish / 1000)
            }

            override fun onFinish() {
                gameOver()
            }
        }

        button.setOnClickListener { view ->
            buttonClicked()
        }
    }

    private fun setTime(l: Long) {
        var timeText = getString(R.string.timeLabel, l)
        timeLabel.text = timeText
    }

    private fun setScore(score: Int){
        var scoreText = getString(R.string.scoreLabel, score)
        scoreLabel.text = scoreText
    }

    private fun initDisplay() {
        setScore(0)
        setTime(GAME_TIME.toLong() / 1000)
    }

    private fun buttonClicked() {
        if (gameStarted) {
            score += 1
            setScore(score)
        } else {
            startGame()
        }
    }

    private fun startGame() {
        gameStarted = true
        score = 0
        button.text = "Tap me"
        timer.start()
    }

    private fun gameOver(){
        gameStarted = false
        button.text = getString(R.string.buttonLabel)
        var message = getString(R.string.message, score)
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        score = 0
        setScore(score)
        setTime(GAME_TIME.toLong()/1000)
    }

}