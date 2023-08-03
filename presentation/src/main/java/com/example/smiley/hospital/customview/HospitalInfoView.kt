package com.example.smiley.hospital.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.domain.hospital.model.Hospital
import com.example.smiley.R
import com.example.smiley.common.extension.computeDistanceToView
import com.example.smiley.common.extension.getTodayOfWeek
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.invisible
import com.example.smiley.common.extension.smoothScrollToView
import com.example.smiley.common.extension.visible
import com.example.smiley.databinding.CustomHospitalInfoLayoutBinding
import com.example.smiley.databinding.LayoutHospitalCallInfoBinding
import com.example.smiley.databinding.LayoutHospitalLocationInfoBinding
import com.example.smiley.databinding.LayoutHospitalReviewInfoBinding
import com.example.smiley.databinding.LayoutHospitalTimeInfoBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import java.util.Calendar

class HospitalInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    val bind: CustomHospitalInfoLayoutBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_hospital_info_layout,
            this,
            true
        )

    private val timeInfoBinding by lazy { LayoutHospitalTimeInfoBinding.bind(bind.root) }
    private val locationInfoBinding by lazy { LayoutHospitalLocationInfoBinding.bind(bind.root)}
    private val callInfoBinding by lazy { LayoutHospitalCallInfoBinding.bind(bind.root) }
    private val reviewInfoBinding by lazy { LayoutHospitalReviewInfoBinding.bind(bind.root) }

    private var behavior: BottomSheetBehavior<ConstraintLayout>? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        initTabLayout()
        initScrollView()
    }

    /**
     * TabLayout 초기화 메소드
     */
    private fun initTabLayout() {
        bind.tabLayout.apply {
            addTab(newTab().setText("병원정보"))
            addTab(newTab().setText("위치정보"))
            addTab(newTab().setText("리뷰"))

            addOnTabSelectedListener(tabSelectedListener)
        }

        repeat(bind.tabLayout.tabCount){
            val tab = (bind.tabLayout.getChildAt(0) as ViewGroup).getChildAt(it)
            val params = tab.layoutParams as MarginLayoutParams

            if(it == bind.tabLayout.tabCount-1) params.setMargins(0, 0, 0, 0)
            else params.setMargins(0, 0, 50, 0)
            tab.requestLayout()
        }
    }

    /**
     * NestedScrollView 초기화 메소드
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initScrollView(){
        bind.ssvContentScrollView.setOnScrollChangeListener(scrollChangedListener)
    }

    fun show(isShow: Boolean){
        if(isShow){
            bind.clBottomSheetLayout.visible()
        } else {
            bind.clBottomSheetLayout.gone()
        }
    }

    fun setLoading(isLoading: Boolean){
        if(isLoading) {
            bind.lottieLodingLayout.visible()
            bind.ssvContentScrollView.invisible()
        } else {
            bind.lottieLodingLayout.gone()
            bind.ssvContentScrollView.visible()
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateHospitalInfo(hospital: Hospital){
        with(bind) {
            partnerTag.text = if (hospital.isPartner) "제휴" else "일반"
            hospitalTitle.text = hospital.name
            hospitalAddress.text = hospital.address
            runningInfo.text = if (hospital.isRunningNow()) "운영중" else "진료 종료"
            runningTime.text =
                "오늘 ${hospital.parseRunningTimeAt(Calendar.getInstance().getTodayOfWeek())}"
        }

        with(timeInfoBinding){
            mondayTime.text     = hospital.parseRunningTimeAt(Calendar.MONDAY)
            tuesdayTime.text    = hospital.parseRunningTimeAt(Calendar.TUESDAY)
            wednesdayTime.text  = hospital.parseRunningTimeAt(Calendar.WEDNESDAY)
            thursdayTime.text   = hospital.parseRunningTimeAt(Calendar.THURSDAY)
            fridayTime.text     = hospital.parseRunningTimeAt(Calendar.FRIDAY)
            saturdayTime.text   = hospital.parseRunningTimeAt(Calendar.SATURDAY)
            sundayTime.text     = hospital.parseRunningTimeAt(Calendar.SUNDAY)
            holidayTime.text    = hospital.parseRunningTimeAt(-1)
        }

        with(locationInfoBinding){
            positAddress.text   = hospital.detailAddress.ifEmpty { hospital.address }
        }

        with(callInfoBinding){
            hospitalPhone.text  = hospital.tel
        }
    }

    /**
     * 요일별 진료 시간을 반환하는 메소드
     */
    private fun Hospital.parseRunningTimeAt(dayOfWeek: Int): String {
        val time = this.getRunnginTimeAt(dayOfWeek)
        if(time.first.isEmpty() || time.second.isEmpty()) return "휴진"

        val startTime = "${time.first.substring(0..1)}:${time.first.substring(2..3)}"
        val endTime = "${time.second.substring(0..1)}:${time.second.substring(2..3)}"

        return "$startTime ~ $endTime"
    }

    fun setBottomSheetBehavior(behavior: BottomSheetBehavior<ConstraintLayout>){
         this.behavior = behavior
         this.behavior?.addBottomSheetCallback(bottomSheetCallback)

    }

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) = Unit

        override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
    }

    /**
     * NestedScrollView 스크롤 리스너
     */
    private val scrollChangedListener = View.OnScrollChangeListener { view, scorllX, scrollY, oldScrollX, oldScrollY ->
        bind.ssvContentScrollView.apply {
            when {
                scrollY < (computeDistanceToView(locationInfoBinding.clHospitalPositInfo) / 2)-> {
                    bind.tabLayout.setScrollPosition(0, 0f, true)
                }
                scrollY < (computeDistanceToView(reviewInfoBinding.clHospitalReviewInfo) / 2) -> {
                    bind.tabLayout.setScrollPosition(1, 0f, true)
                }
                else ->{
                    bind.tabLayout.setScrollPosition(2, 0f, true)
                }
            }
        }
    }

    /**
     * Tab 선택 리스너
     */
    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
        override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        override fun onTabSelected(tab: TabLayout.Tab?) {
            with(bind.ssvContentScrollView){
                when (tab?.position) {
                    0 -> smoothScrollToView(timeInfoBinding.clTimeInfoContainer)
                    1 -> smoothScrollToView(locationInfoBinding.clHospitalPositInfo)
                    2 -> smoothScrollToView(reviewInfoBinding.clHospitalReviewInfo)
                }
            }
        }
    }
}