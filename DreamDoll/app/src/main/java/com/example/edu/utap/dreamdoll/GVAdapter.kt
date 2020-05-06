package com.example.edu.utap.dreamdoll

import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GVAdapter(hairDisplay: ImageTransferItem, eyeDisplay: ImageTransferItem, browDisplay: ImageTransferItem, noseDisplay: ImageTransferItem, lipDisplay: ImageTransferItem)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<DollItem>()

    private var hair = hairDisplay
    private var eyes = eyeDisplay
    private var brows = browDisplay
    private var nose = noseDisplay
    private var lips = lipDisplay

    inner class VH(itemView: View, hairDisplay: ImageTransferItem, eyeDisplay: ImageTransferItem, browDisplay: ImageTransferItem, noseDisplay: ImageTransferItem, lipDisplay: ImageTransferItem) : RecyclerView.ViewHolder(itemView) {
        internal var imageView = itemView.findViewById<ImageView>(R.id.gridItem_image)
        internal var captionTV = itemView.findViewById<TextView>(R.id.gridItem_caption)
        internal var fullBody = 0

        init {
            itemView.setOnClickListener {
                Log.d("GVAdapter", "item clicked ${captionTV.text}")
                var displayID = listOfItems[adapterPosition].imgID
                var fullBodyID = displayID
                var fullDisplayId = listOfItems[adapterPosition].fullBodyID

                if(fullDisplayId != null) {
                    fullBodyID = fullDisplayId
                }

                var text = captionTV.text.toString()

                if(text.contains("hair")) {
                    hairDisplay.image.setImageResource(displayID)
                    hairDisplay.setID(fullBodyID)
                } else if(text.contains("eye")){
                    eyeDisplay.image.setImageResource(displayID)
                    eyeDisplay.setID(fullBodyID)
                } else if(text.contains("brows")){
                    browDisplay.image.setImageResource(displayID)
                    browDisplay.setID(fullBodyID)
                } else if(text.contains("nose")){
                    noseDisplay.image.setImageResource(displayID)
                    noseDisplay.setID(fullBodyID)
                } else {
                    lipDisplay.image.setImageResource(displayID)
                    lipDisplay.setID(fullBodyID)
                }
            }
        }

        fun bindView(item: DollItem) {
            Log.d("GVAdapter", "bindView(item: DollItem)")
            if(item.previewID != null) {
                imageView.setImageResource(item.previewID)
            } else {
                imageView.setImageResource(item.imgID)
            }
            captionTV.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.features_grid_item, parent, false), hair, eyes, brows, nose, lips)
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
