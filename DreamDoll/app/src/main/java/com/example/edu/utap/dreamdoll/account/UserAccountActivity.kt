package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.account.AccountGVAdapter
import com.example.edu.utap.dreamdoll.userProfile.ProfileGVAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.edit_features.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.user_account.*

// EditFaceFrag.kt & edit_features.xml
class UserAccountActivity : BaseActivity() {

    private lateinit var userAccountRV : RecyclerView
    private lateinit var gridLayoutManager : GridLayoutManager
    private val accountGVAdapter = AccountGVAdapter()
    private val numCols = 3
    private val db = Firebase.firestore
    private val curUsername = ""


    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("profile activity","onbackpressed")
        finish()
    }

    // Grabs user info to generate posts and grid view.
    private fun genUserPosts(uuid: String, curUsername: String) {
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
                    val profilePicID: String? = curPost["profilePicID"].toString()
                    val imageID: String? = curPost["pictureID"].toString()
                    val likes: Int = (curPost["likes"] as Long).toInt()
                    val caption: String = curPost["caption"].toString()
                    val timestamp = curPost["timestamp"].toString()
                    val timestampStr = convertTimestamp(timestamp)
                    var item = NewsfeedItem(username, profilePicID, imageID, likes, caption, postID, uuid, timestamp)
                    postsList.add(item)
                }
                Log.d("postList inside listener: ", postsList.toString())
                // Adjust total posts tv.
                var totalPostsTV = findViewById<TextView>(R.id.userProfile_postsTV)
                totalPostsTV.text = postsList.count().toString()
                // Submit to adapter.
                accountGVAdapter.setItemList(postsList)

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

    // Sets the username, likes and high score.
    private fun setUserData(uuid: String, curUsername: String) {
        var usernameTV = findViewById<TextView>(R.id.userProfile_username)
        var highScoreTV = findViewById<TextView>(R.id.userProfile_highScoreTV)
        var coinsTV = findViewById<TextView>(R.id.userProfile_coinsTV)
        usernameTV.text = curUsername
        db.collection("users").document(uuid).get()
            .addOnSuccessListener {  doc ->
                var highScore = doc.get("fallingShoesHighScore").toString()
                var coins = doc.get("coins").toString()
                Log.d("high score grabbed for $curUsername", highScore)
                Log.d("coins grabbed for $curUsername", coins)
                highScoreTV.text = highScore.toString()
                coinsTV.text = coins.toString()
                // Set profile pic
                setProfilePic(uuid)
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_account)
        displayOptionsMenu(true)
        initGrid()

        // Set adapter.
        userAccountRV.adapter = accountGVAdapter

        var userID = mAuth.currentUser!!.uid

        var ref = db.collection("users").document(userID)
        ref.get()
            .addOnSuccessListener {
                Log.d("it.data", it.data.toString())
                if(it.data != null) {
                    var temp = it.data!!["username"]
                    var username = temp.toString()
                    setUserData(userID, username)
                    genUserPosts(userID, username)
                }
            }


    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        userAccountRV = findViewById(R.id.userProfileRV)
        gridLayoutManager = GridLayoutManager(this, numCols)
        userAccountRV.layoutManager = gridLayoutManager
    }

}
