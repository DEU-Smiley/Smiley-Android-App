package com.example.smiley.info.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.smiley.R
import com.example.smiley.common.extension.invisible
import com.example.smiley.common.extension.visible
import com.example.smiley.info.fragment.CalibrationInfoFragment
import com.example.smiley.info.fragment.MedicalInfoFragment
import com.example.smiley.info.fragment.UserInfoFragment


class InfoAdapter(activity: FragmentActivity, indicatorId: Int, private val count: Int): FragmentStateAdapter(activity) {
    private val fragmentMap = hashMapOf<Int, Fragment>()
    private val activity = activity
    private val pointer:List<View>
    private val text:List<TextView>

    init {
        val indicator = activity.findViewById<FrameLayout>(indicatorId)
        pointer = indicator.findViewById<LinearLayout>(R.id.pointer_layout).children.toList()
        text = indicator.findViewById<LinearLayout>(R.id.step_layout).children.toList().map { it as TextView }
    }

    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentFactory(position)
    }

    private fun fragmentFactory(position:Int): Fragment {
        if (fragmentMap[position] == null)
            fragmentMap[position] = when (position) {
                0 -> UserInfoFragment()
                1 -> MedicalInfoFragment()
                2 -> CalibrationInfoFragment()
                else -> UserInfoFragment()
            }

        return fragmentMap[position]!!
    }

    fun getFragmentAt(position:Int) = fragmentFactory(position)

    fun setIndicatorPosit(posit:Int){
        repeat(count){
            if(pointer[it].isVisible){
                pointer[it].invisible()
                text[it].setTextColor(activity.resources.getColor(R.color.gray3_8E))
            }
        }
        pointer[posit].visible()
        text[posit].setTextColor(activity.resources.getColor(R.color.white))
    }
}