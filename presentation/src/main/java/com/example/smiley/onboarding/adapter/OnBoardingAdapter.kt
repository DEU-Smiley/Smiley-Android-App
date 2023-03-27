package com.example.smiley.onboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.smiley.onboarding.fragment.FirstOnBoardingFragment
import com.example.smiley.onboarding.fragment.SecondOnBoardingFragment
import com.example.smiley.onboarding.fragment.ThirdOnBoardingFragment

class OnBoardingAdapter(activity: FragmentActivity, private val count:Int): FragmentStateAdapter(activity){

    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FirstOnBoardingFragment()
            1 -> SecondOnBoardingFragment()
            2 -> ThirdOnBoardingFragment()
            else -> FirstOnBoardingFragment()
        }
    }
}