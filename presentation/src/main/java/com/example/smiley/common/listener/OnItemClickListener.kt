package com.example.smiley.common.listener

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClicked(position: Int, data:T) = Unit
    fun onItemClicked(view:View, position: Int, data: T) = Unit
}