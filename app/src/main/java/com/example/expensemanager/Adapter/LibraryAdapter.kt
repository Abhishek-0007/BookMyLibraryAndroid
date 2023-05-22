package com.example.expensemanager.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.Interfaces.LibraryOnClick
import com.example.expensemanager.databinding.LibraryItemLayoutBinding
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.LibraryBody


class LibraryAdapter(var items : List<LibraryBody>, var context: Context,var  activity: Activity, var listener : LibraryOnClick): RecyclerView.Adapter<LibraryAdapter.DataViewHolder>() {

    inner class DataViewHolder(val binding: LibraryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LibraryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        val item = items[position]
        holder.binding.model = item
        var locate = ExtensionMethods().tryGetCurrentLocation(context = context, activity = activity)
//        holder.binding.distanceLib.setText(ExtensionMethods().tryGetDistance(item, locate))

            holder.binding.send.setOnClickListener {
                val url = Uri.parse("google.navigation:q=" + item.latitude + "," + item.logitude )
                val intent = Intent(Intent.ACTION_VIEW, url)
                intent.setPackage("com.google.android.apps.maps")
                startActivity(context, intent, null)
            }
        holder.binding.titleLib.setText("${item.libraryCode}")

            holder.binding.tvShowTime1.setOnClickListener {
                listener.onCardListener(1, item)
            }
            holder.binding.tvShowTime2.setOnClickListener {
                listener.onCardListener(2, item)
            }
            holder.binding.tvShowTime3.setOnClickListener {
                listener.onCardListener(3, item)
            }
            }

    override fun getItemCount(): Int = items.size
}