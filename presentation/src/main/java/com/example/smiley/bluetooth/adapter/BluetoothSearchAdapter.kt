package com.example.smiley.bluetooth.adapter

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smiley.R
import com.example.smiley.common.extension.gone

class BluetoothSearchAdapter(
    var scanResults: List<BluetoothDevice>
) : RecyclerView.Adapter<BluetoothSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_bluetooth_result_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            if(position == 0) topLine.gone()
            deviceName.text = scanResults[position].name
            deviceAddress.text = scanResults[position].address
        }
    }

    override fun getItemCount() = scanResults.size

    @SuppressLint("NotifyDataSetChanged")
    fun setSearchResults(scanResults: List<BluetoothDevice>){
        this.scanResults = scanResults

        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName      : TextView  = itemView.findViewById(R.id.device_name)
        val deviceAddress   : TextView  = itemView.findViewById(R.id.device_address)
        val topLine         : View      = itemView.findViewById(R.id.top_line)
    }
}