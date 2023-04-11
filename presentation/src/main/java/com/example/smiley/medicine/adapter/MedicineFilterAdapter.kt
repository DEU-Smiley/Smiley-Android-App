package com.example.smiley.medicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.medicine.model.Medicine
import com.example.domain.medicine.model.MedicineList
import com.example.smiley.R
import com.example.smiley.common.extension.setForegroundColor

class MedicineFilterAdapter(
    medicineList: MedicineList
) : RecyclerView.Adapter<MedicineFilterAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onItemClicked(position: Int, data:String)
    }

    fun setOnItemClickListener(listner:OnItemClickListener){
        this.itemClickListener = listner
    }

    private lateinit var itemClickListener: OnItemClickListener
    private var filteredList    : ArrayList<Medicine> = arrayListOf()
    private val unFilteredList  : ArrayList<Medicine> = arrayListOf<Medicine>().apply {
        addAll(medicineList.medicines)
    }
    private var userInput:String = ""

    /**
     * ViewHolder를 생성하는 메소드
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_search_result_item, parent, false)

        return ViewHolder(view)
    }

    /**
     * ViewHolder에 데이터를 바인딩해주는 메소드
     * 생성자에서 전달 받은 medicineList의 데이터를 Position에 맞게 ViewHolder에 할당
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.medicineTitle.apply {
            // 약품 이름 앞에 전문/일반 약품 표시
            val medicine = filteredList[position]
            when (medicine.type) {
                "전문의약품" -> {
                    text = "[전문] " + medicine.itemName
                    setForegroundColor(resources.getColor(R.color.detail_point), 0, 4)
                }
                "일반의약품" -> {
                    text = "[일반] " + medicine.itemName
                    setForegroundColor(resources.getColor(R.color.primary_normal), 0, 4)
                }
                else -> {
                    text = "[기타] " + medicine.itemName
                    setForegroundColor(resources.getColor(R.color.gray4_CB), 0, 4)
                }
            }

            // 검색어와 일치하는 단어 하이라이팅
            val matchedIdx = text.indexOf(userInput, 0, true)
            if(matchedIdx >= 0){
                setForegroundColor(resources.getColor(R.color.primary_normal), matchedIdx, matchedIdx + userInput.length)
            }
        }

        holder.layout.setOnClickListener {
            if(!::itemClickListener.isInitialized) return@setOnClickListener

            itemClickListener.onItemClicked(position, "${holder.medicineTitle.text}")
        }
    }

    override fun getItemCount() = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                userInput = "$p0"
                val newFilteredList =
                    if (userInput.isEmpty()) arrayListOf()
                    else {
                        val filteringList = ArrayList<Medicine>()
                        for (item in unFilteredList) {
                            if (item.itemName.contains(userInput)) filteringList.add(item)
                        }
                        filteringList
                    }

                return FilterResults().apply { values = newFilteredList }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Medicine>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout:LinearLayout = itemView.findViewById(R.id.search_medicine_layout)
        val medicineTitle: TextView = itemView.findViewById(R.id.search_result_item)
    }
}