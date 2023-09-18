package com.example.smiley.selfassessment

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.smiley.R
import com.example.smiley.common.base.BaseFragment
import com.example.smiley.common.extension.addFragmentToFullScreen
import com.example.smiley.common.extension.gone
import com.example.smiley.common.extension.resetStatusBarAndNavigationBar
import com.example.smiley.common.extension.setCustomColorStatusBarAndNavigationBar
import com.example.smiley.common.extension.visibleWithAnimation
import com.example.smiley.common.listener.OnItemClickListener
import com.example.smiley.common.utils.decorutils.SpaceItemDecoration
import com.example.smiley.common.utils.textutils.IndentLeadingMarginSpan
import com.example.smiley.databinding.FragmentSelfAssessmentBinding
import com.example.smiley.databinding.LayoutSubAppBarBinding
import com.example.smiley.selfassessment.adapter.AssessmentAdapter
import com.example.smiley.selfassessment.adapter.AssessmentItem
import com.example.smiley.selfassessment.assessmentresult.AssessmentResultFragment
import eightbitlab.com.blurview.RenderScriptBlur

/**
 * A simple [Fragment] subclass.
 * Use the [SelfAssessmentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelfAssessmentFragment : BaseFragment<FragmentSelfAssessmentBinding>(R.layout.fragment_self_assessment) {

    companion object {
        @JvmStatic
        fun newInstance() = SelfAssessmentFragment()
    }

    private val appBar: LayoutSubAppBarBinding by lazy { LayoutSubAppBarBinding.bind(bind.root) }

    private val assessmentList: ArrayList<AssessmentItem>
        get() = arrayListOf(
            AssessmentItem(
                imageRes = R.drawable.img_assessment_facial,
                type = getString(R.string.assessment_facial_type),
                title = getString(R.string.assessment_facial_tile_title)
            ),
            AssessmentItem(
                imageRes = R.drawable.img_assessment_jaw,
                type = getString(R.string.assessment_jaw_type),
                title = getString(R.string.assessment_jaw_tile_title)
            ),
            AssessmentItem(
                imageRes = R.drawable.img_assessment_tooth_brush,
                type = getString(R.string.assessment_tooth_brush_type),
                title = getString(R.string.assessment_tooth_brush_tile_title)
            ),
        )

    private val assessmentAdapter by lazy {
        context?.let {
            AssessmentAdapter(it, assessmentList, onAssessmentItemClickListener)
        }
    }

    override fun initView() {
        initAppBar()
        initNoticeText()
        initBlurBackground()
        initBtnClickEvent()
        initScrollEnabled()
        initBackPressed()
        initAssessmentList()
    }

    private fun initAppBar(){
        appBar.tvAppBarTitle.text = "AI 자가진단"
    }

    private fun initNoticeText(){
        bind.tvNotice1.text = SpannableStringBuilder(bind.tvNotice1.text).apply {
            setSpan(IndentLeadingMarginSpan(), 0, length, 0)
        }

        bind.tvNotice2.text = SpannableStringBuilder(bind.tvNotice2.text).apply {
            setSpan(IndentLeadingMarginSpan(), 0, length, 0)
        }

        bind.tvNotice3.text = SpannableStringBuilder(bind.tvNotice3.text).apply {
            setSpan(IndentLeadingMarginSpan(), 0, length, 0)
        }

        bind.tvNotice4.text = SpannableStringBuilder(bind.tvNotice4.text).apply {
            setSpan(IndentLeadingMarginSpan(), 0, length, 0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initBlurBackground(){
        context ?: return

        val decorView = activity?.window?.decorView
        val rootView = decorView?.findViewById<ViewGroup>(R.id.clBlurBasedLayout)!!
        val windowBackground = decorView.background

        with(bind.blurViewBackground){
            setOnTouchListener { v, event -> true }
            setupWith(rootView, RenderScriptBlur(context))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(5f)
        }
    }

    private fun initBtnClickEvent(){
        bind.btnAssessment.setOnClickListener {
            showSelectAssessment()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initScrollEnabled(){
        bind.svParent.setOnTouchListener { _, _ ->
            bind.clSelectAssessment.isVisible
        }
    }

    private fun initBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        appBar.ivSubBackBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        bind.ivBackBtn.setOnClickListener {
            hideSelectAssessment()
        }
    }

    private fun initAssessmentList(){
        if(context == null) return

        with(bind.rvAssessment){
            adapter = assessmentAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpaceItemDecoration(context, 20, true))
            PagerSnapHelper().attachToRecyclerView(this)
        }
    }

    private fun showSelectAssessment(){
        this.setCustomColorStatusBarAndNavigationBar(
            R.color.black_trans_70,
            R.color.black_trans_70,
            false
        )

        with(bind){
            lavFaceScan.pauseAnimation()
            clSelectAssessment.visibleWithAnimation(200)
        }
    }

    private fun hideSelectAssessment(){
        this.resetStatusBarAndNavigationBar()
        with(bind){
            lavFaceScan.resumeAnimation()
            clSelectAssessment.gone()
        }
    }

    private val onAssessmentItemClickListener = object : OnItemClickListener<AssessmentItem> {
        override fun onItemClicked(position: Int, data: AssessmentItem) {
            when(position){
                0 -> {
                    this@SelfAssessmentFragment.addFragmentToFullScreen(AssessmentResultFragment.newInstance())
                }
                1 -> {
                    this@SelfAssessmentFragment.addFragmentToFullScreen(AssessmentResultFragment.newInstance())
                }
                else -> {
                    this@SelfAssessmentFragment.addFragmentToFullScreen(AssessmentResultFragment.newInstance())
                }
            }
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(bind.clSelectAssessment.isVisible){
                hideSelectAssessment()
            } else {
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }
}