package com.example.osoitahotelbooking

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoadActivity : AppCompatActivity() {

    private lateinit var hotelTextView: TextView
    private lateinit var descriptionTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        hotelTextView = findViewById(R.id.hotel)
        descriptionTextView = findViewById(R.id.text)

        val handler = Handler(Looper.getMainLooper())
        val navigateToSignup = Runnable {
            val intent = Intent(this@LoadActivity, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        handler.postDelayed(navigateToSignup, 2000)
    }
}
