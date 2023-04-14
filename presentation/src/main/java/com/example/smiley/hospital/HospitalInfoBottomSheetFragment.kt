package com.example.smiley.hospital

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.common.extension.computeDistanceToView
import com.example.smiley.common.extension.smoothScrollToView
import com.example.smiley.databinding.FragmentHospitalInfoBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HospitalInfoBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HospitalInfoBottomSheetFragment() : BottomSheetDialogFragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var _bind: FragmentHospitalInfoBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bind = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_hospital_info_bottom_sheet,
            container,
            false
        )

        initTabLayout()
        initNestedScrollView()

        return _bind.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            setUpFullHeight(it as BottomSheetDialog)
            addBtnToBottom(dialog, it)
        }

        return dialog
    }


    /**
     * 바텀 시트가 펼쳐졌을 때, Height를 화면 전체로 지정하는 메소드
     */
    private fun setUpFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet = bottomSheetDialog.findViewById<ConstraintLayout>(R.id.constraint_layout)
        val behavior = BottomSheetBehavior.from(bottomSheet?.parent as View)

        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    val layoutParams = bottomSheet.layoutParams as CoordinatorLayout.LayoutParams
                    layoutParams.height = getWindowHeight()

                    bottomSheet.layoutParams = layoutParams
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })
    }

    /**
     * 바텀 시트 아래에 버튼을 고정시키는 메소드
     */
    private fun addBtnToBottom(dialog: BottomSheetDialog, dialogInterface: DialogInterface) {
        val containerLayout =
            (dialogInterface as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.container)
        val button = dialog.layoutInflater.inflate(R.layout.sub_bottom_sheet_button, null)

        button.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
        }
        containerLayout!!.addView(button)
    }

    private fun getWindowHeight(): Int {
        return (context as Activity).findViewById<View>(Window.ID_ANDROID_CONTENT).height
    }

    /**
     * TabLayout 초기화 메소드
     */
    private fun initTabLayout() {

        _bind.tabLayout.apply {
            addTab(newTab().setText("병원정보"))
            addTab(newTab().setText("위치정보"))
            addTab(newTab().setText("리뷰"))

            addOnTabSelectedListener(tabSelectedListener)
        }

        repeat(_bind.tabLayout.tabCount){
            val tab = (_bind.tabLayout.getChildAt(0) as ViewGroup).getChildAt(it)
            val params = tab.layoutParams as MarginLayoutParams

            if(it == _bind.tabLayout.tabCount-1) params.setMargins(0, 0, 0, 0)
            else params.setMargins(0, 0, 50, 0)
            tab.requestLayout()
        }
    }

    private fun initNestedScrollView(){
        _bind.nestedScrollView.setOnScrollChangeListener(scrollChangedListener)
    }

    /**
     * Tab 선택 리스너
     */
    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabUnselected(tab: Tab?) = Unit
        override fun onTabReselected(tab: Tab?) = Unit
        override fun onTabSelected(tab: Tab?) {
            _bind.appBarLayout.setExpanded(false, true)
            when (tab?.position) {
                0 -> _bind.nestedScrollView.smoothScrollToView(_bind.hospitalTimeInfo)
                1 -> _bind.nestedScrollView.smoothScrollToView(_bind.hospitalPositInfo)
                2 -> _bind.nestedScrollView.smoothScrollToView(_bind.hospitalReviewInfo)
            }
        }
    }

    /**
     * NestedScrollView 스크롤 리스너
     */
    private val scrollChangedListener = View.OnScrollChangeListener { _, _, scrollY, _, _ ->
        _bind.nestedScrollView.apply {
            when {
                scrollY < (computeDistanceToView(_bind.hospitalPositInfo) / 2)-> {
                    _bind.tabLayout.setScrollPosition(0, 0f, true)
                }
                scrollY < (computeDistanceToView(_bind.hospitalReviewInfo) / 2) -> {
                    _bind.tabLayout.setScrollPosition(1, 0f, true)
                }
                else ->{
                    _bind.tabLayout.setScrollPosition(2, 0f, true)
                }
            }
        }
    }
}