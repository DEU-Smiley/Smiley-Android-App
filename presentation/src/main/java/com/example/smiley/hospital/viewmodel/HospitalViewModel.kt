package com.example.smiley.hospital.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.model.SimpleHospitalList
import com.example.domain.hospital.usecase.GetAllHospitalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val getAllHospitalUseCase: GetAllHospitalUseCase
) : ViewModel() {
    private var _state = MutableStateFlow<HospitalSearchFragmentState>(HospitalSearchFragmentState.Init)
    val state : StateFlow<HospitalSearchFragmentState> get() = _state

    private fun setLoading(){
        _state.value = HospitalSearchFragmentState.IsLoading
    }

    private fun showToast(message: String){
        _state.value = HospitalSearchFragmentState.ShowToast(message)
    }

    fun requestAllHospitals(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllHospitalUseCase()
                .onStart { setLoading() }
                .catch { exception ->
                    showToast(exception.message.toString())
                    Log.d("병원 조회 에러", exception.stackTraceToString())
                }
                .collect{ state ->
                    when(state) {
                        is ResponseState.Success -> {
                            _state.value = HospitalSearchFragmentState.SuccessLoadHospital(state.data)
                        }
                        is ResponseState.Error -> {
                            _state.value = HospitalSearchFragmentState.ErrorLoadHospital(state.error.message)
                        }
                    }
                }
        }
    }
}

sealed class HospitalSearchFragmentState {
    object Init : HospitalSearchFragmentState()
    object IsLoading : HospitalSearchFragmentState()
    data class SuccessLoadHospital(val simpleHospitalList: SimpleHospitalList) : HospitalSearchFragmentState()
    data class ErrorLoadHospital(val error: String) : HospitalSearchFragmentState()
    data class ShowToast(val message: String) : HospitalSearchFragmentState()
}