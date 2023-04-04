package com.example.smiley.medicine.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(): ViewModel() {

    fun getMedicineList(): ArrayList<String> {
        return arrayListOf(
            "타이레놀125mg",
            "타이레놀250mg",
            "타이레놀500mg",
            "타이레놀750mg",
            "타이레놀1000mg",
            "글립타이드정200mg",
            "동아디옥타헤드랄스멕타이트현탁액 (수출용)",
            "세레타이드100디스커스",
            "세레타이드125에보할러",
            "세레타이드250디스커스 (28dose)",
            "게보린정500mg",
            "게보린소프트500mg",
            "게보린정1000mg",
            "#4500제이라스틱",
            "가나릴정(이토프리드염산염)",
            "가나모티에스알정15밀리그램(모사프리드시트르산염수화물)",
            "가나모티정(모사프리드시트르산염수화물)",
            "가나슨캡슐",
            "가나톤정50밀리그램(이토프리드염산염)",
            "가스론엔구강붕해정4밀리그램(이르소글라딘말레산염)",
            "가스론엔구강붕해정2밀리그램(이르소글라딘말레산염)",
            "가스모틴에스알정(모사프리드시트르산염수화물)",
            "가스모프정(모사프리드시트르산염이수화물)",
            "가스젯에스알정15밀리그램(모사프리드시트르산염수화물)",
            "가스탄정(모사프리드시트르산염수화물)",
        )
    }
}