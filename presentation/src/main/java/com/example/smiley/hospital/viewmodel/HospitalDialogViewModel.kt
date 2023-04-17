package com.example.smiley.hospital.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.model.Hospital
import com.example.domain.hospital.model.SimpleHospitalList
import com.example.domain.hospital.usecase.GetHospitalByHpidUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.exp

@HiltViewModel
class HospitalDialogViewModel @Inject constructor(
    private val getHospitalByHpidUseCase: GetHospitalByHpidUseCase
) : ViewModel() {
    private var _state = MutableStateFlow<HospitalDialogState>(HospitalDialogState.Init)
    val state : StateFlow<HospitalDialogState> get() = _state

    private fun setLoading(){
        _state.value = HospitalDialogState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = HospitalDialogState.IsLoading(false)
    }

    private fun showToast(message: String){
        _state.value = HospitalDialogState.ShowToast(message)
    }

    fun requestHospital(hpid:String){
        viewModelScope.launch(Dispatchers.IO) {
            getHospitalByHpidUseCase(hpid)
                .onStart { setLoading() }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                    Log.d("병원 조회 에러", exception.stackTraceToString())
                }
                .collect{ state ->
                    hideLoading()
                    when(state){
                        is ResponseState.Success -> {
                            _state.value = HospitalDialogState.SuccessLoadHospital(state.data)
                        }
                        is ResponseState.Error -> {
                            _state.value = HospitalDialogState.ErrorLoadHospital(state.error.message)
                        }
                    }
                }
        }
    }
}

sealed class HospitalDialogState {
    object Init                                            : HospitalDialogState()
    data class SuccessLoadHospital(val hospital: Hospital) : HospitalDialogState()
    data class ErrorLoadHospital(val error: String)        : HospitalDialogState()
    data class IsLoading(val isLoading: Boolean)           : HospitalDialogState()
    data class ShowToast(val message: String)              : HospitalDialogState()
}