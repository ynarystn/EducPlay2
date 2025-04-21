package com.example.educplay

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class LevelSelectionActivity : FullscreenActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var buttonLeft: ImageButton
    private lateinit var buttonRight: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_level_selection)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Init ViewPager and TabLayout
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
        buttonLeft = findViewById(R.id.level_button_left)
        buttonRight = findViewById(R.id.level_button_right)

        // Adapter setup for ViewPager2
        val adapter = LevelPagerAdapter(this as AppCompatActivity)

        viewPager.adapter = adapter

        // Attach TabLayout to ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val customView = LayoutInflater.from(this).inflate(R.layout.custom_tab, null)

            val tabIcon = customView.findViewById<ImageView>(R.id.tab_icon)

            // Set the correct icon for each tab
            val iconRes = if (position == 0) R.drawable.tab_level_1 else R.drawable.tab_level_2
            tabIcon.setImageResource(iconRes)

            // Optionally, give the first tab a background to show it's selected by default
            if (position == 0) {
                customView.setBackgroundResource(R.drawable.level_tab_1) // your bg drawable
            }

            tab.customView = customView
        }.attach()

        // Change tab icon on page change
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                for (i in 0 until tabLayout.tabCount) {
                    val tab = tabLayout.getTabAt(i)
                    val customView = tab?.customView ?: continue
                    if (i == position) {
                        customView.setBackgroundResource(R.drawable.level_tab_1) // bg when selected
                    } else {
                        customView.setBackgroundResource(0) // remove background
                    }
                }

                // Show/Hide arrow buttons based on current page
                buttonLeft.visibility = if (position == 0) View.INVISIBLE else View.VISIBLE
                buttonRight.visibility = if (position == (viewPager.adapter?.itemCount ?: 1) - 1) View.INVISIBLE else View.VISIBLE
            }
        })

        // Set up button actions for left/right arrow buttons
        buttonLeft.setSoundClickListener(this) {
            if (viewPager.currentItem > 0) {
                viewPager.currentItem -= 1
            }
        }

        buttonRight.setSoundClickListener(this) {
            if (viewPager.currentItem < (viewPager.adapter?.itemCount ?: 0) - 1) {
                viewPager.currentItem += 1
            }
        }

        val buttonCLose: ImageButton = findViewById(R.id.button_close)
        buttonCLose.setSoundClickListener(this) {
            val intent = Intent(this, HomePage::class.java)
            startActivity(intent)
        }
    }
}
