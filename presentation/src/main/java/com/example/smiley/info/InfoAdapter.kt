package com.example.smiley.info

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class InfoAdapter(activity: FragmentActivity, private val count: Int): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> UserInfoFragment()
            else -> UserInfoFragment()
        }
    }
}