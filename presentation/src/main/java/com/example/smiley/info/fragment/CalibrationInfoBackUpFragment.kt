package com.example.smiley.info.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.common.extension.showViewThenCheckedChanged
import com.example.smiley.common.extension.showViewThenTextChanged
import com.example.smiley.common.extension.toDate
import com.example.smiley.common.utils.DataSendable
import com.example.smiley.databinding.FragmentCalibrationInfoBackUpBinding
import com.example.smiley.hospital.HospitalSearchFragment
import com.example.smiley.info.ButtonClickable
import com.example.smiley.info.InfoActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalibrationInfoBackUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalibrationInfoBackUpFragment : Fragment(), ButtonClickable, DataSendable {

    private lateinit var bind:FragmentCalibrationInfoBackUpBinding
    private lateinit var nextBtn:Button
    private lateinit var questionLayout: ArrayList<LinearLayout>

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_calibration_info_back_up, container, false)

        bind.apply {
            questionLayout = arrayListOf(
                question2Layout,
                question3Layout
            )
        }

        initNextBtn()
        initSpinner()
        validSelectedDate()
        addAnswerClickEvent()
        addKeyPressEventToEditText()
        addHospitalEditTextClickEvent()

        return bind.root
    }

    /**
     * 다음 버튼 초기화
     */
    private fun initNextBtn(){
        nextBtn = (activity as InfoActivity).getNextButton()
        setButtonStatus()
    }

    /**
     * DateSpinner 초기화
     */
    private fun initSpinner() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_date_spinner, null)
        val bottomSheetDialog = BottomSheetDialog(requireActivity()).apply {
            setContentView(bottomSheetView)
            behavior.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                isHideable = true
                skipCollapsed = true
                saveFlags = BottomSheetBehavior.SAVE_ALL
                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if(newState != BottomSheetBehavior.STATE_EXPANDED){
                            behavior.state = BottomSheetBehavior.STATE_EXPANDED
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
                })
            }

            /** DateSpinner 날짜 선택 이벤트 */
            val dateSpinner = bottomSheetView.findViewById<DatePicker>(R.id.date_spinner)
            dateSpinner.setOnDateChangedListener { _, year, month, day ->
                bind.answer3SubTextView.text = String.format("%d-%02d-%02d", year, month+1, day)
            }

            /** DateSpinner 선택 완료 버튼 이벤트 */
            val okBtn = bottomSheetView.findViewById<Button>(R.id.ok_btn)
            okBtn.setOnClickListener {
                this.dismiss()
            }
        }

        bind.answer3SubTextView.setOnClickListener {
            bottomSheetDialog.show()
        }
    }

    /**
     * 질문 응답 클릭 이벤트 (라디오 버튼 )
     */
    private fun addAnswerClickEvent(){
        bind.apply {
            answer1Radio.showViewThenCheckedChanged(
                question2Layout,
                question2TextView,
                listOf(question2Layout, question3Layout)
            ) { setButtonStatus() }
        }
    }

    /**
     * 약품 입력 EditText 클릭 이벤트
     */
    private fun addHospitalEditTextClickEvent(){
        bind.answer2SubTextView.setOnClickListener {
            val hospitalSearchFragment = HospitalSearchFragment()
            hospitalSearchFragment.dataSendable = this
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.parent_layout, hospitalSearchFragment)
                .addToBackStack(null)
                .commit()
        }
    }


    /**
     * EditText 엔터 이벤트 (입력 종료 캐치 )
     */
    private fun addKeyPressEventToEditText() {
        bind.apply {
            answer2SubTextView.showViewThenTextChanged(question3Layout, scrollView) { setButtonStatus() }
        }
    }

    /**
     * 교정 시작 날짜 선택 검증
     */
    private fun validSelectedDate(){
        bind.answer3SubTextView.apply {
            addTextChangedListener(object:TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /* No Implements required */ }
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { /* No Implements required */ }

                override fun afterTextChanged(p0: Editable?) {
                    val selectDate = "$p0".toDate()
                    val today = LocalDate.now()

                    error =
                        if(today < selectDate) "현재보다 이후의 날짜는 선택할 수 없습니다."
                        else null

                    setButtonStatus()
                }
            })
        }
    }

    /**
     * 모든 EditText에 형식에 맞는 입력이 들어왔는지 확인
     */
    override fun setButtonStatus(){
        if(::nextBtn.isInitialized) {
            val result = isAllInputCompleted()
            Log.d("결과", "$result")
            nextBtn.isEnabled = result
        }
    }

    private fun isAllInputCompleted() : Boolean {
        bind.apply {
            if (answer1NoRadioBtn.isChecked) return true

            // 질문 레이아웃의 자식들 중 LinearLayout을 가져옴
            questionLayout.forEach { layout ->
                if(!layout.isVisible) return false
                val subLinearLayout = layout.children.
                    filter { it is LinearLayout }
                    .map { it as LinearLayout }
                    .toList()


                // TextView와 EditText를 가져옴
                // 입력 필드가 EditText와 TextView 모두 존재하기 때문에 TextView로 캐스팅
                // (EditText는 TextView의 자식이므로 TextView로 캐스팅 가능)
                subLinearLayout.forEach { subLayout ->
                    subLayout.children
                        .filter { it is TextView || it is EditText }
                        .map { it as TextView }
                        .forEach {
                            if(it.text.equals("yyyy-mm-dd") || it.error != null || it.text.isBlank()) return false
                        }
                }
            }
        }

        return true
    }

    override fun <T> sendData(data: T) {
        Log.d("교정정보", "DataSendable : $data")
        bind.answer2SubTextView.text = data.toString()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalibrationInfoFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalibrationInfoBackUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}