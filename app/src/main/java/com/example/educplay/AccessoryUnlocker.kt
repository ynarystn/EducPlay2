package com.example.educplay

import android.content.Context
import java.io.File

object AccessoryUnlocker {
    fun isEyeglassUnlocked(context: Context): Boolean {
        val score = loadScoreFromFile(context, 20)
        return score >= 2 // Or whatever threshold you want
    }

    private fun loadScoreFromFile(context: Context, level: Int): Int {
        return try {
            val file = File(context.getExternalFilesDir(null), "level_${level}_score.txt")
            if (file.exists()) {
                file.readText().trim().toInt()
            } else {
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }
}
