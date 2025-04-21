package com.example.educplay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import java.io.File


class LevelPageFragment : Fragment() {

    private var startLevel = 1
    private var endLevel = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startLevel = it.getInt(ARG_START_LEVEL)
            endLevel = it.getInt(ARG_END_LEVEL)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_level_page, container, false)
        val levelGrid = view.findViewById<GridLayout>(R.id.level_grid)

        levelGrid.post {
            val columnCount = levelGrid.columnCount
            val totalWidth = levelGrid.width
            val buttonSize = (totalWidth - (columnCount * 3)) / columnCount

            for ((index, level) in (startLevel..endLevel).withIndex()) {
                val buttonView = inflater.inflate(R.layout.item_level_button, levelGrid, false)

                val levelButton = buttonView.findViewById<ImageButton>(R.id.levelButton)
                val lockIcon = buttonView.findViewById<ImageView>(R.id.lockIcon)
                val starIcon = buttonView.findViewById<ImageView>(R.id.starIcon)

                val drawableId = resources.getIdentifier("level_button_$level", "drawable", requireContext().packageName)
                levelButton.setImageResource(drawableId)

                // Load score from internal storage
                val score = loadScoreFromFile(level)

                // Set star icon based on score
                val starDrawable = when (score) {
                    5 -> R.drawable.star_3
                    3, 4 -> R.drawable.star_2
                    2 -> R.drawable.star_1
                    else -> R.drawable.star_0
                }

                // Set the correct star icon based on the score
                starIcon.setImageResource(starDrawable)

                // Lock logic: Disable button and show lock icon for levels greater than 1
                // Unlock logic: Unlock level 2 if level 1 has at least 2 stars
                val prevLevelScore = if (level > 1) loadScoreFromFile(level - 1) else 0
                val isUnlocked = level == 1 || prevLevelScore >= 3

                if (isUnlocked) {
                    lockIcon.visibility = View.GONE
                    levelButton.isEnabled = true
                } else {
                    lockIcon.visibility = View.VISIBLE
                    levelButton.isEnabled = false
                }


                // Determine row: top row (0), bottom row (1)
                val row = index / columnCount
                val layout = buttonView.findViewById<LinearLayout>(R.id.levelItemLayout)
                layout.removeView(starIcon)

                if (row == 0) {
                    layout.addView(starIcon, 0) // Star on top
                } else {
                    layout.addView(starIcon)    // Star on bottom
                    starIcon.scaleY = -1f       // Flip vertically
                }

                // Level button click logic
                levelButton.setSoundClickListener(requireContext()) {
                    val intent = Intent(requireContext(), SolvingGame::class.java)
                    intent.putExtra("LEVEL", level)
                    startActivity(intent)
                }

                // Dynamic size adjustment for buttons
                val layoutParams = GridLayout.LayoutParams().apply {
                    width = buttonSize - 10
                    height = buttonSize + (buttonSize / 10)
                    setMargins(0, 0, 0, 0)
                }
                buttonView.layoutParams = layoutParams

                levelGrid.addView(buttonView)
            }
        }

        return view
    }

    companion object {
        private const val ARG_START_LEVEL = "start_level"
        private const val ARG_END_LEVEL = "end_level"

        fun newInstance(startLevel: Int, endLevel: Int) = LevelPageFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_START_LEVEL, startLevel)
                putInt(ARG_END_LEVEL, endLevel)
            }
        }
    }

    // Helper function to load score from internal storage based on level
    private fun loadScoreFromFile(level: Int): Int {
        return try {
            val file = File(requireContext().getExternalFilesDir(null), "level_${level}_score.txt")
            if (file.exists()) {
                file.readText().trim().toInt()
            } else {
                0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0  // Return 0 if there's an error
        }
    }

}

