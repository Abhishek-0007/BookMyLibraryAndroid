package com.example.expensemanager.Adapter

import android.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.expensemanager.Interfaces.SearchOnCLick
import com.example.expensemanager.databinding.SearchItemLayoutBinding
import com.example.expensemanager.models.SearchModel
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso


class SearchAdapter(val items:List<SearchModel>, val context : Context, val listener:SearchOnCLick) : RecyclerView.Adapter<SearchAdapter.DataViewHolder>() {
    inner class DataViewHolder(val binding : SearchItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = items[position]
        val theImage = GlideUrl(
            item.imageUrl, LazyHeaders.Builder()
                .addHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit / 537.36(KHTML, like Gecko) Chrome  47.0.2526.106 Safari / 537.36")
                .build()
        )
        theImage.let {
            Glide.with(context)
                .load(theImage)
                .into(holder.binding.image)
        }

        holder.binding.bookName.setText(item.title)
        holder.binding.authorName.setText(item.author)

        holder.binding.search.setOnClickListener {
            listener.searchOnClickListener(position, item, holder.itemView.x.toString(), holder.itemView.y.toString())
        }
    }
}