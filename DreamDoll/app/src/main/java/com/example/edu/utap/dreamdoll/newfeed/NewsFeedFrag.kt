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
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.news_feed.*

// NewsFeedFrag.kt & news_feed.xml
class NewsFeedFrag : Fragment() {

    private lateinit var rvAdapter: NewsfeedRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.news_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set up.
        rvAdapter = NewsfeedRVAdapter()
        newsfeed_RV.adapter = rvAdapter
        newsfeed_RV.layoutManager = LinearLayoutManager(this.context)

        // Fetch data.
        var sampleList = listOf<NewsfeedItem>(
            NewsfeedItem("cupofcoffee",null, null, 3, "Caption for my picture. Woo! temproray" ),
            NewsfeedItem("ilovedressupgames",null, null, 35, "Check out this doll!!! placeholder " ),
            NewsfeedItem("patrick",null, null, 435, "this is a temporary caption" )
            )
        rvAdapter.setItemList(sampleList)

    }

}
