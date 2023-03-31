package com.example.smiley.info.fragment


import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.smiley.R
import com.example.smiley.common.extension.showViewThenCheckedChanged
import com.example.smiley.common.extension.showViewThenEnterPressed
import com.example.smiley.common.extension.visible
import com.example.smiley.databinding.FragmentMedicalInfoBinding
import com.example.smiley.info.ButtonClickable
import com.example.smiley.info.InfoActivity
import java.time.LocalDate


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicalInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicalInfoFragment : Fragment(), ButtonClickable {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind: FragmentMedicalInfoBinding
    private lateinit var radioBtnList:ArrayList<RadioGroup>
    private lateinit var editTextList:ArrayList<EditText>
    private lateinit var nextBtn:Button


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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_medical_info, container, false)
        bind.apply {
            radioBtnList = arrayListOf(
                answer1Radio,
                answer2Radio,
                answer3Radio,
                answer4Radio,
            )

            editTextList = arrayListOf(
                answer2SubEditText,
                answer3SubEditText,
            )
        }

        initNextBtn()
        addAnswerClickEvent()
        addKeyPressEventToEditText()

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
     * 라디오 버튼 클릭 이벤트
     */
    private fun addAnswerClickEvent(){
        bind.apply {
            answer1Radio.showViewThenCheckedChanged(question2Layout, null, null) { setButtonStatus() }
            answer2Radio.showViewThenCheckedChanged(answer2SubQuestion, question3Layout) { setButtonStatus() }
            answer3Radio.showViewThenCheckedChanged(answer3SubQuestion, question4Layout) { setButtonStatus() }
            answer4Radio.setOnCheckedChangeListener { _, _ -> setButtonStatus() }
        }
    }

    /**
     * EditText 엔터키 이벤트
     */
    private fun addKeyPressEventToEditText() {
        bind.apply {
            answer2SubEditText.showViewThenEnterPressed(question3Layout, scrollView) { setButtonStatus() }
            answer3SubEditText.showViewThenEnterPressed(question4Layout, scrollView) { setButtonStatus() }
        }
    }


    override fun setButtonStatus(){
        if(::nextBtn.isInitialized) nextBtn.isEnabled = isAllInputCompleted()
    }

    private fun isAllInputCompleted(): Boolean {
        if(!::radioBtnList.isInitialized || !::editTextList.isInitialized) return false
        
        radioBtnList.forEach {
            if (it.checkedRadioButtonId < 0) return false
        }

        editTextList.forEach {
            if (it.isShown && (it.error != null || it.text.isEmpty())) return false
        }
        return true
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedicalInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MedicalInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}