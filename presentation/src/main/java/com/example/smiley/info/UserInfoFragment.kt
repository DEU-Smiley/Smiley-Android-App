package com.example.smiley.info

import android.icu.text.IDNA.Info
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import com.example.smiley.R
import com.example.smiley.databinding.FragmentUserInfoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserInfoFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bind:FragmentUserInfoBinding
    private lateinit var nextBtn: Button
    private val editTextList = arrayListOf<EditText>()

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
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info, container, false)
        bind.apply {
            lifecycleOwner = requireActivity()
            editTextList.apply {
                add(nameEditText)
                add(birthEditText)
                add(emailEditText)
                add(phoneEditText)
            }
        }

        initNextBtn()
        addTextWatcherToEditText()
        addRadioGroupClickEvent()

        return bind.root
    }

    private fun addRadioGroupClickEvent(){
        bind.genderRadio.setOnCheckedChangeListener { _, _ ->
            isAllInputComplete()
        }
    }

    private fun initNextBtn(){
        nextBtn = (activity as InfoActivity).getNextButton()
        nextBtn.isEnabled = false
    }

    /**
     * 모든 EditText에 형식에 맞는 입력이 들어왔는지 확인
     */
    private fun isAllInputComplete() : Boolean {
        if(!bind.maleRadioBtn.isChecked && !bind.femaleRadioBtn.isChecked) {
            nextBtn.isEnabled = false
            return false
        }

        editTextList.forEach{
            if(it.error != null || it.text.isEmpty()) {
                nextBtn.isEnabled = false
                return false
            }
        }

        nextBtn.isEnabled = true
        return true
    }

    /**
     * EditText에 TextWatcher 등록
     */
    private fun addTextWatcherToEditText(){
        bind.apply {
            nameEditText.addTextChangedListener(baseTextWatcher)
            birthEditText.addTextChangedListener(birthDayTextWatcher)
            emailEditText.addTextChangedListener(emailTextWatcher)
            phoneEditText.addTextChangedListener(phoneNumberTextWatcher)
        }
    }

    /**
     * 기본 TextWatcher (타입 체크가 필요 없는 EditText에 적용)
     */
    private val baseTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            // 모든 edittext가 정상적으로 입력 되었는지 확인
            isAllInputComplete()
        }
    }


    /**
     * 생년월일 형식 체크 TextWatcher
     */
    private val birthDayTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(!input.matches(Regex("^\\d{8}"))){
                bind.birthEditText.error = "올바르지 않은 형식입니다."
            } else {
                bind.birthEditText.error = null
            }
            isAllInputComplete()
        }
    }

    /**
     * 이메일 형식 체크 TextWatcher
     */
    private val emailTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val pattern = Patterns.EMAIL_ADDRESS

            if(!pattern.matcher("$p0").matches()){
                bind.emailEditText.error = "올바르지 않은 이메일 형식입니다."
            }
            else{
                bind.emailEditText.error = null
            }
            isAllInputComplete()
        }
    }

    /**
     * 전화번호 형식 체크 TextWatcher
     */
    private val phoneNumberTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(input.matches(Regex("^\\d{3}-\\d{4}-\\d{4}\$"))){
                bind.phoneEditText.error = "- 없이 입력해주세요"
            }
            else if(!input.matches(Regex("^[0-9]{11}$"))) {
                bind.phoneEditText.error = "올바르지 않은 형식입니다."
            }
            else{
                bind.phoneEditText.error = null
            }
            isAllInputComplete()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}