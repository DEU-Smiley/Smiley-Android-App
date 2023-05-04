package com.example.smiley.medicine.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.medicine.model.MedicineList
import com.example.domain.medicine.usecase.GetAllMedicinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getAllMedicinesUseCase: GetAllMedicinesUseCase
): ViewModel() {

    private val _state = MutableStateFlow<MedicineFragmentState>(MedicineFragmentState.Init)
    val state : StateFlow<MedicineFragmentState> get() = _state

    var medicineList: MedicineList? = null

    private fun setLoading(){
        _state.value = MedicineFragmentState.IsLoading(true)
    }

    private fun hideLoading(){
        _state.value = MedicineFragmentState.IsLoading(false)
    }

    private fun showDialog(message: String){
        _state.value = MedicineFragmentState.ShowDialog(message)
    }

    fun getMedicineList() {
        // 받아온 약품 리스트가 없으면 서버로 요청
        if(medicineList == null){
            requestAllMedigines()
            return
        }

        medicineList?.let {
            _state.value = MedicineFragmentState.SuccessMedicine(it)
        }
    }

    private fun requestAllMedigines(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllMedicinesUseCase()
                .onStart { setLoading() }
                .catch { exception ->
                    hideLoading()
                    showDialog(exception.message.toString())
                    Log.d("의약품 조회 에러", exception.stackTraceToString())
                }
                .collect{ state ->
                    hideLoading()
                    when(state) {
                        is ResponseState.Success -> {
                            medicineList = state.data
                            _state.value = MedicineFragmentState.SuccessMedicine(state.data)
                        }
                        is ResponseState.Error -> {
                            _state.value = MedicineFragmentState.ErrorMedicine(state.error.code)
                            Log.e("의약품 조회 에러", state.error.toString())
                        }
                    }
                }
        }
    }
}

sealed class MedicineFragmentState {
    object Init                                                 : MedicineFragmentState()
    data class SuccessMedicine(val medicineList: MedicineList)  : MedicineFragmentState()
    data class ErrorMedicine(val error:String)                  : MedicineFragmentState()
    data class IsLoading(val isLoading: Boolean)                : MedicineFragmentState()
    data class ShowDialog(val message: String)                  : MedicineFragmentState()
}