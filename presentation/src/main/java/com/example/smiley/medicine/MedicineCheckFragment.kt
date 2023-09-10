package com.example.smiley.medicine

import android.text.SpannableStringBuilder
import com.example.smiley.R
import com.example.smiley.common.base.BaseFragment
import com.example.smiley.common.utils.textutils.IndentLeadingMarginSpan
import com.example.smiley.databinding.FragmentMedicineCheckBinding
import com.example.smiley.databinding.LayoutSubAppBarBinding

class MedicineCheckFragment : BaseFragment<FragmentMedicineCheckBinding>(R.layout.fragment_medicine_check) {

    private val appBar by lazy { LayoutSubAppBarBinding.bind(bind.root) }

    companion object {
        @JvmStatic
        fun newInstance() = MedicineCheckFragment()
    }

    override fun initView() {
        initAppBar()
        initNoticeText()
    }

    private fun initAppBar(){
        appBar.tvAppBarTitle.text = "의약품 검사"
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
    }
}