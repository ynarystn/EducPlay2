package com.example.educplay

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Settings : FullscreenActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val checkButton: ImageButton = findViewById(R.id.button_music_check)
        val checkIcon: ImageView = findViewById(R.id.icon_music_check)

        var isMusicOn = sharedPref.getBoolean("music_enabled", true)
        checkIcon.visibility = if (isMusicOn) View.VISIBLE else View.INVISIBLE

        checkButton.setSoundClickListener(this) {
            isMusicOn = !isMusicOn
            sharedPref.edit().putBoolean("music_enabled", isMusicOn).apply()
            checkIcon.visibility = if (isMusicOn) View.VISIBLE else View.INVISIBLE

            if (isMusicOn) {
                MusicManager.startMusic(this)
            } else {
                MusicManager.stopMusic()
            }
        }

        val soundCheckButton: ImageButton = findViewById(R.id.button_sound_check)
        val soundCheckIcon: ImageView = findViewById(R.id.icon_sound_check)

        // Get current state (default: true if not saved yet)
        val isSoundOn = sharedPref.getBoolean("sound_enabled", true)
        soundCheckIcon.visibility = if (isSoundOn) View.VISIBLE else View.INVISIBLE

        soundCheckButton.setSoundClickListener(this) {
            val newSoundState = !sharedPref.getBoolean("sound_enabled", true)

            // ✅ Save new state to SharedPreferences
            sharedPref.edit().putBoolean("sound_enabled", newSoundState).apply()

            // ✅ Update check icon visibility
            soundCheckIcon.visibility = if (newSoundState) View.VISIBLE else View.INVISIBLE
        }


        val closeButton: ImageButton = findViewById(R.id.button_close_popup)
        closeButton.setSoundClickListener(this) {
            finish() // ← This will bring user back to the previous activity
        }

        val exitButton: ImageButton = findViewById(R.id.button_exit_settings)
        exitButton.setSoundClickListener(this) {
            finishAffinity() // Closes all activities and exits the app
        }
    }
}