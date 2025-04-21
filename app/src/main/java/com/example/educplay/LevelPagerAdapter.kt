package com.example.educplay

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LevelPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2 // Two pages: 1–10, 11–20

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LevelPageFragment.newInstance(1, 10)   // First page = levels 1–10
            1 -> LevelPageFragment.newInstance(11, 20)  // Second page = levels 11–20
            else -> throw IllegalArgumentException("Invalid page index")
        }
    }
}
