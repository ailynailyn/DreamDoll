package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.news_feed.*

// NewsFeedFrag.kt & news_feed.xml
class FallingShoesMenuFrag : Fragment() {

    private lateinit var rvAdapter: RVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.falling_shoes_menu, container, false)
    }

    private fun setButtonsVisibility(visibility: Int) {
        var buttonsLayout = view!!.findViewById<LinearLayout>(R.id.fallingShoesMenuButtonsLayout)
        buttonsLayout.visibility = visibility
    }

    private fun setHowToPlayVisibility(visibility: Int) {
        var howToPlayLayout = view!!.findViewById<LinearLayout>(R.id.howToPlayFallingShoesLayout)
        howToPlayLayout.visibility = visibility
    }

    private fun setLeadershipBoardVisibility(visibility: Int) {
        var leadershipBoardLayout = view!!.findViewById<LinearLayout>(R.id.leadershipBoardFallingShoesLayout)
        leadershipBoardLayout.visibility = visibility
    }

    private fun howToPlayClicked() {
        setButtonsVisibility(View.GONE)
        setHowToPlayVisibility(View.VISIBLE)

    }

    private fun leadershipBoardClicked() {
        setButtonsVisibility(View.GONE)
        setLeadershipBoardVisibility(View.VISIBLE)
    }

    private fun quitClicked() {
        (activity as FallingShoesActivity).supportFragmentManager.popBackStack()
        (activity as FallingShoesActivity).finish()
    }

    private fun initButtons() {
        // Menu buttons.
        var howToPlayButton = view!!.findViewById<Button>(R.id.fallingShoesHowToPlayButton)
        howToPlayButton.setOnClickListener {
            Log.d("menu fragment", "how to play button clicked")
            howToPlayClicked()
        }

        var leadershipBoardbutton = view!!.findViewById<Button>(R.id.fallingShoesLeadershipBoardButton)
        leadershipBoardbutton.setOnClickListener {
            Log.d("menu fragment", "leadership button clicked")
            leadershipBoardClicked()
        }

        var quitButton = view!!.findViewById<Button>(R.id.fallingShoesQuitButton)
        quitButton.setOnClickListener {
            Log.d("menu fragment", "quit button clicked")
            quitClicked()
        }

        // How to Play buttons.
        var howToPlayBackButton = view!!.findViewById<Button>(R.id.howToPlayFallingShoesBackButton)
        howToPlayBackButton.setOnClickListener {
            Log.d("howtoplay", "back button clicked")
            setHowToPlayVisibility(View.GONE)
            setButtonsVisibility(View.VISIBLE)
        }

        // Leadership Board buttons.
        var leadershipBoardButton = view!!.findViewById<Button>(R.id.leadershipButtonBackButton)
        leadershipBoardButton.setOnClickListener {
            Log.d("leadership board", "back button clicked")
            setLeadershipBoardVisibility(View.GONE)
            setButtonsVisibility(View.VISIBLE)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initButtons()

    }

}
