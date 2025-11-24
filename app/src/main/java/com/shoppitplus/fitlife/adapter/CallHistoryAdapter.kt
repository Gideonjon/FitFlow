package com.shoppitplus.fitlife.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shoppitplus.fitlife.R
import com.shoppitplus.fitlife.db.PhoneCall

class CallHistoryAdapter : RecyclerView.Adapter<CallHistoryAdapter.CallViewHolder>() {

    private val items = mutableListOf<PhoneCall>()

    fun submitList(list: List<PhoneCall>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class CallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.txtCallName)
        private val txtPhone: TextView = itemView.findViewById(R.id.txtCallPhone)
        private val txtTypeTime: TextView = itemView.findViewById(R.id.txtCallTypeTime)

        fun bind(call: PhoneCall) {
            txtName.text = call.name
            txtPhone.text = call.phone
            txtTypeTime.text = "${call.type} • ${call.time}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_call_history, parent, false)
        return CallViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        holder.bind(items[position])
    }
}