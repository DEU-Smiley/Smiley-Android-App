package com.example.smiley.main.reserv.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.NetworkError
import com.example.domain.common.base.ResponseState
import com.example.domain.reservation.model.ReservList
import com.example.domain.reservation.usecase.GetReservHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservHistoryViewModel @Inject constructor(
    private val getReservHistoryUseCase: GetReservHistoryUseCase
): ViewModel() {
    private val _state = MutableStateFlow<ReservHistoryFragmentState>(ReservHistoryFragmentState.Init)
    val state:StateFlow<ReservHistoryFragmentState> = _state

    private fun setLoading(){
        _state.value = ReservHistoryFragmentState.IsLoading
    }

    private fun setToast(message: String){
        _state.value = ReservHistoryFragmentState.ShowToast(message)
    }

    private fun setSuccess(reservs: ReservList){
        _state.value = ReservHistoryFragmentState.SuccessReservs(reservs)
    }

    private fun setError(error: NetworkError){
        _state.value = ReservHistoryFragmentState.Error(error)
    }

    /**
     * 이전 예약 내역을 호출하는 메소드
     */
    fun requestReservHistory(){
        viewModelScope.launch(Dispatchers.IO) {
            getReservHistoryUseCase()
                .onStart { setLoading() }
                .catch {
                    setToast(it.message.toString())
                    Log.e("예약 조회 에러", it.message.toString())
                }
                .collect{ state ->
                    when(state) {
                        is ResponseState.Success -> {
                            setSuccess(state.data)
                        }
                        is ResponseState.Error -> {
                            setError(state.error)
                        }
                    }
                }
        }
    }
}

sealed class ReservHistoryFragmentState {
    object Init : ReservHistoryFragmentState()
    object IsLoading : ReservHistoryFragmentState()
    data class ShowToast(val message:String) : ReservHistoryFragmentState()
    data class SuccessReservs(val reservs: ReservList) : ReservHistoryFragmentState()
    data class Error(val error:NetworkError) : ReservHistoryFragmentState()
}