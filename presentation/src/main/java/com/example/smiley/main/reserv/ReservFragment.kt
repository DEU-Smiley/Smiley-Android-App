package com.example.smiley.main.reserv

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.databinding.FragmentReservBinding
import com.example.smiley.main.reserv.adapter.ReservViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservFragment : Fragment() {
    private var _bind: FragmentReservBinding?=null
    private val bind get() = _bind!!

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_reserv, container, false)

        initViewPager()
        initTabLayout()

        return bind.root
    }

    /**
     * TabLayout 초기화 메소드
     */
    private fun initTabLayout() {
        val tabTitles = arrayListOf("현재 예약", "이전 예약")
        TabLayoutMediator(bind.tabLayout, bind.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        repeat(bind.tabLayout.tabCount){
            val tab = (bind.tabLayout.getChildAt(0) as ViewGroup).getChildAt(it)
            val params = tab.layoutParams as ViewGroup.MarginLayoutParams

            if(it == bind.tabLayout.tabCount-1) params.setMargins(0, 0, 0, 0)
            else params.setMargins(0, 0, 50, 0)
            tab.requestLayout()
        }
    }

    /**
     * ViewPager 초기화 메소드
     */
    private fun initViewPager(){
        with(bind){
            viewPager.adapter = ReservViewPagerAdapter(requireActivity())

        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReservFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}