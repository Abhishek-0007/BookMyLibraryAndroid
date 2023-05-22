package com.example.expensemanager.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.Interfaces.SeatsOnClick
import com.example.expensemanager.databinding.ActivitySeatBookingBinding
import com.example.expensemanager.databinding.SeatsLayoutItemBinding
import com.example.expensemanager.models.SeatViewModel
import com.example.expensemanager.R;
import com.example.expensemanager.extensions.ExtensionMethods

class SeatAdapter(var items : List<SeatViewModel>, var listener:SeatsOnClick) : RecyclerView.Adapter<SeatAdapter.DataViewHolder>() {

    inner class DataViewHolder(var binding: SeatsLayoutItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(SeatsLayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = items[position]

        holder.binding.firstSeat.let {
            it.background.setTint(Color.parseColor(seatColor(item.firstSeat)))
                it.setOnClickListener {
                    if(item.firstSeat != 1){
                        it.background.setTint(Color.parseColor("#C51162"))
                        clickHelper(1,position) }
                    }

        }
        holder.binding.secondSeat.let {
            it.background.setTint(Color.parseColor(seatColor(item.secondSeat)))
            it.setOnClickListener {
                if(item.secondSeat != 1){
                    it.background.setTint(Color.parseColor("#C51162"))
                    clickHelper(2,position)
                }
            }

        }
        holder.binding.thirdSeat.let {
            it.background.setTint(Color.parseColor(seatColor(item.thirdSeat)))
            it.setOnClickListener {
                if(item.thirdSeat != 1){
                    it.background.setTint(Color.parseColor("#C51162"))
                    clickHelper(3,position)
                }
            }
        }
        holder.binding.fourthSeat.let {
            it.background.setTint(Color.parseColor(seatColor(item.fourthSeat)))
            it.setOnClickListener {
                if(item.fourthSeat != 1){
                    it.background.setTint(Color.parseColor("#C51162"))
                    clickHelper(4,position)
                }
            }
        }
        holder.binding.fifthSeat.let {
            it.background.setTint(Color.parseColor(seatColor(item.fifthSeat)))
            it.setOnClickListener {
                if(item.fifthSeat != 1){
                    it.background.setTint(Color.parseColor("#C51162"))
                    clickHelper(5,position)
                }
            }
        }
    }

    private fun clickHelper(seatNumber : Int, row : Int){
        listener.onSeatClick(seatNumber, row)
    }

    private fun seatColor(seat : Int) : String{
        if (seat == 0)
            return "#E8EAF6"
        else return "#304FFE"
    }
    override fun getItemCount(): Int = items.size
}