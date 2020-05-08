package com.example.edu.utap.dreamdoll.account

import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.*
import com.google.firebase.storage.FirebaseStorage

class AccountGVAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<NewsfeedItem>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var imageView = itemView.findViewById<ImageView>(R.id.gridItem_image)
        internal lateinit var postItem : NewsfeedItem

        init {

            imageView.setOnClickListener {
                Log.d("GVAdapter", "userGVadapter photo was clicked.")
                // Open up the picture.
//                var picLayout = findViewById<FrameLayout>(R.id.singlePostLayout)
//                picLayout.visibility = View.VISIBLE
                val intent = Intent(itemView.context, AccountSinglePostActivity::class.java)
                intent.putExtra("username", postItem.username)
                intent.putExtra("profilePicID", postItem.profilePicID)
                intent.putExtra("imageID", postItem.imageID)
                intent.putExtra("likes", postItem.likes)
                intent.putExtra("caption", postItem.caption)
                intent.putExtra("postID", postItem.postID)
                intent.putExtra("userID", postItem.userID)
                val timestampStr = convertTimestamp(postItem.timestamp)
                intent.putExtra("timestamp", timestampStr)
                itemView.context.startActivity(intent)
            }
        }

        // The grid of post pictures.
        fun bindView(item: NewsfeedItem) {
            Log.d("GVAdapter", "bindView(item: NewsfeedItem)")
            postItem = item
            var imageID = item.imageID
            // Set the image.
            // Reference to an image file in Cloud Storage
            Log.d("bidning for imageid: ", "" + item.imageID)
            if(item.imageID != null && item.imageID != "xxx") {
                Log.d("imageID: ", item.imageID)
                val mStorageRef = FirebaseStorage.getInstance().getReference()
                val childImage = mStorageRef.child(item.imageID!!)
                childImage.getBytes(1024*1024 / 2)
                    .addOnSuccessListener { bytes ->
                        var imageBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        imageView.setImageBitmap(imageBmp)
                    }

            } else {
                imageView.setImageResource(R.drawable.doll_face)   // use glide???
            }
        }
        private fun convertTimestamp(timestamp: String) : String {
            var timestampRegex = Regex("[A-Za-z]+\\s([A-Za-z]+)\\s(\\d+)\\s(\\d+):(\\d+):\\d+\\s([A-Z]+)\\s(\\d+)")
            // Comes in as "WEEKDAY MONTH DAY HOUR:MIN:SEC TIMEZONE YEAR"
            var str = ""
            val match = timestampRegex.find(timestamp)
            if(match != null) {
                val (month, day, milHour, min, zone, year) = match.destructured
                var hour = milHour.toInt()
                var time = "am"
                if(hour > 12) {
                    hour -= 12
                    time = "pm"
                }
                str = "$month $day, $year at $hour:$min $time"
                return str
            }
            return timestamp
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
