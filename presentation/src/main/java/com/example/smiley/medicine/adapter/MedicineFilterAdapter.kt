package com.example.smiley.medicine.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.R

class MedicineFilterAdapter(
    medicineList: ArrayList<String>
) : RecyclerView.Adapter<MedicineFilterAdapter.ViewHolder>(), Filterable {
    interface OnItemClickListener {
        fun onItemClicked(position: Int, data:String)
    }

    fun setOnItemClickListener(listner:OnItemClickListener){
        this.itemClickListener = listner
    }

    private lateinit var itemClickListener: OnItemClickListener

    private var filteredList: ArrayList<String> = medicineList
    private val unFilteredList: ArrayList<String> = medicineList
    private var userInput:String
        get() = userInput
        set(value) {
            userInput = value
        }

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
        holder.medicineTitle.text = filteredList[position]
        holder.layout.setOnClickListener {
            if(!::itemClickListener.isInitialized) return@setOnClickListener

            itemClickListener.onItemClicked(position, "${holder.medicineTitle.text}")
        }
    }

    override fun getItemCount() = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val input = "$p0"

                filteredList =
                    if (input.isEmpty()) arrayListOf()
                    else {
                        val filteringList = ArrayList<String>()
                        for (item in unFilteredList) {
                            if (item.contains(input)) filteringList.add(item)
                        }
                        filteringList
                    }

                val filterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<String>
                notifyDataSetChanged()
            }
        }
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout:LinearLayout = itemView.findViewById(R.id.search_medicine_layout)
        val medicineTitle: TextView = itemView.findViewById(R.id.search_result_item)
    }
}