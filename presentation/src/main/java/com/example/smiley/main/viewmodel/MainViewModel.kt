package com.example.smiley.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.user.usecase.GetAccessFlagUseCase
import com.example.domain.user.usecase.SetAccessFlagUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setAccessFlagUseCase: SetAccessFlagUseCase
) : ViewModel() {

    /**
     *  최초 접속 플래그를 세팅하는 메소드
     */
    fun setAccessFlag(flag: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            setAccessFlagUseCase(flag)
        }
    }
}