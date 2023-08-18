package com.example.smiley.common.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel<T>: ViewModel() {

    private val _state by lazy { createSharedFlow() }
    val state: SharedFlow<T> = _state

    protected open fun createSharedFlow(): MutableSharedFlow<T> {
        return MutableSharedFlow()
    }

    protected suspend fun setState(newState: T) {
        _state.emit(newState)
    }
}