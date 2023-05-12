package com.example.expensemanager.Adapter

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.Interfaces.GenreOnCLick
import com.example.expensemanager.databinding.BookItemLayoutBinding
import com.example.expensemanager.databinding.GenreItemLayoutBinding
import com.example.expensemanager.models.BookInfo
import com.example.expensemanager.models.GenreInfo

class GenreListAdapter(var items: List<GenreInfo> ) : RecyclerView.Adapter<GenreListAdapter.DataViewHolder>() {
    inner class DataViewHolder(val binding : GenreItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(GenreItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = items[position]
        val imageByteArray: ByteArray = Base64.decode(item.genreImage, Base64.DEFAULT)
        Glide.with(holder.binding.root.context)
            .asBitmap()
            .load(imageByteArray)
            .into(holder.binding.image)
        holder.binding.name.setText(item.genreTitle)
    }

}
