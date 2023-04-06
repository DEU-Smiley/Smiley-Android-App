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
    context:Context,
    medicineList: MedicineList
) : RecyclerView.Adapter<MedicineFilterAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onItemClicked(position: Int, data:String)
    }

    fun setOnItemClickListener(listner:OnItemClickListener){
        this.itemClickListener = listner
    }

    private lateinit var itemClickListener: OnItemClickListener
    private val context = context
    private var filteredList    : List<Medicine> = emptyList()
    private val unFilteredList  : List<Medicine> = medicineList.medicines
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.medicineTitle.apply {
            text = filteredList[position].itemName
            val matchedIdx = text.indexOf(userInput, 0, true)
            if(matchedIdx >= 0){
                setForegroundColor(context.resources.getColor(R.color.primary_normal), matchedIdx, matchedIdx + userInput.length)
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

                filteredList =
                    if (userInput.isEmpty()) arrayListOf()
                    else {
                        val filteringList = ArrayList<Medicine>()
                        for (item in unFilteredList) {
                            if (item.itemName.contains(userInput)) filteringList.add(item)
                        }
                        filteringList
                    }

                val filterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Medicine>
                notifyDataSetChanged()
            }
        }
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout:LinearLayout = itemView.findViewById(R.id.search_medicine_layout)
        val medicineTitle: TextView = itemView.findViewById(R.id.search_result_item)
    }
}