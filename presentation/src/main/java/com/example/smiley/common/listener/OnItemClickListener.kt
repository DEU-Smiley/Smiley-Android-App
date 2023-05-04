package com.example.smiley.common.listener

interface OnItemClickListener<T> {
    fun onItemClicked(position: Int, data:T)
}