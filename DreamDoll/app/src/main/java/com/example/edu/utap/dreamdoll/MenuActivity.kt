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
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.options_menu.*

// MenuActivity.kt & options_menu.xml
class MenuActivity : BaseActivity() {

//    var buttonListener: OptionsMenuButtonListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.options_menu)
        initButtons()
    }


    // Newsfeed button was clicked.
    fun newsFeedButtonClicked() {
        displayOptionsMenu(true)
        // Go to newsfeed page.
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, newsfeed_frag)
            .commit()
    }

    // Create a character button was clicked.
    fun createCharButtonClicked() {

    }

    // Edit a character button was clicked.
    fun editCharButtonClicked() {
        val intent = Intent(this, EditCharacterActivity::class.java)
        intent.putExtra("title", "madeit")
        startActivity(intent)
    }

    // Buy items button was clicked.
    fun buyItemsButtonClicked() {

    }

    // Earn coins button was clicked.
    fun earnCoinsButtonClicked() {

    }

    // Logout button was clicked.
    fun logoutButtonClicked() {
        mAuth.signOut()
        displayOptionsMenu(false)
        // Go to sign in screen.
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, startup_frag)
            .commit()
    }

     fun initButtons() {
        // Sets up the buttons.
        news_feed.setOnClickListener {
            Log.d("options menu fragment", "newfeed button clicked")
            newsFeedButtonClicked()
        }
        create_character.setOnClickListener {
            Log.d("options menu fragment", "create character button clicked")
            createCharButtonClicked()
        }
        edit_character.setOnClickListener {
            Log.d("options menu fragment", "edit character button clicked")
            editCharButtonClicked()
        }
        buy_items.setOnClickListener {
            Log.d("options menu fragment", "buy items button clicked")
            buyItemsButtonClicked()
        }
        earn_coins.setOnClickListener {
            Log.d("options menu fragment", "earn coins button clicked")
            earnCoinsButtonClicked()
        }
        logout.setOnClickListener {
            Log.d("options menu fragment", "logout button clicked")
            logoutButtonClicked()
        }
    }

}
