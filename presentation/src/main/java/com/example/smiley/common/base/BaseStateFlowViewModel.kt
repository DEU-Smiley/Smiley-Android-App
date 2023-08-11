package com.example.smiley.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseStateFlowViewModel<T>: ViewModel() {
    private val _state = MutableStateFlow(initialState())
    val state: SharedFlow<T> = _state

    abstract fun initialState(): T

    protected fun setState(newState: T){
        _state.value = newState
    }
}