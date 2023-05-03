package com.example.smiley.bluetooth.fragment

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.nfc.Tag
import android.os.Bundle
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
import com.example.smiley.R
import com.example.smiley.bluetooth.adapter.BluetoothSearchAdapter
import com.example.smiley.bluetooth.viewmodel.BluetoothSearchFragmentState
import com.example.smiley.bluetooth.viewmodel.BluetoothViewModel
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.extension.visible
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.databinding.FragmentBluetoothSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BluetoothSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class BluetoothSearchFragment : Fragment() {

    private var _bind: FragmentBluetoothSearchBinding?=null
    private val bind get() = _bind!!

    private val bluetoothVm: BluetoothViewModel by viewModels()
    private val bluetoothSearchAdapter: BluetoothSearchAdapter by lazy {
        BluetoothSearchAdapter(listOf())
    }

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
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_bluetooth_search, container, false)
        observe()
        init()

        return bind.root
    }

    private fun observe(){
        bluetoothVm.state.flowWithLifecycle(
            viewLifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        ).onEach { state ->
            handleStateChanged(state)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    /**
     * 뷰 초기화 메소드
     */
    private fun init(){
        bind.searchResultView.apply {
            adapter = bluetoothSearchAdapter.apply {
                setOnItemClickListener(btItemClickListener)
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        bluetoothVm.startScan()
    }

    private fun handleStateChanged(state:BluetoothSearchFragmentState){
        Log.d("옵저버", "옵저버 동작 state : $state")
        when(state){
            is BluetoothSearchFragmentState.Init -> {
                // 초기 화면으로 돌리기
                // 여기서 Adapter의 리스트가 0이면 검색 결과 없음을 표시
                // 0이 아니면 검색 결과 리스트 표시
                handleInitial()
            }
            is BluetoothSearchFragmentState.Error -> {
                handleError(state.message)
            }
            is BluetoothSearchFragmentState.SuccessConnect -> {
                // 장치 연결이 완료된 경우
            }
            is BluetoothSearchFragmentState.IsConnecting -> {
                // 장치 연결 중인 경우
            }
            is BluetoothSearchFragmentState.IsScanning -> {
                handleScanning(state.scanResults)
            }
            is BluetoothSearchFragmentState.RequiredPermission -> Unit
            is BluetoothSearchFragmentState.FailedScan -> {
                // 스캔에 실패한 경우 (못 찾은 경우가 아닌 에러가 발생한 경우)
                handleError(state.message)
            }
        }
    }

    /**
     * 블루투스 화면의 초기 상태 지정
     * Adapter의 리스트가 0이면 검색 결과 없음을 표시
     * 0이 아니면 검색 결과 리스트 표시
     */
    private fun handleInitial(){
        with(bind){
            if(bluetoothSearchAdapter.scanResults.isEmpty()){
                titleTextView.text = "검색된 장치가 없습니다."
                subTextView.text = "블루투스와 위치 기능이 켜져 있는지 확인해주세요."
                notFoundView.visible()
                searchResultView.gone()
                // Not Found 아이콘까지 표시해주기
            } else {
                titleTextView.text = "장치를 선택해주세요."
                subTextView.text = "장치의 이름과 고유 번호를 확인해주세요."
                notFoundView.gone()
                searchResultView.visible()
            }
            searchLoadingView.gone()
        }

        // 버튼을 다시 찾기 모드로 변경
        setBtnToRefindState()
    }

    /**
     * 스캔 중인 경우의 핸들러
     * 스캔 중에 검색되는 장치들을 리사이클러뷰에 보여줌
     */
    private fun handleScanning(scanResults: Set<BluetoothDevice>){
        bluetoothSearchAdapter.setSearchResults(scanResults.toList())
        with(bind) {
            titleTextView.text = "장치를 찾는 중입니다..."
            subTextView.text = "블루투스와 위치 기능이 켜져 있는지 확인해주세요."
            searchLoadingView.visible()
            searchResultView.visible()
            notFoundView.gone()
        }

        // 버튼을 취소 가능한 상태로 변경
        setBtnToCancleState()
    }

    /**
     * 장치에 연결 중일 떄의 핸들러
     */
    private fun handleIsConnecting(){
        with(bind){

        }
    }

    /**
     * 에러 발생 핸들러
     */
    private fun handleError(message:String){
        requireActivity().showConfirmDialog(
            title = "블루투스 오류",
            content = message,
        )
    }


    /**
     * 버튼을 취소 가능 버튼으로 변경
     */
    private fun setBtnToCancleState(){
        bind.bluetoothBtn.apply {
            background = resources.getDrawable(R.drawable.selector_white_btn)
            setTextColor(resources.getColor(R.color.black1_20))
            text = "취소하기"

            setOnClickListener {
                bluetoothVm.stopScan()
            }
        }
    }
    /**
     * 버튼을 다시 찾기 모드로 변경
     */
    private fun setBtnToRefindState(){
        bind.bluetoothBtn.apply {
            background = resources.getDrawable(R.drawable.selector_btn)
            setTextColor(resources.getColor(R.color.white))
            text = "다시 찾기"

            setOnClickListener {
                bluetoothVm.startScan()
            }
        }
    }

    private val btItemClickListener = object : OnItemClickListener<BluetoothDevice>{
        @SuppressLint("MissingPermission")
        override fun onItemClicked(position: Int, data: BluetoothDevice) {
            // 연결 메소드
            bluetoothVm.connectToDevice(data.address)

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BluetoothSearchFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BluetoothSearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}