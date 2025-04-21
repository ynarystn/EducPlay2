package com.example.educplay

import android.content.Context
import android.media.MediaPlayer

object MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isInitialized = false

    fun startMusic(context: Context) {
        if (!isInitialized) {
            mediaPlayer = MediaPlayer.create(context, R.raw.bg_music)
            mediaPlayer?.isLooping = true
            mediaPlayer?.setVolume(0.5f, 0.5f)
            isInitialized = true
        }

        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    fun stopMusic() {
        mediaPlayer?.pause()
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isInitialized = false
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
}
