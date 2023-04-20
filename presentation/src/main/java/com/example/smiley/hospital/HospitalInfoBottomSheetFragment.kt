package com.example.smiley.hospital

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Outline
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.domain.hospital.model.Hospital
import com.example.smiley.R
import com.example.smiley.common.extension.*
import com.example.smiley.common.utils.DataSendable
import com.example.smiley.databinding.FragmentHospitalInfoBottomSheetBinding
import com.example.smiley.hospital.viewmodel.HospitalDialogState
import com.example.smiley.hospital.viewmodel.HospitalDialogViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HospitalInfoBottomSheetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HospitalInfoBottomSheetFragment() : BottomSheetDialogFragment() {
    private var hpid: String? = null

    private var _bind: FragmentHospitalInfoBottomSheetBinding?=null
    private val bind get() = _bind!!
    private val hospitalVm: HospitalDialogViewModel by viewModels()
    lateinit var dataSendable: DataSendable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
             hpid = it.getString("hpid")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog: BottomSheetDialog =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener {
            setUpFullHeight(it as BottomSheetDialog) // 바텀 시트 높이 화면 전체로 설정
            addBtnToBottom(dialog, it)               // 바텀 시트 하단에 고정 버튼 추가
        }

        return dialog
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

        observe()
        initCoordinatorLayout()
        initTabLayout()
        initNestedScrollView()
        requestHospital()
        
        return bind.root
    }

    private fun observe(){
        hospitalVm.state.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach { state ->
            handleStateChange(state)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    /**
     * 바텀시트 코너 Radius 지정
     */
    private fun initCoordinatorLayout(){
        bind.coordinatorLayout.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(
                    0,
                    0,
                    view.width,
                    view.height,
                    requireActivity().convertDpToPx(30).toFloat()
                )
            }
        }
        bind.coordinatorLayout.clipToOutline = true
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
    private fun initNestedScrollView(){
        bind.nestedScrollView.setOnScrollChangeListener(scrollChangedListener)
    }

    private fun requestHospital(){
        hpid?.let {
            hospitalVm.requestHospital(it)
        }
    }

    private fun addSelectBtnClickEvent(){

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
        val btnLayout = dialog.layoutInflater.inflate(R.layout.sub_bottom_sheet_button, null)

        btnLayout.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.BOTTOM
        }

        val button = btnLayout.findViewById<Button>(R.id.next_btn)
        button.setOnClickListener {
            if(!::dataSendable.isInitialized) return@setOnClickListener
            Log.d("바텀시트", "버튼 클릭")
            dataSendable.sendData(bind.hospitalTitle.text)
            this@HospitalInfoBottomSheetFragment.dismiss()
        }

        containerLayout!!.addView(btnLayout)
    }

    private fun getWindowHeight(): Int {
        return requireActivity().findViewById<View>(Window.ID_ANDROID_CONTENT).height
    }

    /**
     * ViewModel 상태 핸들러
     */
    private fun handleStateChange(state: HospitalDialogState){
        when(state){
            is HospitalDialogState.Init -> Unit
            is HospitalDialogState.IsLoading -> handleLoading(state.isLoading)
            is HospitalDialogState.SuccessLoadHospital ->{
                handleSucccessHospital(state.hospital)
                handleLoading(false)
            }
            is HospitalDialogState.ErrorLoadHospital -> handleErrorHospital(state.error)
            is HospitalDialogState.ShowToast -> handleShowToast(state.message)
        }
    }

    /**
     * HospitalList 조회에 성공한 경우의 핸들러
     */
    @SuppressLint("SetTextI18n")
    private fun handleSucccessHospital(hospital: Hospital){
        bind.apply {
            partnerTag.text     = if (hospital.isPartner) "제휴" else "일반"
            hospitalTitle.text  = hospital.name
            hospitalAddress.text = hospital.address
            runningInfo.text    = if(hospital.isRunningNow()) "운영중" else "진료 종료"
            runningTime.text    = "오늘 ${hospital.parseRunningTimeAt(Calendar.getInstance().getTodayOfWeek())}"


            mondayTime.text     = hospital.parseRunningTimeAt(Calendar.MONDAY)
            tuesdayTime.text    = hospital.parseRunningTimeAt(Calendar.TUESDAY)
            wednesdayTime.text  = hospital.parseRunningTimeAt(Calendar.WEDNESDAY)
            thursdayTime.text   = hospital.parseRunningTimeAt(Calendar.THURSDAY)
            fridayTime.text     = hospital.parseRunningTimeAt(Calendar.FRIDAY)
            saturdayTime.text   = hospital.parseRunningTimeAt(Calendar.SATURDAY)
            sundayTime.text     = hospital.parseRunningTimeAt(Calendar.SUNDAY)
            holidayTime.text    = hospital.parseRunningTimeAt(-1)

            positAddress.text   = hospital.detailAddress.ifEmpty { hospital.address }
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

    /**
     * HospitalList 조회에 실패한 경우의 핸들러
     */
    private fun handleErrorHospital(error:String){
        requireActivity().showConfirmDialog(
            "병원 조회 에러",
            error
        )
    }

    /**
     * 로딩 다이얼로그 핸들러
     */
    private fun handleLoading(isLoding: Boolean){
        if(isLoding) {
            bind.lodingLayout.visible()
            bind.coordinatorLayout.gone()
        } else {
            bind.lodingLayout.gone()
            bind.coordinatorLayout.visible()
        }
    }

    /**
     * ToastMessage 핸들러
     */
    private fun handleShowToast(message: String){
        requireActivity().showToast(message)
    }

    /**
     * Tab 선택 리스너
     */
    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabUnselected(tab: Tab?) = Unit
        override fun onTabReselected(tab: Tab?) = Unit
        override fun onTabSelected(tab: Tab?) {
            bind.appBarLayout.setExpanded(false, true)
            when (tab?.position) {
                0 -> bind.nestedScrollView.smoothScrollToView(bind.hospitalTimeInfo)
                1 -> bind.nestedScrollView.smoothScrollToView(bind.hospitalPositInfo)
                2 -> bind.nestedScrollView.smoothScrollToView(bind.hospitalReviewInfo)
            }
        }
    }

    /**
     * NestedScrollView 스크롤 리스너
     */
    private val scrollChangedListener = View.OnScrollChangeListener { _, _, scrollY, _, _ ->
        bind.nestedScrollView.apply {
            when {
                scrollY < (computeDistanceToView(bind.hospitalPositInfo) / 2)-> {
                    bind.tabLayout.setScrollPosition(0, 0f, true)
                }
                scrollY < (computeDistanceToView(bind.hospitalReviewInfo) / 2) -> {
                    bind.tabLayout.setScrollPosition(1, 0f, true)
                }
                else ->{
                    bind.tabLayout.setScrollPosition(2, 0f, true)
                }
            }
        }
    }

    companion object {
        const val TAG = "HospitalInfoBottomSheetFragment"
    }
}