package com.example.educplay

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomePage : FullscreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnSolvingFrame: ImageButton = findViewById(R.id.h_solving_frame)
        val settingsBtn: ImageButton = findViewById(R.id.button_settings)
        val avatarBtn: ImageButton = findViewById(R.id.h_avatar_frame)

        btnSolvingFrame.setSoundClickListener(this) {
            val intent = Intent(this, LevelSelectionActivity::class.java)
            startActivity(intent)
        }

        settingsBtn.setSoundClickListener(this) {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        avatarBtn.setSoundClickListener(this) {
            val intent = Intent(this, AvatarPage::class.java)
            startActivity(intent)
        }
    }
}