package com.example.smiley.hospital.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.hospital.model.Hospital
import com.example.domain.hospital.model.HospitalList
import com.example.smiley.R
import com.example.smiley.common.extension.setForegroundColor

class HospitalFilterAdapter(
    hospitalList: HospitalList
) : RecyclerView.Adapter<HospitalFilterAdapter.ViewHolder>(), Filterable {

    interface OnItemClickListener {
        fun onItemClicked(position: Int, data:String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.itemClickListener = listener
    }

    private lateinit var itemClickListener: OnItemClickListener
    private var filteredList : ArrayList<Hospital> = arrayListOf()
    private val unFilteredList : ArrayList<Hospital> = arrayListOf<Hospital>().apply {
        addAll(hospitalList.hospitals)
    }
    private var userInput:String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_search_result_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(filteredList.size <= position) return

        val hospital = filteredList[position]
        holder.hospitalTitle.apply {
            text = hospital.dutyName

            val matchedIdx = text.indexOf(userInput, 0, true)
            if(matchedIdx >= 0){
                setForegroundColor(resources.getColor(R.color.primary_normal), matchedIdx, matchedIdx + userInput.length)
            }
        }

        holder.address.text = "[${hospital.dutyAddress}] "
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                userInput = "$p0"
                val newFilteredList =
                    if (userInput.isEmpty()) arrayListOf()
                    else {
                        val filteringList = arrayListOf<Hospital>()
                        for (item in unFilteredList) {
                            if (item.dutyName.contains(userInput)) filteringList.add(item)
                        }
                        filteringList
                    }

                return FilterResults().apply { values = newFilteredList }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredList = p1?.values as ArrayList<Hospital>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: LinearLayout = itemView.findViewById(R.id.search_medicine_layout)
        val hospitalTitle: TextView = itemView.findViewById(R.id.search_result_item)
        val address: TextView = itemView.findViewById(R.id.search_sub_title)
    }
}