package com.example.smiley.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T>: ViewModel() {
    private val _state = MutableSharedFlow<T>()
    val state: SharedFlow<T> = _state

    protected suspend fun setState(newState: T){
        _state.emit(newState)
    }
}