package com.example.smiley.main.stats.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.domain.stats.model.Exp
import com.example.smiley.R

class ExpGridAdapter (
    private val context:Context,
    private val data: List<Exp>,
    private val colors: ArrayList<Int>
): BaseAdapter() {
    override fun getCount(): Int = data.size

    override fun getItem(p0: Int): Exp = data[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.sub_exp_grid_item, parent, false)

        view.apply {
            val expName = findViewById<TextView>(R.id.exp_name)
            val expType = findViewById<View>(R.id.ic_exp_type)
            expName.text = data[position].title
            expType.backgroundTintList = ColorStateList.valueOf(colors[position])
        }

        return view
    }
}