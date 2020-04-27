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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.userProfile.ProfileGVAdapter
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.news_feed.*

// NewsFeedFrag.kt & news_feed.xml
class UserProfileFrag : Fragment() {

    private lateinit var userProfileRV : RecyclerView
    private lateinit var gridLayoutManager : GridLayoutManager
    private val profileGVAdapter = ProfileGVAdapter()
    private val repository = Repository()
    private val numCols = 3

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.user_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initGrid()

        // Set adapter.
        userProfileRV.adapter = profileGVAdapter

        // Fetch the information about the user. Will have to get data from srver
        profileGVAdapter.setItemList(repository.fetchUserPics())

    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        userProfileRV = view!!.findViewById(R.id.userProfileRV)
        gridLayoutManager = GridLayoutManager(this.context, numCols)
        userProfileRV.layoutManager = gridLayoutManager
    }

}
