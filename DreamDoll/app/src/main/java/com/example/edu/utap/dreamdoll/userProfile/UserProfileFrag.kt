package com.example.edu.utap.dreamdoll

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.userProfile.ProfileGVAdapter
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.news_feed.*
import kotlinx.android.synthetic.main.user_profile.*

// NewsFeedFrag.kt & news_feed.xml
class UserProfileFrag(username : String) : Fragment() {

    private lateinit var userProfileRV : RecyclerView
    private lateinit var gridLayoutManager : GridLayoutManager
    private val profileGVAdapter = ProfileGVAdapter()
    private val repository = Repository()
    private val numCols = 3
    private val db = Firebase.firestore
    private val curUsername = username


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.user_profile, container, false)
    }

    // Grabs user info to generate posts and grid view.
    private fun genUserPosts(uuid: String) {
        var postsList = mutableListOf<NewsfeedItem>()
        db.collection("users")
            .document(uuid)
            .collection("posts")
            .get()
            .addOnSuccessListener { posts ->
                posts.forEach {
                    var curPost = it.data
                    var postID = it.id
                    Log.d("curPost", curPost.toString())
                    val username: String = curUsername
                    val profilePicID: String? = null
                    val imageID: String? = curPost["pictureID"].toString()
                    val likes: Int = (curPost["likes"] as Long).toInt()
                    val caption: String = curPost["caption"].toString()
                    var timestamp = (curPost["timestamp"] as Timestamp).toDate()
                    var timestampStr = convertTimestamp(timestamp.toString())
                    Log.d("timestampStr", timestampStr)
                    var item = NewsfeedItem(username, profilePicID, imageID, likes, caption, postID, uuid, timestampStr)
                    postsList.add(item)
                }
                Log.d("postList inside listener: ", postsList.toString())
                // Adjust total posts tv.
                var totalPostsTV = view!!.findViewById<TextView>(R.id.userProfile_postsTV)
                totalPostsTV.text = postsList.count().toString()
                // Submit to adapter.
                profileGVAdapter.setItemList(postsList)

            }
            .addOnFailureListener {
                Log.d("Could not get user posts data from database", "FAILED")
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

    // Uses the username collection to get the uuid of the given user.
    private fun prepareProfile() {
        // Get user id from database.
        db.collection("usernames").document(curUsername).get()
            .addOnSuccessListener {  doc ->
                var userID = doc.get("uuid").toString()
                Log.d("data grabbed for $curUsername", userID)
                setUserData(userID)
                genUserPosts(userID)
            }
            .addOnFailureListener {
                Log.d("couldnt find existing user in username database", "FAILED")
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

                            userProfile_image.setImageBitmap(imageBmp)
                        }

                }
            }
        }
    }

    // Sets the username, likes and high score.
    private fun setUserData(uuid: String) {
        setProfilePic(uuid)
        var usernameTV = view!!.findViewById<TextView>(R.id.userProfile_username)
        var highScoreTV = view!!.findViewById<TextView>(R.id.userProfile_highScoreTV)
        var coinsTV = view!!.findViewById<TextView>(R.id.userProfile_coinsTV)
        usernameTV.text = curUsername
        db.collection("users").document(uuid).get()
            .addOnSuccessListener {  doc ->
                var highScore = doc.get("fallingShoesHighScore").toString()
                var coins = doc.get("coins").toString()
                Log.d("high score grabbed for $curUsername", highScore)
                Log.d("coins grabbed for $curUsername", coins)
                highScoreTV.text = highScore.toString()
                coinsTV.text = coins.toString()
            }
            .addOnFailureListener {
                Log.d("couldnt find existing user in username database", "FAILED")
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initGrid()

        // Set adapter.
        userProfileRV.adapter = profileGVAdapter

        // Need to get the user pics from the database.
        prepareProfile()

    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        userProfileRV = view!!.findViewById(R.id.userProfileRV)
        gridLayoutManager = GridLayoutManager(this.context, numCols)
        userProfileRV.layoutManager = gridLayoutManager
    }

}
