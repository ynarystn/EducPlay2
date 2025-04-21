package com.example.educplay

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LogoPage : FullscreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logo_page)

        makeFullScreen()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val logo = findViewById<ImageView>(R.id.logo_bg)
//        val bgBlack = findViewById<ImageView>(R.id.bg_black_logo)
//
//        logo.setOnClickListener {
//            Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show()
//            bgBlack.visibility = View.VISIBLE
//        }

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoadingPage::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}