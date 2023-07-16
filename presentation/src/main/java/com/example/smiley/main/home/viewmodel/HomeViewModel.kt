package com.example.smiley.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.NetworkError
import com.example.domain.common.base.ResponseState
import com.example.domain.magazine.usecase.GetRecentMagazineUseCase
import com.example.smiley.main.home.adapter.TimeLineItem
import com.example.smiley.main.home.adapter.TimeLineObject
import com.example.smiley.main.home.adapter.ViewType
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
class HomeViewModel @Inject constructor(
    private val getRecentMagazineUseCase: GetRecentMagazineUseCase
): ViewModel() {
    private val _timeLineState = MutableStateFlow<TimeLineState>(TimeLineState.Init)
    val timeLineState: StateFlow<TimeLineState> = _timeLineState

    private fun setSuccess(timeLines: List<TimeLineItem>){
        _timeLineState.value = TimeLineState.SuccessLoad(timeLines)
    }

    private fun setError(message: String){
        _timeLineState.value = TimeLineState.Error(message)
    }

    private fun showToast(message: String){
        _timeLineState.value = TimeLineState.ShowToast(message)
    }

    fun getTimeLineData(){
        viewModelScope.launch(Dispatchers.IO){
            getRecentMagazineUseCase(2)
                .onStart {
                    // Skeleton
                    Log.d("매거진 조회 요청", "요청 보냄")
                }
                .catch {
                    showToast(message = it.message.toString())
                    Log.e("타임라인 조회 에러", it.message.toString())
                }
                .collect { state ->
                    when(state){
                        is ResponseState.Success -> {
                            val timeLineItems = arrayListOf<TimeLineItem>().apply {
                                state.data.magazines.forEach {
                                    add(
                                        TimeLineItem(
                                            viewType = ViewType.MAGAZINE_OBJECT.name,
                                            viewObject = TimeLineObject.MagazineObject(
                                                notice = it.title.replace("\n", " "),
                                                magazine = it
                                            )
                                        )
                                    )
                                }
                            }

                            setSuccess(timeLineItems)
                        }
                        is ResponseState.Error -> {
                            setError(state.error.message)
                        }
                    }
                }
        }
    }
}

sealed class TimeLineState {
    object Init: TimeLineState()
    data class SuccessLoad(val timeLine: List<TimeLineItem>): TimeLineState()
    data class Error(val message: String): TimeLineState()
    data class ShowToast(val message: String): TimeLineState()
}