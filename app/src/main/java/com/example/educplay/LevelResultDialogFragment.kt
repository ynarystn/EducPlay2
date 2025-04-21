package com.example.educplay

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.io.File

class LevelResultDialogFragment(private val score: Int, private val level: Int) : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Optional fullscreen style
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_popup_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popupStars = view.findViewById<ImageView>(R.id.popup_stars)
        val popupBanner = view.findViewById<ImageView>(R.id.popup_banner)
        val homeBtn = view.findViewById<ImageButton>(R.id.popup_button_home)
        val nextBtn = view.findViewById<ImageButton>(R.id.popup_button_next)
        val replayBtn = view.findViewById<ImageButton>(R.id.popup_button_replay)
        val closeBtn = view.findViewById<ImageButton>(R.id.button_close_popup)



        // Set banner and replay icons based on score
        val bannerDrawable: Int
        val replayDrawable: Int

        if (score <= 2) {
            bannerDrawable = R.drawable.popup_failed_banner
            replayDrawable = R.drawable.popup_replay_failed
            nextBtn.visibility = View.GONE

            // Optional: shift home button
            val layoutParams = homeBtn.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.marginStart = 60
            homeBtn.layoutParams = layoutParams
        } else {
            bannerDrawable = R.drawable.popup_complete_banner
            replayDrawable = R.drawable.popup_replay
            nextBtn.visibility = View.VISIBLE
        }

        popupBanner.setImageResource(bannerDrawable)
        replayBtn.setImageResource(replayDrawable)

        // Set stars based on score
        val starDrawable = when (score) {
            5 -> R.drawable.star_3
            in 3..4 -> R.drawable.star_2
            2 -> R.drawable.star_1
            else -> R.drawable.star_0
        }
        popupStars.setImageResource(starDrawable)

        // Button listeners
        homeBtn.setSoundClickListener(requireContext()) {
            startActivity(Intent(requireContext(), HomePage::class.java))
            requireActivity().finish()
        }

        replayBtn.setSoundClickListener(requireContext()) {
            dismiss()
            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }

        nextBtn.setSoundClickListener(requireContext()) {
            dismiss()
            val intent = Intent(requireContext(), SolvingGame::class.java)
            intent.putExtra("LEVEL", level + 1)
            startActivity(intent)
            requireActivity().finish()
            saveScoreToFile(level, score)
        }


        closeBtn.setSoundClickListener(requireContext()) {
            val intent = Intent(requireContext(), LevelSelectionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        val width = dpToPx(620)
        val height = dpToPx(400)

        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun saveScoreToFile(level: Int, score: Int) {
        try {
            val context = requireContext() // Get the context
            val fileName = "level_${level}_score.txt"
            val file = File(context.getExternalFilesDir(null), fileName)  // Use context to get external files directory

            // Write the score to the file
            file.writeText(score.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}
