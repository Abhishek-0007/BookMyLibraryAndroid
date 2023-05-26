package com.example.expensemanager.Adapter

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.expensemanager.Interfaces.BookOnCLick
import com.example.expensemanager.databinding.BookItemLayoutBinding
import com.example.expensemanager.models.BookInfo
import com.google.android.material.R


class PopularBookListAdapter(var items: List<BookInfo>, var listener: BookOnCLick) : RecyclerView.Adapter<PopularBookListAdapter.DataViewHolder>() {

    inner class DataViewHolder(val binding : BookItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(BookItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = items[position]
        val imageByteArray: ByteArray = Base64.decode(item.bookImage, Base64.DEFAULT)
        val theImage = GlideUrl(
            item.bookImage, LazyHeaders.Builder()
                .addHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36")
                .build()
        )
        theImage.let {
            Glide.with(holder.binding.root.context)
                .load(theImage)
                .error(com.example.expensemanager.R.drawable.bookdefault)
                .into(holder.binding.image)
        }


        val maxLen : Int = if(item.bookName?.length!! >= 16) 16 else item.bookName?.length!!
        holder.binding.name.setText(item.bookName?.substring(0,maxLen))
        holder.binding.image.setOnClickListener{
            listener.bookOnClickListener(position, item)
        }
    }

}