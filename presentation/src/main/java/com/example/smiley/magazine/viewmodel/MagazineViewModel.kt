package com.example.smiley.magazine.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.magazine.model.Magazine
import com.example.domain.magazine.model.MagazineList
import com.example.domain.magazine.usecase.GetAllMagazinesUseCase
import com.example.smiley.main.home.adapter.TimeLineItem
import com.example.smiley.main.home.viewmodel.TimeLineState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MagazineViewModel @Inject constructor(
    private val getAllMagazinesUseCase: GetAllMagazinesUseCase
): ViewModel() {
    private val _state = MutableStateFlow<MagazineListState>(MagazineListState.Init)
    val state: StateFlow<MagazineListState> = _state

    private fun setInit(){
        _state.value = MagazineListState.Init
    }
    private fun setSuccess(magazineList: MagazineList){
        _state.value = MagazineListState.SuccessLoad(magazineList)
    }

    private fun setError(message: String){
        _state.value = MagazineListState.Error(message)
    }

    fun getAllMagazines(){
        viewModelScope.launch(Dispatchers.IO) {
            getAllMagazinesUseCase()
                .onStart {
                    setInit()
                    Log.d("매거진 조회 요청", "요청 보냄")
                }
                .catch {
                    setError(message = it.message.toString())
                    Log.e("매거진 조회 에러", it.message.toString())
                }
                .collect { state ->
                    when(state){
                        is ResponseState.Success -> {
                            setSuccess(state.data)
                        }
                        is ResponseState.Error -> {
                            setError(state.error.message)
                        }
                    }
                }
        }
    }
}

sealed class MagazineListState {
    object Init: MagazineListState()
    data class SuccessLoad(val magazineList: MagazineList): MagazineListState()
    data class Error(val message: String): MagazineListState()
}