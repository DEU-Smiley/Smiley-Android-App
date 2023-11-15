package com.example.smiley.selfassessment.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.domain.assessment.model.Assessment
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.common.base.BaseFragment
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.repeatOnStarted
import com.example.smiley.common.extension.showToast
import com.example.smiley.common.extension.visible
import com.example.smiley.common.utils.decorutils.AllSpaceDecoration
import com.example.smiley.common.utils.decorutils.EdgeSpaceDecoration
import com.example.smiley.databinding.FragmentAssessmentResultBinding
import com.example.smiley.selfassessment.adapter.assessment.AssessmentResultAdapter
import com.example.smiley.test_ai_assessment.AssessmentState
import com.example.smiley.test_ai_assessment.FaceDetectViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.annotations.TestOnly
import java.io.ByteArrayOutputStream
import java.io.File

@AndroidEntryPoint
class AssessmentResultFragment : BaseFragment<FragmentAssessmentResultBinding>(R.layout.fragment_assessment_result) {

    companion object {
        const val IMAGE_PATH: String = "IMAGE_PATH"
        const val IMAGE: String = "IMAGE"
        const val ASSESSMENT_TYPE = "ASSESSMENT_TYPE"

        @JvmStatic
        fun newInstance(imagePath: String? = null, image: ByteArray? = null, type: AssessmentType) = AssessmentResultFragment().apply {
            arguments = Bundle().apply {
                putString(IMAGE_PATH, imagePath)
                putByteArray(IMAGE, image)
                putString(ASSESSMENT_TYPE, type.name)
            }
        }
    }

    enum class AssessmentType {
        FACIAL,
        SIDE_FACE,
        TOOTH_BRUSH
    }

    private var imagePath: String? = null
    private var image: ByteArray? = null
    private var assessmentType: AssessmentType? = null

    private val detectVm: FaceDetectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            imagePath = it.getString(IMAGE_PATH)
            image = it.getByteArray(IMAGE)
            assessmentType = it.getString(ASSESSMENT_TYPE)?.let { name ->
                AssessmentType.valueOf(name)
            }
        }
    }

    override fun initView() {
        initObserver()

        initLoadingView()
        loadImage()
        initResultRecyclerView()
    }

    private fun initObserver(){
        repeatOnStarted {
            observDetectState()
        }
    }

    private suspend fun observDetectState(){
        detectVm.state.collect { state ->
            when(state){
                is AssessmentState.Init -> Unit
                is AssessmentState.Error -> { context?.showToast(state.message) }
                is AssessmentState.Loading -> { handleLoading(true) }
                is AssessmentState.ShowToast -> { context?.showToast(state.message) }
                is AssessmentState.ToothBrushResult -> {
                    handleLoading(false)

                    Glide.with(requireContext())
                        .load(state.result.image)
                        .into(bind.ivResult)
                }
            }
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading) bind.clLoadingView.visible()
        else bind.clLoadingView.gone()
    }

    private fun loadImage(){
        context ?: return

        // 안면 인식인 경우
        val resultImage = imagePath?.run {
            val file = File(this)
            val bExist = file.exists()
            if (bExist) {
                BitmapFactory.decodeFile(this)
            } else {
                null
            }
        } ?: return

        when(assessmentType){
            AssessmentType.FACIAL -> {
                Glide.with(requireContext())
                    .load(resultImage)
                    .into(bind.ivResult)
            }
            AssessmentType.TOOTH_BRUSH ->{
                val userId = App.user?.userId ?: ""
                val byteArray = ByteArrayOutputStream().apply {
                    resultImage.compress(Bitmap.CompressFormat.PNG, 50, this)
                }.toByteArray()

                detectVm.detectToothBrush(userId, byteArray)
            }
            else -> {}
        }
    }

    private fun initResultRecyclerView(){
        context ?: return
        with(bind.rvAssessmentResult){
            adapter = AssessmentResultAdapter(context, getAssessmentData())
            addItemDecoration(AllSpaceDecoration(context, 0, 0, 0, 20))
            addItemDecoration(EdgeSpaceDecoration(context, 100, isLastBottomPadding = true))
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initLoadingView(){
        when(assessmentType){
            AssessmentType.FACIAL -> {
                bind.tvTitle.text = "스마일리가 얼굴을 분석 중이에요.\n잠시만 기다려주세요"
            }
            AssessmentType.TOOTH_BRUSH -> {
                bind.clLoadingView.visible()
                bind.tvTitle.text = "스마일리가 칫솔모를 분석 중이에요.\n잠시만 기다려주세요"
            }
            else -> Unit
        }
    }

    @TestOnly
    private fun getAssessmentData(): ArrayList<Assessment> {
        return if(assessmentType == AssessmentType.FACIAL){
            arrayListOf(
                Assessment.Header(
                    resultTag = "주의",
                    title = "${App.user?.name}님의 검사 결과",
                    explain = "높진 않지만 비대칭이 보이는 것 같아요. 그러나 비대칭이라고 무조건 안 좋은 것은 아니에요! 약간의 비대칭은 오히려 자연스러운 모습으로 보여질 수도 있답니다. 그래도 걱정 된다면 전문의와의 상담을 추천드려요!"
                ),

                Assessment.Stats(
                    facialAsymmetry = 13,
                    eyeDegree = 23,
                    lipDegree = 34
                ),

                Assessment.FaqList(
                    question = resources.getStringArray(R.array.facial_type1_question),
                    answer = resources.getStringArray(R.array.facial_type1_answer)
                ),
                Assessment.RecommendVideoList(
                    getRecommendVideoList()
                ),
            )
        } else {
            arrayListOf(
                Assessment.Header(
                    resultTag = "교체 필요",
                    title = "칫솔모 검사 결과",
                    explain = "칫솔 교체가 필요합니다. 눈으로는 손상이 심하지 않더라도, 망가진 칫솔모는 제 기능을 하지 못하며, 치아 손상까지 초래할 수 있습니다. 교정 환자에게는 더욱 치명적이니 꼭 칫솔을 교체해주세요!"
                ),

                Assessment.FaqList(
                    question = resources.getStringArray(R.array.toothbrush_question),
                    answer = resources.getStringArray(R.array.toothbrush_answer)
                ),
                Assessment.RecommendVideoList(
                    getRecommendVideoList()
                ),
            )
        }
    }

    private fun getRecommendVideoList(): ArrayList<Assessment.RecommendVideoList.RecommendVideo> {
        return arrayListOf(
            Assessment.RecommendVideoList.RecommendVideo(
                title = "안면비대칭 치료방법은? 한의원이냐 경락이냐 치아교정이냐 모든 논란을 끝내준다!",
                youtubeUrl = "https://www.youtube.com/watch?v=lPP2HIDSp1g",
                thumbnailRes = R.drawable.img_facial_youtube_thumbnail_1
            ),

            Assessment.RecommendVideoList.RecommendVideo(
                title = "안면비대칭 유형 구분법 / 안면비대칭 교정운동 / 안면비대칭 자가교정 [교정의 신, 리샘TV]",
                youtubeUrl = "https://www.youtube.com/watch?v=HooEhzG0t1U",
                thumbnailRes = R.drawable.img_facial_youtube_thumbnail_2
            ),

            Assessment.RecommendVideoList.RecommendVideo(
                title = "내 안면비대칭 타입은...? 안면비대칭 타입3와 실제 교정과정! | 정파카",
                youtubeUrl = "https://www.youtube.com/watch?v=FnPF60zjUu0",
                thumbnailRes = R.drawable.img_facial_youtube_thumbnail_3
            )
        )
    }
}