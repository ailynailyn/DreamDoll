package com.example.edu.utap.dreamdoll.newfeed

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.NewsfeedItem
import com.example.edu.utap.dreamdoll.R
import com.example.edu.utap.dreamdoll.UserProfileActivity

class NewsfeedRVAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<NewsfeedItem>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var profilePic = itemView.findViewById<ImageView>(R.id.newsfeed_profilePic)
        internal var username = itemView.findViewById<TextView>(R.id.newsfeed_username)
        internal var image = itemView.findViewById<ImageView>(R.id.newsfeed_image)
        internal var likesTV = itemView.findViewById<TextView>(R.id.newsfeed_likesTV)
        internal var likeButton = itemView.findViewById<Button>(R.id.newsfeed_likeButton)
        internal var caption = itemView.findViewById<TextView>(R.id.newfeed_caption)

        init {
            itemView.setOnClickListener {
                Log.d("RVAdapter", "item clicked ${username.text}")

            }
        }

        fun bindView(item: NewsfeedItem) {
            Log.d("RVAdapter", "bindView(item: NewsfeedItem)")
//            profilePic.setImageResource(item.profilePicID)
            username.text = item.username
//            image.setImageResource(item.imageID)
            likesTV.text = "${item.likes} Likes"
            caption.text = item.caption

            username.setOnClickListener {
                Log.d("username clicked", "${username.text}")
                // Go to user profile.
                val intent = Intent(itemView.context, UserProfileActivity::class.java)
                intent.putExtra("username", username.text.toString())
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.news_feed_item, parent, false))
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
