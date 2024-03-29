package com.example.smiley.info.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.bluetooth.fragment.BluetoothSearchFragment
import com.example.smiley.common.extension.addFragment
import com.example.smiley.common.extension.changeActivity
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.extension.showLottieGenericDialog
import com.example.smiley.common.extension.toDate
import com.example.smiley.common.extension.visibleWithAnimation
import com.example.smiley.common.utils.DataSendable
import com.example.smiley.databinding.FragmentCalibrationInfoBinding
import com.example.smiley.hospital.HospitalSearchFragment
import com.example.smiley.main.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.time.LocalDate

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalibrationInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalibrationInfoFragment : Fragment(), DataSendable {
    private var _bind:FragmentCalibrationInfoBinding? = null
    private val bind get() = _bind!!

    private var dateSpinnerDialog: BottomSheetDialog? = null

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
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_calibration_info, container, false)

        initSpinner()
        initDateEditText()
        initHospitalEditText()
        initConfirmBtn()

        return bind.root
    }

    /**
     * 병원명 입력 EditText 초기화
     */
    private fun initHospitalEditText(){
        with(bind){
            hospitalEditText.setOnClickListener {
                val hospitalSearchFragment = HospitalSearchFragment()
                hospitalSearchFragment.dataSendable = this@CalibrationInfoFragment
                this@CalibrationInfoFragment.addFragment(hospitalSearchFragment)
            }
            hospitalEditText.addTextChangedListener(hospitalTextWatcher)
        }
    }

    /**
     * 교정 시작일 입력 EditText 초기화
     */
    private fun initDateEditText(){
        with(bind){
            dateEditText.setOnClickListener{
                dateSpinnerDialog?.show()
            }
            dateEditText.addTextChangedListener(dateTextWatcher)
        }
    }

    /**
     * 확인 버튼 초기화 메소드
     */
    private fun initConfirmBtn() {
        bind.confirmBtn.setOnClickListener {
            if (isAllInputCompleted()) {
                requireActivity().showLottieGenericDialog(
                    title = getString(R.string.dialog_title_info_complete),
                    content = getString(R.string.dialog_message_add_device),
                    subContent = getString(R.string.dialog_message_setting_device),
                    lottieView = R.raw.complete,
                    confirmText = getString(R.string.dialog_button_resist),
                    cancleText = getString(R.string.dialog_button_later),
                    confirmListener = { this.addFragment(BluetoothSearchFragment()) },
                    cancleListner = { requireActivity().changeActivity(MainActivity::class.java) }
                )
            }
        }
    }
    
    /**
     * 확인 버튼 초기화 메소드
     */
    private fun isAllInputCompleted(): Boolean {
        val selectDate = bind.dateEditText.text.toString().toDate()
        val today = LocalDate.now()

        if (bind.hospitalEditText.text.isBlank() || bind.dateEditText.text.isBlank()) {
            requireActivity().showConfirmDialog(
                getString(R.string.dialog_title_confirm_input),
                getString(R.string.dialog_error_no_empty_space)
            )
            return false
        }

        if (today < selectDate) {
            requireActivity().showConfirmDialog(
                getString(R.string.dialog_title_confirm_input),
                getString(R.string.dialog_error_check_calibration_start_date)
            )
            return false
        }

        return true
    }

    /**
     * DateSpinner 초기화
     */
    private fun initSpinner() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_date_spinner, null)
        dateSpinnerDialog = BottomSheetDialog(requireActivity()).apply {
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
                bind.dateEditText.setText(String.format("%d-%02d-%02d", year, month+1, day))
            }

            /** DateSpinner 선택 완료 버튼 이벤트 */
            val okBtn = bottomSheetView.findViewById<Button>(R.id.ok_btn)
            okBtn.setOnClickListener {
                this.dismiss()
            }
        }

        bind.dateEditText.setOnClickListener {
            dateSpinnerDialog?.show()
        }
    }

    override fun <T> sendData(data: T) {
        Log.d("교정정보", "DataSendable : $data")
        bind.hospitalEditText.setText(data.toString())
    }

    private val dateTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(input.isNotEmpty()){
                with(bind){
                    confirmBtn.visibleWithAnimation()
                }
            }
        }
    }

    private val hospitalTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(input.isNotEmpty()){
                with(bind){
                    dateLayout.visibleWithAnimation()
                    dateSpinnerDialog?.show()
                    titleTextView.text = "교정 시작 일을\n입력해 주세요."
                }
            }
        }
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
            CalibrationInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}