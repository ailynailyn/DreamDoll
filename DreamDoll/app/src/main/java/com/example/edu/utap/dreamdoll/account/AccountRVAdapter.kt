package com.example.edu.utap.dreamdoll.account

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class AccountRVAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<NewsfeedItem>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mAuth = FirebaseAuth.getInstance();
        private val db = Firebase.firestore
        private var postID = ""
        private var username = ""
        private var userID = ""
        private var imageID = ""
        private var isLiked = false
        private var postLikes = 0
        internal var profilePicIV = itemView.findViewById<ImageView>(R.id.accountPostProfilePic)
        internal var usernameTV = itemView.findViewById<TextView>(R.id.accountPostUsername)
        internal var image = itemView.findViewById<ImageView>(R.id.accountPostImage)
        internal var likesTV = itemView.findViewById<TextView>(R.id.accountPostLikesTV)
        internal var likeButton = itemView.findViewById<Button>(R.id.accountPostLikeButton)
        internal var caption = itemView.findViewById<TextView>(R.id.accountPostCaption)
        internal val setAsProfilePicButton = itemView.findViewById<Button>(R.id.accountPostProfilePicButton)
        internal val timestampTV = itemView.findViewById<TextView>(R.id.accountPostTimestamp)

        init {
            itemView.setOnClickListener {
                Log.d("RVAdapter", "item clicked ${usernameTV.text}")

            }

            // Find out if the picture is liked.
            var userRef = db.collection("users").document(mAuth.currentUser!!.uid)
            userRef.get()
                .addOnSuccessListener {
                    var data = it.data
                    Log.d("data: ", data.toString())
                    if(data != null) {
                        var likesList: ArrayList<String> = data["likes"] as ArrayList<String>
                        likesList.forEach { post ->
                            if(post == postID) {
                                isLiked = true
                            }
                        }
                        Log.d("After going through likes", "current post is liked: $isLiked")
                    }
                }
                .addOnFailureListener {
                    Log.d("Could not get user data", "Failed")
                }

            likeButton.setOnClickListener {
                Log.d("newfeed adapter", "like button clicked for post $postID")
                //var isLiked = true

                var likes = postLikes
                if(isLiked) {
                    Log.d("was liked", "going to unlike")
                    isLiked = false
                    // Change to unlike.
                    likeButton.text = "♡"

                    postLikes = likes - 1
                    updateLikesTV(postLikes)

                    // Update post likes in database.
                    updateLikesInDatabase(postLikes)

                    // Remove post from user's likes.
                    updatePostInUserLikes(FieldValue.arrayRemove(postID))


                } else {
                    Log.d("was unliked", "going to like")
                    isLiked = true
                    // Change button to like.
                    likeButton.text = "❤"

                    postLikes = likes + 1
                    updateLikesTV(postLikes)

                    // Update post likes in database.
                    updateLikesInDatabase(postLikes)

                    // Add post to user's likes.
//                    mAuth.currentUser!!.uid
                    updatePostInUserLikes(FieldValue.arrayUnion(postID))

                }
            }
            setAsProfilePicButton.setOnClickListener {
                Log.d("set as profile pic clicked", "clicked!")
                db.collection("users").document(userID)
                    .update("profilePicID", imageID)
                    .addOnSuccessListener {
                        Log.d("updated profiles pic for current user", "$imageID")
                        // Set the actual image.
                        val mStorageRef = FirebaseStorage.getInstance().getReference()
                        val childImage = mStorageRef.child(imageID)
                        childImage.getBytes(1024*1024)
                            .addOnSuccessListener { bytes ->
                                var imageBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                profilePicIV.setImageBitmap(imageBmp)
                            }
                    }
                    .addOnFailureListener {

                    }

            }
        }

        // Updates the likes array for the current user.
        private fun removePostFromUserLikes() {
            val temp = hashMapOf("likes" to FieldValue.arrayRemove(postID))
            db.collection("users").document(mAuth.currentUser!!.uid)
                .update(temp as Map<String, Any>)       // whats up with that??
                .addOnSuccessListener {
                    Log.d("updated user likes array", "removed")
                }
                .addOnFailureListener {
                    Log.d("failed", "couldnt remove likes")
                }
        }

        // Updates the likes array for the current user.
        private fun updatePostInUserLikes(value: FieldValue) {
            val temp = hashMapOf("likes" to value)
            db.collection("users").document(mAuth.currentUser!!.uid)
                .update(temp as Map<String, Any>)       // whats up with that??
                .addOnSuccessListener {
                    Log.d("updated user likes array", "union!")
                }
                .addOnFailureListener {
                    Log.d("failed", "couldnt remove likes")
                }
        }

        private fun updateLikesTV(count: Int) {
            likesTV.text = "$count Likes"
        }

        private fun updateLikesInDatabase(count: Int) {
            updateNewsfeedPostLikes(count)
            updateUserPostLikes(count)
        }

        // Updates the likes count on the current post in the database.
        // For the posts in the newsfeed collection.
        private fun updateNewsfeedPostLikes(count: Int) {
            db.collection("newsfeed").document(postID).update("likes", count)
                .addOnSuccessListener {
                    Log.d("SUCCESS", "Updated likes ($count) for post $postID")
                }
                .addOnFailureListener {
                    Log.d("FAILED", "Couldn't update likes ($count) for post $postID")
                }
        }

        // Updates the likes count on the current post in the database.
        // For the posts in the
        private fun updateUserPostLikes(count: Int) {
            db.collection("users").document(userID)
                .collection("posts").document(postID)
                .update("likes", count)
        }

        private fun setLikedHeart() {
            Log.d("current user id:", mAuth.currentUser!!.uid)
            db.collection("users").document(mAuth.currentUser!!.uid).get()
                .addOnSuccessListener { doc ->
                    var likesList: List<String> = doc.get("likes") as List<String>
                    likesList.forEach { curID ->
                        if(curID == postID) {
                            isLiked = true
                            likeButton.text = "❤"
                            return@addOnSuccessListener
                        }
                    }
                }
                .addOnFailureListener {
                    Log.d("Could not find current users likes", "failed")
                }
        }

        private fun setProfilePic(uuid: String) {
            var userRef = db.collection("users").document(uuid)
            userRef.get().addOnSuccessListener {
                var data = it.data
                if(data != null) {
                    var profilePicID = data["profilePicID"]
                    Log.d("bidning for profile pic: ", "" + profilePicID)
                    if(profilePicID != null && profilePicID != "xxx") {
                        Log.d("profilePICID: ", profilePicID.toString())
                        val mStorageRef = FirebaseStorage.getInstance().getReference()
                        val childImage = mStorageRef.child(profilePicID.toString())
                        childImage.getBytes(1024*1024)
                            .addOnSuccessListener { bytes ->
                                var imageBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                profilePicIV.setImageBitmap(imageBmp)
                            }

                    }
                }
            }
        }

        fun bindView(item: NewsfeedItem) {
            Log.d("RVAdapter", "bindView(item: NewsfeedItem)")
//            profilePicIV.setImageResource(item.profilePicID)
//            if(item.profilePicID != null && item.profilePicID != "xxx") {
////                Log.d("profilePicID: ", item.profilePicID)
////                var profilePicID = item.profilePicID
////                val mStorageRef = FirebaseStorage.getInstance().getReference()
////                val childImage = mStorageRef.child(profilePicID)
////                childImage.getBytes(1024*1024/4)
////                    .addOnSuccessListener { bytes ->
////                        var imageBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
////                        profilePicIV.setImageBitmap(imageBmp)
////                    }
////
////            }
            setProfilePic(mAuth.currentUser!!.uid)





            username = item.username
            usernameTV.text = username
//            image.setImageResource(item.imageID)

            // Set the image.
            // Reference to an image file in Cloud Storage
            Log.d("bidning for imageid: ", "" + item.imageID)
            if(item.imageID != null && item.imageID != "xxx") {
                Log.d("imageID: ", item.imageID)
                imageID = item.imageID
                val mStorageRef = FirebaseStorage.getInstance().getReference()
                val childImage = mStorageRef.child(item.imageID!!)
                childImage.getBytes(1024*1024)
                    .addOnSuccessListener { bytes ->
                        var imageBmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        image.setImageBitmap(imageBmp)
                    }

            }


//            likesTV.text = "${item.likes} Likes"
            updateLikesTV(item.likes)
            caption.text = item.caption
            postID = item.postID
            postLikes = item.likes
            userID = item.userID
            if(item.timestamp == null || item.timestamp == "null") {
                timestampTV.text = ""
            } else {
                timestampTV.text = item.timestamp
            }


            // Set isLiked variable. Depends Also sets the text of the like button.
            setLikedHeart()

            usernameTV.setOnClickListener {
                Log.d("username clicked", "${usernameTV.text}")
                // Go to user profile.
                val intent = Intent(itemView.context, UserProfileActivity::class.java)
                intent.putExtra("username", usernameTV.text.toString())
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.account_post_item, parent, false))
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
