package com.example.expensemanager.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.Interfaces.CategoryOnClick
import com.example.expensemanager.databinding.CategoryListItemBinding
import com.example.expensemanager.databinding.LibraryItemLayoutBinding
import com.example.expensemanager.models.CategoryBody

class CategoryAdapter(var items: List<String>, var listener: CategoryOnClick) : RecyclerView.Adapter<CategoryAdapter.DataViewHolder>() {

    inner class DataViewHolder(val binding : CategoryListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(CategoryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        var item = items[position]
        if (item.toString().length <= 20)
            holder.binding.title.setText(item)
        else
            holder.binding.title.setText(item.toString().substring(0,20))

        holder.binding.card.setOnClickListener { listener.categoryClickListener(position, item) }
    }

    override fun getItemCount(): Int = items.size
}