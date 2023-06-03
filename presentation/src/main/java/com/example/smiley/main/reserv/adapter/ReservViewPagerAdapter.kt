package com.example.smiley.main.reserv.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.smiley.main.reserv.ReservDetailFragment
import com.example.smiley.main.reserv.ReservHistoryFragment

class ReservViewPagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {
    private val fragments = arrayListOf<Fragment>()

    init {
        fragments.add(ReservDetailFragment())
        fragments.add(ReservHistoryFragment())
    }

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int):Fragment = fragments[position]
}