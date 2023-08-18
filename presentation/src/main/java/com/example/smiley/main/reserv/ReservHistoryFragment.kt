package com.example.smiley.main.reserv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.common.base.NetworkError
import com.example.domain.reservation.model.ReservList
import com.example.smiley.R
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.showConfirmDialog
import com.example.smiley.common.extension.showToast
import com.example.smiley.common.extension.visible
import com.example.smiley.databinding.FragmentReservHistoryBinding
import com.example.smiley.main.reserv.adapter.ReservHistoryAdapter
import com.example.smiley.main.reserv.viewmodel.ReservHistoryFragmentState
import com.example.smiley.main.reserv.viewmodel.ReservHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservHistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ReservHistoryFragment : Fragment() {
    private var _bind: FragmentReservHistoryBinding?=null
    private val bind:FragmentReservHistoryBinding get() = _bind!!

    private val historyVm: ReservHistoryViewModel by viewModels()
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
        _bind = DataBindingUtil.inflate(inflater, R.layout.fragment_reserv_history, container, false)

        observe()
        requestReservHistory()
        initSwipeRefreshLayout()

        return bind.root
    }

    private fun observe(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                historyVm.state.collect { state ->
                    when(state){
                        is ReservHistoryFragmentState.Init -> Unit
                        is ReservHistoryFragmentState.IsLoading -> handleLoading(true)
                        is ReservHistoryFragmentState.ShowToast -> handleShowToast(state.message)
                        is ReservHistoryFragmentState.SuccessReservs -> handleSuccess(state.reservs)
                        is ReservHistoryFragmentState.Error -> handleError(state.error)
                    }
                }
            }
        }
    }

    /**
     * 예약 내역을 요청하는 메소드
     */
    private fun requestReservHistory(){
        historyVm.requestReservHistory()
    }

    private fun initRecylcerView(reservs: ReservList){
        bind.reservHistoryView.apply {
            adapter = ReservHistoryAdapter(reservs)

            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initSwipeRefreshLayout(){
        bind.swipeRefreshLayout.setOnRefreshListener {
            requestReservHistory()
            // 로딩 아이콘 제거
            bind.swipeRefreshLayout.isRefreshing = false
        }
    }
    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            bind.loadingLayout.visible()
        } else{
            bind.loadingLayout.gone()
        }
    }

    private fun handleShowToast(message:String){
        requireActivity().showToast(message)
    }
    private fun handleSuccess(reservs:ReservList){
        handleLoading(false)
        initRecylcerView(reservs)
    }

    private fun handleError(error:NetworkError){
        handleLoading(false)
        requireActivity().showConfirmDialog(
            title = "예약 조회 오류",
            content = error.message,
            subContent = "(화면을 아래로 쓸어 내려 다시 시도해 주세요.)",
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ReservHistoryFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReservHistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}