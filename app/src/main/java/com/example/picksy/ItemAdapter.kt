package com.example.picksy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(val context: Context,val itemarr: ArrayList<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.imageItem)
        val details = itemView.findViewById<TextView>(R.id.textItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent, false))
    }

    override fun getItemCount(): Int {
        return itemarr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = itemarr[position]
        holder.image.setImageResource(item.image)
        holder.details.text = item.name
    }

}