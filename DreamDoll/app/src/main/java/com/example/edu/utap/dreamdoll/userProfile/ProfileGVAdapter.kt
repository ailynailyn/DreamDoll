package com.example.edu.utap.dreamdoll.userProfile

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.DollItem
import com.example.edu.utap.dreamdoll.NewsfeedItem
import com.example.edu.utap.dreamdoll.R
import com.example.edu.utap.dreamdoll.UserProfileActivity

class ProfileGVAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<NewsfeedItem>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var imageView = itemView.findViewById<ImageView>(R.id.gridItem_image)

        init {

            imageView.setOnClickListener {
                Log.d("GVAdapter", "userGVadapter photo was clicked.")
                // Open up the picture.
//                var picLayout = findViewById<FrameLayout>(R.id.singlePostLayout)
//                picLayout.visibility = View.VISIBLE

            }
        }

        // The grid of post pictures.
        fun bindView(item: NewsfeedItem) {
            Log.d("GVAdapter", "bindView(item: NewsfeedItem)")
            var imageID = item.imageID
            if(imageID == null) {
                imageView.setImageResource(R.drawable.falling_shoes_bag)
            } else {
                imageView.setImageResource(R.drawable.doll_face)   // use glide???
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.profile_grid_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemVH = holder as VH
        itemVH.bindView(listOfItems[position])
    }

    fun setItemList(listOfItems: List<NewsfeedItem>) {
        this.listOfItems = listOfItems
        notifyDataSetChanged()
    }
}
