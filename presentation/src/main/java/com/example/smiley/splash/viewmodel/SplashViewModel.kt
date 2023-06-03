package com.example.smiley.splash.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.usecase.GetAccessFlagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getAccessFlagUseCase: GetAccessFlagUseCase
): ViewModel() {
    private val _state = MutableStateFlow<SplashActivityState>(SplashActivityState.Init)
    val state: StateFlow<SplashActivityState> get() = _state

    private fun setIsFirstAccess(flag: Boolean){
        _state.value = SplashActivityState.IsFirstAccess(flag)
    }

    fun checkFirstAccess() {
        viewModelScope.launch(Dispatchers.IO) {
            getAccessFlagUseCase().collect {
                Log.d("최초 접속 여부", "${!it}")
                // AccessFlag가 false이면 최초 접속 (접속한 적이 없음)
                setIsFirstAccess(!it)
            }
        }
    }
}

sealed class SplashActivityState {
    object Init : SplashActivityState()
    data class IsFirstAccess(val flag: Boolean) : SplashActivityState()
}