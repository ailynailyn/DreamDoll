package com.example.edu.utap.dreamdoll

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.news_feed.*

// NewsFeedFrag.kt & news_feed.xml
class EarnCoinsFrag : Fragment() {

    private lateinit var rvAdapter: RVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.earn_coins, container, false)
    }

    private fun initButtons() {
        var shoesButton = view!!.findViewById<Button>(R.id.fallingShoesGameButton)
        shoesButton.setOnClickListener {
            Log.d("Catch the shoes clicked", "going to game")
            val intent = Intent(this.context, FallingShoesActivity::class.java)
            intent.putExtra("going to falling shoes activity", "madeit")
            startActivity(intent)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initButtons()

    }

}
