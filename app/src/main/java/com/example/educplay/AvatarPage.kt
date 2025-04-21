package com.example.educplay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.GridLayout
import android.widget.Toast

class AvatarPage : FullscreenActivity() {

    private var selectedEyewearResId: Int? = null  // To track selected eyewear

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_page)

        val accessoryGrid: GridLayout = findViewById(R.id.accessoryGrid)
        val eyewearSlot: ImageView = findViewById(R.id.eyewearSlot)

        // Load previously saved eyewear if any
        val sharedPref = getSharedPreferences("AvatarPrefs", MODE_PRIVATE)
        val savedEyewearResId = sharedPref.getInt("selected_eyewear", -1)
        if (savedEyewearResId != -1) {
            eyewearSlot.setImageResource(savedEyewearResId)
            eyewearSlot.visibility = View.VISIBLE
            selectedEyewearResId = savedEyewearResId
        }

        val accessories = listOf(
            Accessory(R.drawable.avatarpage_eyeglass, isUnlocked = AccessoryUnlocker.isEyeglassUnlocked(this), type = "eyewear", requiredMiniGame = "Mini Game 1"),
//            Accessory(R.drawable.avatarpage_eyeglass, isUnlocked = true, type = "eyewear", requiredMiniGame = "Mini Game 1"),
            Accessory(R.drawable.avatarpage_ribbon, isUnlocked = false, type = "ribbon", requiredMiniGame = "Mini Game 2"),
            Accessory(R.drawable.avatarpage_flower, isUnlocked = false, type = "flower", requiredMiniGame = "Mini Game 3"),
            Accessory(R.drawable.avatarpage_hat, isUnlocked = false, type = "hat", requiredMiniGame = "Mini Game 4"),
            Accessory(R.drawable.avatarpage_pirate, isUnlocked = false, type = "pirate", requiredMiniGame = "Mini Game 5"),
            Accessory(R.drawable.avatarpage_bear, isUnlocked = false, type = "bear", requiredMiniGame = "Mini Game 6")
        )

        for (accessory in accessories) {
            val itemView = layoutInflater.inflate(R.layout.avatar_item_accessory, accessoryGrid, false)
            val accessoryImage: ImageButton = itemView.findViewById(R.id.accessoryImage)
            val lockIcon: ImageButton = itemView.findViewById(R.id.accessoryLock)

            accessoryImage.setImageResource(accessory.imageResId)

            if (accessory.isUnlocked) {
                lockIcon.visibility = View.INVISIBLE
                accessoryImage.setSoundClickListener(this) {
                    when (accessory.type) {
                        "eyewear" -> {
                            eyewearSlot.setImageResource(accessory.imageResId)
                            eyewearSlot.visibility = View.VISIBLE
                            selectedEyewearResId = accessory.imageResId
                        }
                        // Add logic for other types if needed
                    }
                }
            } else {
                lockIcon.visibility = View.VISIBLE
                lockIcon.setSoundClickListener(this) {
                    Toast.makeText(this, "This accessory is locked. Complete ${accessory.requiredMiniGame} to unlock it.", Toast.LENGTH_SHORT).show()
                }
            }

            accessoryGrid.addView(itemView)
        }

        val saveButton: ImageButton = findViewById(R.id.avatarpage_save_button)
        saveButton.setSoundClickListener(this) {
            val editor = sharedPref.edit()
            if (selectedEyewearResId != null) {
                editor.putInt("selected_eyewear", selectedEyewearResId!!)
                Toast.makeText(this, "Avatar saved!", Toast.LENGTH_SHORT).show()
            } else {
                editor.remove("selected_eyewear") // Remove saved accessory if user cleared it
                Toast.makeText(this, "Accessory removed and saved.", Toast.LENGTH_SHORT).show()
            }
            editor.apply()
        }


        val startOverButton: ImageButton = findViewById(R.id.avatarpage_start_button)
        startOverButton.setSoundClickListener(this) {
            eyewearSlot.setImageDrawable(null)
            eyewearSlot.visibility = View.INVISIBLE
            selectedEyewearResId = null  // Clear selection, but NOT saved yet
        }

        val buttonClose: ImageButton = findViewById(R.id.button_close)
        buttonClose.setSoundClickListener(this) {
            startActivity(Intent(this, HomePage::class.java))
        }
    }
}
