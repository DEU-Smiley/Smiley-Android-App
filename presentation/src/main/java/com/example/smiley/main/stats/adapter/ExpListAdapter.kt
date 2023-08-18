package com.example.smiley.main.stats.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.stats.model.Exp
import com.example.smiley.R
import java.text.DecimalFormat

class ExpListAdapter(
    private val exp:List<Exp>
): RecyclerView.Adapter<ExpListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_exp_list_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val title = exp[position].title

        holder.title.text = title.substring(0..title.lastIndexOf(" "))
        holder.exp.text = "+${DecimalFormat("#,###").format(exp[position].exp)} exp"
    }

    override fun getItemCount(): Int = exp.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.exp_title)
        val exp = itemView.findViewById<TextView>(R.id.exp)
    }
}