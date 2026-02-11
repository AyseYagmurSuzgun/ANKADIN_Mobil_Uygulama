package com.example.ankadin

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        tvTimer = findViewById(R.id.tvTimer)

        object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000) + 1
                tvTimer.text = secondsLeft.toString()
            }

            override fun onFinish() {
                tvTimer.text = "0"
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        }.start()
    }
}
