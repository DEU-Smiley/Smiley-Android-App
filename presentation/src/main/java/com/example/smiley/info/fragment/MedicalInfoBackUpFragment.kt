package com.example.smiley.info.fragment


import android.os.Bundle
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
import com.example.smiley.common.extension.showViewThenEnterPressed
import com.example.smiley.common.extension.showViewThenTextChanged
import com.example.smiley.common.utils.DataSendable
import com.example.smiley.databinding.FragmentMedicalInfoBackUpBinding
import com.example.smiley.info.ButtonClickable
import com.example.smiley.info.InfoActivity
import com.example.smiley.medicine.MedicineSearchFragment
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedicalInfoBackUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedicalInfoBackUpFragment : Fragment(), ButtonClickable, DataSendable {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind: FragmentMedicalInfoBackUpBinding
    private lateinit var radioBtnList:ArrayList<RadioGroup>
    private lateinit var subQuestionLayout:ArrayList<LinearLayout>
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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_medical_info_back_up, container, false)

        init()
        addEvent()

        return bind.root
    }
    
    private fun init(){
        initLayoutList()
        initNextBtn()
    }

    private fun addEvent(){
        addAnswerClickEvent()
        addKeyPressEventToEditText()
        addMedicineEditTextClickEvent()
    }

    /**
     * 약품 입력 EditText 클릭 이벤트
     */
    private fun addMedicineEditTextClickEvent(){
        bind.answer2SubTextView.setOnClickListener {
            val medicineSearchFragment = MedicineSearchFragment()
            medicineSearchFragment.dataSendable = this
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.parent_layout, medicineSearchFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initLayoutList(){
        bind.apply {
            radioBtnList = arrayListOf(
                answer1Radio,
                answer2Radio,
                answer3Radio,
                answer4Radio,
            )

            subQuestionLayout = arrayListOf(
                answer2SubQuestion,
                answer3SubQuestion
            )
        }
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
            answer2SubTextView.showViewThenTextChanged(question3Layout, scrollView) { setButtonStatus() }
            answer3SubEditText.showViewThenEnterPressed(question4Layout, scrollView) { setButtonStatus() }
        }
    }


    override fun setButtonStatus(){
        if(::nextBtn.isInitialized) nextBtn.isEnabled = isAllInputCompleted()
    }

    private fun isAllInputCompleted(): Boolean {
        radioBtnList.forEach {
            if (it.checkedRadioButtonId < 0) return false
        }

        subQuestionLayout.forEach { layout ->
            val editText = layout.children
                .filter { it is TextView || it is EditText }
                .map { it as TextView }
                .toList()

            if(layout.isVisible){
                editText.forEach {
                    if(it.error != null || it.text.isEmpty() || it.text.isBlank()) return false
                }
            }
        }

        return true
    }

    override fun <T> sendData(data: T) {
        val sj = StringJoiner(", ")
        (data as ArrayList<String>).forEach {
            sj.add(it)
        }

        bind.answer2SubTextView.text = sj.toString()
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
            MedicalInfoBackUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}