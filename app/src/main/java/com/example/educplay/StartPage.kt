package com.example.educplay

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class StartPage : FullscreenActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isMusicOn = sharedPref.getBoolean("music_enabled", true)

        if (isMusicOn) {
            MusicManager.startMusic(this)
        }

        val btnStartText: ImageButton = findViewById(R.id.btn_start_text)
        val settingsBtn: ImageButton = findViewById(R.id.button_settings)

        btnStartText.setSoundClickListener(this) {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }

        settingsBtn.setSoundClickListener(this) {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }
    }

//    override fun onPause() {
//        super.onPause()
//        if (bgMusicPlayer.isPlaying) {
//            bgMusicPlayer.pause()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val isMusicOn = getSharedPreferences("AppPrefs", MODE_PRIVATE)
//            .getBoolean("music_enabled", true)
//
//        if (isMusicOn && !bgMusicPlayer.isPlaying) {
//            bgMusicPlayer.start()
//        }
//    }

//
//    override fun onDestroy() {
//        super.onDestroy()
//        bgMusicPlayer.release()
//    }

}