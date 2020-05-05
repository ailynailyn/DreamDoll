package com.example.edu.utap.dreamdoll

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.newfeed.NewsfeedRVAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.news_feed.*

// NewsFeedFrag.kt & news_feed.xml
class NewsFeedFrag : Fragment() {

    private lateinit var rvAdapter: NewsfeedRVAdapter
    private val db = Firebase.firestore
    private val postList = mutableListOf<NewsfeedItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.news_feed, container, false)
    }

    private fun genNewsfeedList() {
        var list = mutableListOf<NewsfeedItem>()
        db.collection("newsfeed").get()
            .addOnSuccessListener { posts ->
                posts.forEach {
                    var postID = it.id
                    var curPost = it.data
                    var userID = curPost["userID"].toString()
                    var username = curPost["username"].toString()
                    var imageID = curPost["pictureID"].toString()       // Need to set correctly.
                    var likes = curPost["likes"].toString()
                    var caption = curPost["caption"].toString()
                    // Generate newsfeeditem for each post.
                    var item = NewsfeedItem(username,null, null, likes.toInt(), caption)
                    list.add(item)
                }
                // Submit to adapter.
                rvAdapter.setItemList(list)
            }
            .addOnFailureListener {
                Log.d("couldnt fetch  newsfeed list", "FAILED")
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set up.
        rvAdapter = NewsfeedRVAdapter()
        newsfeed_RV.adapter = rvAdapter
        newsfeed_RV.layoutManager = LinearLayoutManager(this.context)

        genNewsfeedList()

    }

}
