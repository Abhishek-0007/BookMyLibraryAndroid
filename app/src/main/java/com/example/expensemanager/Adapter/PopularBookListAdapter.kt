package com.example.expensemanager.Adapter

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.databinding.BookItemLayoutBinding
import com.example.expensemanager.models.BookInfo


class PopularBookListAdapter(var items: List<BookInfo>, var listener: BookOnCLick) : RecyclerView.Adapter<PopularBookListAdapter.DataViewHolder>() {

    inner class DataViewHolder(val binding : BookItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(BookItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = items[position]
        val imageByteArray: ByteArray = Base64.decode(item.bookImage, Base64.DEFAULT)
        Glide.with(holder.binding.root)
            .asBitmap()
            .load(imageByteArray)
            .into(holder.binding.image)
        val maxLen : Int = if(item.bookName?.length!! >= 16) 16 else item.bookName?.length!!
        holder.binding.name.setText(item.bookName?.substring(0,maxLen))
        holder.binding.image.setOnClickListener{
            listener.bookOnClickListener(position, item)
        }
    }

}