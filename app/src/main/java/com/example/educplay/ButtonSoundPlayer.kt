package com.example.educplay

import android.content.Context
import android.media.MediaPlayer
import android.widget.ImageButton

object ButtonSoundPlayer {
    private var mediaPlayer: MediaPlayer? = null

    fun playSound(context: Context) {
        val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isSoundOn = prefs.getBoolean("sound_enabled", true)

        if (!isSoundOn) return // Don't play if sound is off

        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.button_sound)
        mediaPlayer?.start()
    }
}

// Reusable click extension for ImageButtons
fun ImageButton.setSoundClickListener(context: Context, action: () -> Unit) {
    this.setOnClickListener {
        ButtonSoundPlayer.playSound(context)
        action()
    }
}
