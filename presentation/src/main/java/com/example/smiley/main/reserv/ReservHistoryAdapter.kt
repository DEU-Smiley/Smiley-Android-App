package com.example.smiley.main.reserv

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.reservation.model.Reserv
import com.example.domain.reservation.model.ReservList
import com.example.smiley.App
import com.example.smiley.R
import com.example.smiley.common.extension.getDateDifference
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubReservHistoryItemBinding
import java.time.LocalDateTime

class ReservHistoryAdapter(
    private val data: ReservList
): RecyclerView.Adapter<ReservHistoryAdapter.ViewHolder>(){
    private val _context = App.ApplicationContext()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_reserv_history_item)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.reservList[position])
    }

    override fun getItemCount() = data.reservList.size

    inner class ViewHolder(
        private val bind: SubReservHistoryItemBinding
    ) : RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: Reserv){
            val dateDiff = item.reservDate.getDateDifference(LocalDateTime.now())

            with(bind){
                reservStatus.apply {
                    if(dateDiff < 0){
                        text = "진료 완료"
                        backgroundTintList = ColorStateList.valueOf(
                            _context.resources.getColor(R.color.background)
                        )
                        setTextColor(_context.resources.getColor(R.color.gray3_8E))
                    } else {
                        backgroundTintList = ColorStateList.valueOf(
                            _context.resources.getColor(R.color.light_blue1)
                        )
                        setTextColor(_context.resources.getColor(R.color.primary_dark))
                        text = "D-$dateDiff"
                    }
                }

                hospitalName.text = item.hospitalName
                hospitalAddress.text = "${item.hospitalAddress} | 치과"
                reservDate.text = item.reservDate.run {
                    "${year}년 ${monthValue}월 ${this.dayOfMonth}일 ${hour}시 ${minute}분"
                }

                // 클릭 효과 설정
                reservHistoryLayout.setOnTouchListener(TransparentTouchListener())
            }

        }
    }

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
}