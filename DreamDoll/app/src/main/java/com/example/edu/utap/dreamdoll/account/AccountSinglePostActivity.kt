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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.account.AccountRVAdapter
import com.example.edu.utap.dreamdoll.newfeed.NewsfeedRVAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.edit_features.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.news_feed.*
import kotlinx.android.synthetic.main.profile_single_post.*

// EditFaceFrag.kt & edit_features.xml
class AccountSinglePostActivity : BaseActivity() {

    private lateinit var rvAdapter: AccountRVAdapter


    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("single post activity","onbackpressed")
        finish()
    }

    private fun setupPost(extras: Bundle) {

        val username = extras.getString("username").toString()
        val profilePicID = extras.getString("profilePicID").toString()
        val imageID = extras.getString("imageID").toString()
        val likes = extras.getInt("likes")
        val caption = extras.getString("caption").toString()
        val postID = extras.getString("postID").toString()
        val userID = extras.getString("userID").toString()
        val timestamp = extras.getString("timestamp").toString()

        var item = NewsfeedItem(username, profilePicID, imageID, likes, caption, postID, userID, timestamp)
        val list = mutableListOf<NewsfeedItem>()
        list.add(item)
        rvAdapter.setItemList(list)

    }

    // note to self: this might be more work than needed
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_single_post)
        displayOptionsMenu(true)

        rvAdapter = AccountRVAdapter()
        singlePostRV.adapter = rvAdapter
        singlePostRV.layoutManager = LinearLayoutManager(this)

        // Get extra information.
        var extras = intent.extras
        if(extras != null) {
            setupPost(extras)
        }

    }

}
