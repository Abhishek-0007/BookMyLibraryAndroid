package com.example.expensemanager.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.databinding.LibraryItemLayoutBinding
import com.example.expensemanager.extensions.ExtensionMethods
import com.example.expensemanager.models.LibraryBody
import java.util.*


class LibraryAdapter(var items : List<LibraryBody>, var context: Context,var  activity: Activity): RecyclerView.Adapter<LibraryAdapter.DataViewHolder>() {

    inner class DataViewHolder(val binding: LibraryItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(LibraryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        var item = items[position]
        holder.binding.model = item
        var locate = ExtensionMethods().toGetCurrentLocation(context = context, activity = activity)
        holder.binding.distanceLib.setText(ExtensionMethods().toGetDistance(item, locate))

            holder.binding.send.setOnClickListener {
//            val gmmIntentUri = Uri.parse("google.streetview:cbll=${items[position].latitude},${items[position].logitude}")
//            val mapIntent : Intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(holder.binding.root.context, mapIntent, null)
                val builder = Uri.Builder()
                builder.scheme("https")
                    .authority("www.google.com")
                    .appendPath("maps")
                    .appendPath("dir")
                    .appendPath("")
                    .appendQueryParameter("api", "1")
                    .appendQueryParameter("destination", item.latitude.toString() + "," + item.latitude.toString())
                val url = builder.build().toString()
                val intent = Intent(Intent.ACTION_VIEW,)
                intent.setData(Uri.parse(url))
                startActivity(context,intent, null)
            }

            holder.binding.card.setOnClickListener {  }
        }

    override fun getItemCount(): Int = items.size
}