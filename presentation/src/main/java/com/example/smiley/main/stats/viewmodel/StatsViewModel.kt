package com.example.smiley.main.stats.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.NetworkError
import com.example.domain.common.base.ResponseState
import com.example.domain.stats.model.Stats
import com.example.domain.stats.usecase.GetTodayStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getTodayStatsUseCase: GetTodayStatsUseCase
): ViewModel() {
    private val _state = MutableStateFlow<StatsFragmentState>(StatsFragmentState.Init)
    val state:StateFlow<StatsFragmentState> = _state

    private fun setLoading(){
        _state.value = StatsFragmentState.IsLoading
    }

    private fun setSuccess(stats: Stats){
        _state.value = StatsFragmentState.SuccessStats(stats)
    }

    private fun setError(error:NetworkError){
        _state.value = StatsFragmentState.Error(error)
    }

    private fun showToast(message:String){
        _state.value = StatsFragmentState.ShowToast(message)
    }

    fun requestStatToDate(date: LocalDate){
        viewModelScope.launch(Dispatchers.IO) {
            getTodayStatsUseCase()
                .onStart {
                    setLoading()
                }
                .catch {
                    showToast(it.message.toString())
                    Log.e("통계 조회 에러", it.message.toString())
                }
                .collect { state ->
                    when(state){
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

sealed class StatsFragmentState{
    object Init : StatsFragmentState()
    object IsLoading: StatsFragmentState()
    data class SuccessStats(val stats:Stats): StatsFragmentState()
    data class Error(val error:NetworkError): StatsFragmentState()
    data class ShowToast(val message: String): StatsFragmentState()
}