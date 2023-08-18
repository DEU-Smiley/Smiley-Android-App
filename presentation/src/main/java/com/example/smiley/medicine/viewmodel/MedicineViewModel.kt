package com.example.smiley.medicine.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.medicine.model.MedicineList
import com.example.domain.medicine.usecase.GetAllMedicinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getAllMedicinesUseCase: GetAllMedicinesUseCase
): ViewModel() {

    private val _state = MutableStateFlow<MedicineFragmentState>(MedicineFragmentState.Init)
    val state : StateFlow<MedicineFragmentState> get() = _state

    var medicineList: MedicineList? = null

    private fun setLoading(){
        _state.value = MedicineFragmentState.IsLoading
    }

    private fun showDialog(message: String){
        _state.value = MedicineFragmentState.ShowDialog(message)
    }

    private fun setStateToSuccess(medicineList: MedicineList){
        _state.value = MedicineFragmentState.SuccessMedicine(UUID.randomUUID(), medicineList)
    }

    private fun setStateToError(error:String){
        _state.value = MedicineFragmentState.ErrorMedicine(error)
    }

    fun getMedicineList() {
        // 받아온 약품 리스트가 없으면 서버로 요청
        if(medicineList == null){
            requestAllMedigines()
            return
        }

        medicineList?.let {
            viewModelScope.launch(Dispatchers.IO) {
                setStateToSuccess(it)
            }
        }
    }

    private fun requestAllMedigines(){
        viewModelScope.launch {
            getAllMedicinesUseCase()
                .onStart { setLoading() }
                .catch { exception ->
                    showDialog(exception.message.toString())
                    Log.d("의약품 조회 에러", exception.stackTraceToString())
                }
                .collect{ state ->
                    when(state) {
                        is ResponseState.Success -> {
                            medicineList = state.data
                            setStateToSuccess(state.data)
                        }
                        is ResponseState.Error -> {
                            setStateToError(state.error.code)
                            Log.e("의약품 조회 에러", state.error.toString())
                        }
                    }
                }
        }
    }
}

sealed class MedicineFragmentState {
    object Init                                                 : MedicineFragmentState()
    object IsLoading                                            : MedicineFragmentState()
    data class SuccessMedicine(
        val id: UUID,
        val medicineList: MedicineList)                         : MedicineFragmentState()
    data class ErrorMedicine(val error:String)                  : MedicineFragmentState()
    data class ShowDialog(val message: String)                  : MedicineFragmentState()
}