package com.example.smiley.main.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.magazine.model.Magazine
import com.example.smiley.R
import com.example.smiley.common.listener.OnItemClickListener

class TimeLineAdapter(
    private var items: ArrayList<TimeLineItem>
): RecyclerView.Adapter<TimeLineViewHolder>() {
    private var magazineClickListener: OnItemClickListener<Magazine>? = null

    fun setMagazineClickListener(listener: OnItemClickListener<Magazine>){
        magazineClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        return when(viewType){
            ViewType.TEXT_OBJECT.ordinal -> {
                TimeLineViewHolder.TextObjectViewHolder(
                    getViewDataBinding(parent, R.layout.timeline_text_view)
                )
            }
            else -> {
                TimeLineViewHolder.MagazineObjectViewHolder(
                    getViewDataBinding(parent, R.layout.timeline_magazine_view),
                    magazineClickListener
                )
            }
        }
    }

    /**
     * 바인딩 작업은 각 ViewHolder에서 처리
     * 특정 position의 아이템만 넘겨줌
     */
    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        val isLastView = position == items.size-1
        holder.bind(items[position], isLastView)
    }

    /**
    * ViewType에서 해당 position의 데이터의 ordinal(인덱스)를 반환
    */
    override fun getItemViewType(position: Int): Int {
        return ViewType.valueOf(items[position].viewType).ordinal
    }

    override fun getItemCount() = items.size

    /**
     * ViewHolder 별로 Binding을 생성하는 메소드
     */
    private fun <T: ViewDataBinding> getViewDataBinding(parent: ViewGroup, layoutRes: Int): T {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeDataSet(items: ArrayList<TimeLineItem>){
        this.items = items
        notifyDataSetChanged()
    }
}