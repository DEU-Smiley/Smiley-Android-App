package com.example.smiley.info.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isGone
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.user.model.User
import com.example.smiley.R
import com.example.smiley.common.dialog.LoadingDialog
import com.example.smiley.common.extension.*
import com.example.smiley.databinding.FragmentSignUpBinding
import com.example.smiley.info.viewmodel.InfoViewModel
import com.example.smiley.info.viewmodel.SignUpFragmentState
import com.example.smiley.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _bind:FragmentSignUpBinding?=null
    private val bind get() = _bind!!

    private val infoVm: InfoViewModel by viewModels()
    private val editTextList: ArrayList<EditText> = arrayListOf()

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
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        observe()
        init()
        setNameBtnClickEvent()
        setConfirmBtnClickEvent()
        setTextWatcher()

        return bind.root
    }

    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                infoVm.state.collect { state ->
                    when(state){
                        is SignUpFragmentState.Init -> Unit
                        is SignUpFragmentState.IsLoading -> handleLoading(true)
                        is SignUpFragmentState.ShowToast -> handleShowToast(state.message)
                        is SignUpFragmentState.Error -> handleError(state.message)
                        is SignUpFragmentState.SuccessSendInfo -> handleSuccess(state.user)
                    }
                }
            }
        }
    }

    /**
     * 로딩 다이얼로그 핸들러
     */
    private lateinit var loadingDialog: LoadingDialog
    private fun handleLoading(isLoading: Boolean){
        if(!::loadingDialog.isInitialized) return

        if(isLoading) loadingDialog.show()
        else loadingDialog.dismiss()
    }

    private fun handleShowToast(message:String){
        handleLoading(false)
        requireActivity().showToast(message)
    }

    private fun handleError(message:String){
        handleLoading(false)
        requireActivity().showConfirmDialog(
            "회원가입 오류",
            message,
            "(계속해서 오류가 발생하는 경우 SMILEY 팀으로 문의 부탁드립니다.)",
        )
    }

    private fun handleSuccess(user:User){
        handleLoading(false)
        requireActivity().showLottieGenericDialog(
            "회원가입 완료!",
            "${user.name}님 환영합니다!",
            "교정 치료를 받고 계신가요?\n병원을 등록하면 앱에서 예약할 수 있어요 !",
            confirmText = "등록하러 가기",
            cancleText = "나중에 하기",
            lottieView = R.raw.hospital,
            confirmListener = { this@SignUpFragment.addFragment(CalibrationInfoFragment()) },
            cancleListner = { requireActivity().changeActivity(MainActivity::class.java) }
        )
    }

    private fun init(){
        with(bind){
            setFocusChangeListener(emailEditText, emailTextView)
            setFocusChangeListener(phoneEditText, phoneTextView)
            setFocusChangeListener(birthEditText, birthTextView)
            setFocusChangeListener(zenderEditText, zenderEditText)
            setFocusChangeListener(nameEditText, nameTextView)
            editTextList.addAll(
                listOf(
                    emailEditText,
                    phoneEditText,
                    birthEditText,
                    zenderEditText,
                    nameEditText
                )
            )
        }

        bind.nameEditText.requestFocus()
        requireActivity().window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    private fun setTextWatcher(){
        with(bind){
            nameEditText.addTextChangedListener(nameTextWatcher)
            birthEditText.addTextChangedListener(birthTextWatcher)
            zenderEditText.addTextChangedListener(zenderTextWatcher)
            phoneEditText.addTextChangedListener(phoneTextWatcher)
            emailTextView.addTextChangedListener(emailTextWatcher)
        }
    }

    private fun setFocusChangeListener(editText: EditText, textView:TextView){
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) { // 포커스 얻었을 때
                textView.setTextColor(resources.getColor(R.color.primary_normal))
            } else { // 포커스 잃었을 때
                textView.setTextColor(resources.getColor(R.color.gray2_6E))
            }
        }
    }

    /**
     * 이름 입력 버튼 클릭 이벤트
     */
    private fun setNameBtnClickEvent(){
        bind.nameBtn.setOnClickListener {
            requireActivity().window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN)
            it.goneWithAnimation()
            with(bind){
                confirmBtn.visibleWithAnimation()
                birthLayout.visibleWithAnimation()
                birthEditText.requestFocus()
                signUpTextView.text = "생년월일을\n입력해 주세요."
            }
        }
    }

    /**
     * 확인 버튼 클릭 이벤트
     */
    private fun setConfirmBtnClickEvent(){
        bind.confirmBtn.setOnClickListener {
            if(isAllInputCompleted()){
                infoVm.sendUserInfoToServer(
                    name = "${bind.nameEditText.text}",
                    userId = "${bind.emailEditText.text}",
                    birth = "${bind.birthEditText.text}".toDateOfyyMMdd()
                )
            }
        }
    }

    private val nameTextWatcher = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            with(bind){
                if(input.isBlank()){
                    // 빈 칸이면 버튼 숨김
                    nameBtn.gone()
                } else if(nameBtn.isGone && confirmBtn.isGone){
                    // 빈 칸이 아니고 버튼이 둘 다 안보이는 상태에만 표시
                    nameBtn.visibleWithAnimation()
                }
            }
        }
    }

    private val emailTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) {
            val pattern = Patterns.EMAIL_ADDRESS
            val input = "$p0"

            bind.emailEditText.error =
                if(!pattern.matcher(input).matches()) "올바르지 않은 이메일 형식입니다."
                else null

            if(input.length == 6 && bind.emailEditText.error == null){
                bind.zenderEditText.requestFocus()
            }
        }
    }

    private val birthTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(input.length == 6){
                bind.zenderEditText.requestFocus()
            }
        }
    }

    private val zenderTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(input.length == 1){
                with(bind){
                    phoneLayout.visibleWithAnimation()
                    phoneEditText.requestFocus()
                    signUpTextView.text = "전화번호를\n입력해 주세요."
                }
            }
        }
    }

    private val phoneTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
        override fun afterTextChanged(p0: Editable?) {
            val input = "$p0"

            if(input.length == 11){
                with(bind){
                    emailLayout.visibleWithAnimation()
                    emailEditText.requestFocus()
                    signUpTextView.text = "이메일을\n입력해 주세요."
                }
            }
        }
    }

    private fun isAllInputCompleted(): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS

        editTextList.forEach {
            if(it.text.isBlank()){
                requireActivity().showConfirmDialog("입력 확인","빈 칸 없이 입력해 주세요.")
                return false
            }
        }

        if(!pattern.matcher(bind.emailEditText.text).matches()){
            requireActivity().showConfirmDialog("입력 확인","이메일 형식을 확인해 주세요.")
            return false
        }

        if(bind.phoneEditText.text.length != 11){
            requireActivity().showConfirmDialog("입력 확인","전화번호를 확인해 주세요.")
            return false
        }

        if(bind.birthEditText.text.length != 6){
            requireActivity().showConfirmDialog("입력 확인","생년월일을 확인해 주세요.")
            return false
        }

        try {
            "${bind.birthEditText.text}".toDateOfyyMMdd()
        } catch (e: java.lang.Exception){
            requireActivity().showConfirmDialog("입력 확인","생년월일을 확인해 주세요.")
            return false
        }

        return true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingDialog = LoadingDialog(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignUpFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}