package com.example.expensemanager.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.Interfaces.DateClickListener
import com.example.expensemanager.databinding.DateLayoutBinding
import com.example.expensemanager.models.DateModel
import java.util.*

class DateAdapter(var list : List<DateModel>,val listener:DateClickListener) : RecyclerView.Adapter<DateAdapter.DataViewHolder>() {
    inner class DataViewHolder(val binding : DateLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    private var selectedPosition = 0;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder  =
        DataViewHolder(DateLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = list.size

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: DataViewHolder, @SuppressLint("RecyclerView") position: Int) {
      val item = list[position]
            holder.binding.day.setText(item.day)
            holder.binding.date.setText(item.date)
            holder.binding.month.setText(item.month)

        if(selectedPosition==position) {
            holder.binding.date.setTextColor(Color.WHITE)
            holder.binding.day.setTextColor(Color.WHITE)
            holder.binding.month.setTextColor(Color.WHITE)

            holder.binding.card.setBackgroundColor(Color.parseColor("#EF5350"))

        }
        else {
            holder.binding.date.setTextColor(Color.BLACK)
            holder.binding.day.setTextColor(Color.BLACK)
            holder.binding.month.setTextColor(Color.BLACK)
            holder.binding.card.setBackgroundColor(Color.parseColor("#ffffff"))

        }
        holder.binding.card.setOnClickListener {
            selectedPosition = position
            notifyDataSetChanged()
            listener.dateOnClick(position, item)
        }
        }

}
