package com.example.smiley.main.home.adapter.partner

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.hospital.model.SimpleHospital
import com.example.smiley.R
import com.example.smiley.common.extension.invisible
import com.example.smiley.common.listener.TransparentTouchListener
import com.example.smiley.databinding.SubPartnerHospitalItemBinding

class PartnerListAdapter(
    private var hospitals: ArrayList<SimpleHospital>
): RecyclerView.Adapter<PartnerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            getViewDataBinding(parent, R.layout.sub_partner_hospital_item)
        )
    }

    override fun getItemCount(): Int = hospitals.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(hospitals[position], position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataSet(hospitals: ArrayList<SimpleHospital>){
        this.hospitals = hospitals
        notifyDataSetChanged()
    }

    inner class ViewHolder(
        private val bind: SubPartnerHospitalItemBinding
    ) : RecyclerView.ViewHolder(bind.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: SimpleHospital, position: Int){
            with(bind){
                tvHospitalName.text = item.dutyName
                tvHospitalAddress.text = item.dutyAddress
                tvRank.text = "${position+1}"
                tvReviewPoint.text ="★ 5.0"
                tvReviewCnt.text = " (${String.format("%,d", ((Math.random() * 1500) + 1000).toInt())})"

                if(position != 0) viewTopDivider.invisible()
            }

            bind.clParent.setOnTouchListener(TransparentTouchListener())
        }
    }

    private fun <T: ViewDataBinding> getViewDataBinding(parent: ViewGroup, layoutRes: Int): T {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            false
        )
    }


}