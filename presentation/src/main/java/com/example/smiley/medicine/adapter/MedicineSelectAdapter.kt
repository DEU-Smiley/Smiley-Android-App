package com.example.smiley.medicine.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.medicine.model.Medicine
import com.example.smiley.R

/**
 * 선택한 약품에 대한 어댑터 (해시태그 형태)
 */
class MedicineSelectAdapter(
    private val selectList: ArrayList<String>
) : RecyclerView.Adapter<MedicineSelectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_medicine_selected_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.medicineTextView.text = selectList[position]
        holder.deleteBtn.setOnClickListener {
            deleteMedicine(position)
        }
    }

    override fun getItemCount() = selectList.size

    /**
     * 약품 선택시 리스트에 추가하는 메소드
     * @param medicine: String
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addMedicine(medicine:String){
        // 약품 이름 앞에 [타입] 부분은 제외하고 추가
        val realItemName = medicine.split(" ")[1]
        if(selectList.contains(realItemName)) return

        selectList.add(realItemName)
        notifyDataSetChanged()
    }

    /**
     * 약품 선택 취소시 리스트에서 제거하는 메소드
     * @param posit: Int
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun deleteMedicine(posit:Int) {
        this.selectList.removeAt(posit)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        val medicineTextView = itemView.findViewById<TextView>(R.id.medicine_text_view)
        val deleteBtn = itemView.findViewById<ImageView>(R.id.delete_icon)
    }
}