package com.example.smiley.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.smiley.common.extension.applyTouchEffectToAllViews

abstract class BaseFragment<T: ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {

    lateinit var bind: T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DataBindingUtil.inflate(inflater, layoutRes, container, false)

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.applyTouchEffectToAllViews(view as ViewGroup)
        bind.lifecycleOwner = this@BaseFragment
        initView()

        super.onViewCreated(view, savedInstanceState)
    }

    abstract fun initView()
}