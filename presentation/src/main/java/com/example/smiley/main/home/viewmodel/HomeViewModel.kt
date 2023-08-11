package com.example.smiley.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.common.base.ResponseState
import com.example.domain.hospital.model.SimpleHospital
import com.example.domain.hospital.usecase.GetNearByHospitalUseCase
import com.example.domain.hospital.usecase.GetNearByPartnerHospitalUseCase
import com.example.domain.magazine.usecase.GetRecentMagazineUseCase
import com.example.domain.youtube.model.YoutubeVideo
import com.example.domain.youtube.usecase.GetRecommendVideoUseCase
import com.example.smiley.common.base.BaseViewModel
import com.example.smiley.main.home.adapter.timeline.TimeLineItem
import com.example.smiley.main.home.adapter.timeline.TimeLineObject
import com.example.smiley.main.home.adapter.timeline.ViewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentMagazineUseCase: GetRecentMagazineUseCase,
    private val getNearByPartnerHospitalUseCase: GetNearByPartnerHospitalUseCase,
    private val getRecommendVideoUseCase: GetRecommendVideoUseCase
): BaseViewModel<HomeFragmentState>() {

    fun getTimeLineData(){
        viewModelScope.launch(Dispatchers.IO){
            getRecentMagazineUseCase(2)
                .onStart {
                    // Skeleton
                    Log.d("매거진 조회 요청", "요청 보냄")
                }
                .catch {
                    setState(HomeFragmentState.ShowToast(message = it.message.toString()))
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

                            setState(HomeFragmentState.TimeLine(timeLineItems))
                        }
                        is ResponseState.Error -> {
                            setState(HomeFragmentState.Error(state.error.message))
                        }
                    }
                }
        }
    }

    fun getNearByPartnerList(cnt: Int){
        viewModelScope.launch(Dispatchers.IO) {
            getNearByPartnerHospitalUseCase(cnt)
                .onStart {  }
                .catch {
                    setState(HomeFragmentState.ShowToast(message = it.message.toString()))
                    Log.e("파트너 병원 조회 에러", it.message.toString())
                }
                .collect { state ->
                    when(state){
                        is ResponseState.Success -> {
                            setState(HomeFragmentState.PartnerHospital(state.data.simpleHospitals))
                        }
                        is ResponseState.Error -> {
                            setState(HomeFragmentState.Error(state.error.message))
                        }
                    }
                }
        }
    }

    fun getRecommendVideoList(){
        viewModelScope.launch(Dispatchers.IO){
            getRecommendVideoUseCase()
                .catch {
                    setState(HomeFragmentState.ShowToast(message = it.message.toString()))
                    Log.d("추천 영상 조회 에러", it.message.toString())
                }
                .collect { state ->
                    when(state){
                        is ResponseState.Success -> {
                            setState(HomeFragmentState.RecommendVideo(state.data.youtubeList))
                        }
                        is ResponseState.Error -> {
                            setState(HomeFragmentState.Error(state.error.message))
                        }
                    }
                }
        }
    }
}

sealed class HomeFragmentState {
    object Init: HomeFragmentState()
    data class TimeLine(val timeLine: List<TimeLineItem>): HomeFragmentState()
    data class PartnerHospital(val hospitals: List<SimpleHospital>): HomeFragmentState()
    data class RecommendVideo(val youtubeList: ArrayList<YoutubeVideo>): HomeFragmentState()
    data class Error(val message: String): HomeFragmentState()
    data class ShowToast(val message: String): HomeFragmentState()
}