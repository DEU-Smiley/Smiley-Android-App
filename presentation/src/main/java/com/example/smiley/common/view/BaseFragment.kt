package com.example.smiley.common.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.smiley.common.extension.applyTouchEffectToAllViews

open class BaseFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.applyTouchEffectToAllViews(view as ViewGroup)
    }
}