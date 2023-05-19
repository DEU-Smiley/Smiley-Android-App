package com.example.smiley.hospital.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.model.HospitalPositList
import com.example.domain.hospital.usecase.GetNearByHospitalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HospitalMapViewModel  @Inject constructor(
    private val getNearByHospitalUseCase: GetNearByHospitalUseCase
) : ViewModel() {
    private var _state = MutableStateFlow<HospitalMapFragmentState>(HospitalMapFragmentState.Init)
    val state : StateFlow<HospitalMapFragmentState> get() = _state

    private fun setLoading(){
        _state.value = HospitalMapFragmentState.IsLoading
    }

    private fun showError(message: String){
        _state.value = HospitalMapFragmentState.Error(message)
    }

    private fun showToast(message: String){
        _state.value = HospitalMapFragmentState.ShowToast(message)
    }
    /**
     * 현재 위치에서 반경 n미터 내의 병원을 가져오는 메소드
     *
     * @param lat Double
     * @param lng Double
     * @param dis Double (1 = 1km)
     */
    fun getNearByHospitals(
        lat: Double,
        lng: Double,
        dis: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getNearByHospitalUseCase(lat, lng, dis)
                .onStart { setLoading() }
                .catch { exception ->
                    showToast(exception.message.toString())
                    Log.d("병원 조회 에러", exception.stackTraceToString())
                }
                .collect { state ->
                    when (state) {
                        is ResponseState.Success -> {
                            _state.value = HospitalMapFragmentState.SuccessLoadHospital(state.data)
                        }
                        is ResponseState.Error -> {
                            showError(state.error.message)
                            _state.value = HospitalMapFragmentState.Error(state.error.message)
                        }
                    }
                }
        }
    }
}

sealed class HospitalMapFragmentState {
    object Init : HospitalMapFragmentState()
    object IsLoading : HospitalMapFragmentState()
    data class SuccessLoadHospital(
        val hospitalPositList: HospitalPositList
    ) : HospitalMapFragmentState()
    data class Error(val error: String) : HospitalMapFragmentState()
    data class ShowToast(val message: String) : HospitalMapFragmentState()
}