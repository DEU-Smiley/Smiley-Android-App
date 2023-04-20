package com.example.smiley.hospital

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.hospital.model.SimpleHospitalList
import com.example.smiley.R
import com.example.smiley.common.dialog.LoadingDialog
import com.example.smiley.common.extension.*
import com.example.smiley.common.utils.DataSendable
import com.example.smiley.databinding.FragmentHospitalSearchBinding
import com.example.smiley.hospital.adapter.HospitalFilterAdapter
import com.example.smiley.hospital.viewmodel.HospitalSearchFragmentState
import com.example.smiley.hospital.viewmodel.HospitalViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HospitalSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HospitalSearchFragment : Fragment(), DataSendable{
    private var param1: String? = null
    private var param2: String? = null

    private var _bind: FragmentHospitalSearchBinding? = null
    private val bind get() = _bind!!
    private val hospitalVm: HospitalViewModel by viewModels()

    /* 병원 검색시 사용될 Adapter */
    private lateinit var hospitalFilterAdapter: HospitalFilterAdapter

    /* 부모 프래그먼트로 선택 약품 리스트를 전달하기 위한 인터페이스 */
    lateinit var dataSendable: DataSendable

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
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_hospital_search, container, false)

        observe()
        fetchHospitalList()
        initSearchEditText()
        addBackBtnClickEvent()

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
     * 병원 리스트 조회 요청
     */
    private fun fetchHospitalList() {
        hospitalVm.requestAllHospitals()
    }

    /**
     * 병원 검색 EditText 초기화
     */
    private fun initSearchEditText() {
        bind.searchEditText.addTextChangedListener(filterTextWatcher)
    }

    /**
     * 뒤로가기 버튼 이벤트
     */
    private fun addBackBtnClickEvent(){
        bind.backBtn.setOnClickListener {
            this@HospitalSearchFragment.dismiss()
        }
    }

    /**
     * 검색어 입력 체크 TextWatcher
     */
    private val filterTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if(!::hospitalFilterAdapter.isInitialized) return
            hospitalFilterAdapter.filter.filter(p0)
        }
    }

    /**
     * 리사이클러뷰 초기화
     */
    private fun initRecyclerView(simpleHospitalList: SimpleHospitalList) {
        hospitalFilterAdapter = HospitalFilterAdapter(simpleHospitalList).apply {
            setOnItemClickListener(hospitalItemClickListener)
        }

        bind.searchResultView.apply {
            adapter = hospitalFilterAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    /**
     * ViewModel 상태 핸들러
     */
    private fun handleStateChange(state: HospitalSearchFragmentState){
        when(state){
            is HospitalSearchFragmentState.Init -> Unit
            is HospitalSearchFragmentState.IsLoading -> handleLoading(state.isLoading)
            is HospitalSearchFragmentState.SuccessLoadHospital ->{
                handleSucccessHospital(state.simpleHospitalList)
                handleLoading(false)
            }
            is HospitalSearchFragmentState.ErrorLoadHospital -> handleErrorHospital(state.error)
            is HospitalSearchFragmentState.ShowToast -> handleShowToast(state.message)
        }
    }

    /**
     * HospitalList 조회에 성공한 경우의 핸들러
     */
    private fun handleSucccessHospital(simpleHospitalList: SimpleHospitalList){
        initRecyclerView(simpleHospitalList)
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
    private lateinit var loadingDialog: LoadingDialog
    private fun handleLoading(isLoding: Boolean){
        if(!::loadingDialog.isInitialized) return

        if(isLoding) loadingDialog.show()
        else loadingDialog.dismiss()
    }

    /**
     * ToastMessage 핸들러
     */
    private fun handleShowToast(message: String){
        requireActivity().showToast(message)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        loadingDialog = LoadingDialog(context)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bind = null
    }

    /**
     * 부모 프래그먼트로 데이터 전달
     * 교정 정보 입력 페이지 <- 검색 페이지 <- 바텀 시트 순서로 데이터를 반환해야 함
     * 병원 검색 페이지에선 바텀 시트의 결과를 교정 정보 입력 페이지로 전달하는 역할만 수행
     */
    override fun <T> sendData(data: T) {
        Log.d("검색페이지", "DataSendable : $data")
        dataSendable.sendData(data)
        this.dismiss()
    }

    /**
     * 검색 결과 병원 클릭 이벤트 리스너
     */
    private val hospitalItemClickListener = object : HospitalFilterAdapter.OnItemClickListener {
        @SuppressLint("InflateParams")
        override fun onItemClicked(position: Int, data: String) {
            val bundle = Bundle()
            bundle.putString("hpid", data)
            HospitalInfoBottomSheetFragment().apply {
                arguments = bundle
                this.dataSendable = this@HospitalSearchFragment
            }.show(parentFragmentManager, HospitalInfoBottomSheetFragment.TAG)
        }
    }

    companion object {
        const val TAG = "HosptialSearchFragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HospitalSearchFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HospitalSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}