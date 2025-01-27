package com.example.smiley.test_ai_assessment

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.toothbrush.model.ToothBrush
import com.example.domain.toothbrush.usecase.CheckToothBrushStatusUseCase
import com.example.smiley.common.base.BaseStateFlowViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FaceDetectViewModel @Inject constructor(
    private val checkToothBrushStatusUseCase: CheckToothBrushStatusUseCase
): BaseStateFlowViewModel<AssessmentState>() {

    override fun initialState() = AssessmentState.Init

    /**
     * 옆면 얼굴 감지 메소드
     */
    fun detectSideFace(userId:String, bitmap: Bitmap){
    }

    /**
     * 칫솔모 교체 판별 메소드
     * @param userId String
     * @param image ByteArray
     */
    fun detectToothBrush(userId: String, image: ByteArray) {
        viewModelScope.launch(Dispatchers.IO) {
            checkToothBrushStatusUseCase(userId, image)
                .onStart { setState(AssessmentState.Loading) }
                .catch {
                    setState(AssessmentState.ShowToast(it.message.toString()))
                    Log.d("칫솔 검사 에러", it.message.toString())
                }
                .collect { state ->
                    when (state) {
                        is ResponseState.Success -> {
                            setState(AssessmentState.ToothBrushResult(state.data))
                        }

                        is ResponseState.Error -> {
                            setState(AssessmentState.Error(state.error.message))
                        }
                    }
                }
        }
    }
}

sealed class AssessmentState {
    object Init: AssessmentState()
    object Loading: AssessmentState()
    data class ToothBrushResult(val result: ToothBrush): AssessmentState()
    data class Error(val message: String): AssessmentState()
    data class ShowToast(val message: String): AssessmentState()
}