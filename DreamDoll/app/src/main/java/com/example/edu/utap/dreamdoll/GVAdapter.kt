package com.example.edu.utap.dreamdoll

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GVAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<DollItem>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var imageView = itemView.findViewById<ImageView>(R.id.gridItem_image)
        internal var captionTV = itemView.findViewById<TextView>(R.id.gridItem_caption)

        init {
            itemView.setOnClickListener {
                Log.d("GVAdapter", "item clicked ${captionTV.text}")
            }
        }

        fun bindView(item: DollItem) {
            Log.d("GVAdapter", "bindView(item: DollItem)")
            imageView.setImageResource(item.imgID)
            captionTV.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.features_grid_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemVH = holder as VH
        itemVH.bindView(listOfItems[position])
    }

    fun setItemList(listOfItems: List<DollItem>) {
        this.listOfItems = listOfItems
        notifyDataSetChanged()
    }
}
