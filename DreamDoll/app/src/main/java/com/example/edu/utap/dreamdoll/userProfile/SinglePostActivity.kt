package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import kotlinx.android.synthetic.main.edit_features.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*

// EditFaceFrag.kt & edit_features.xml
class SinglePostActivity : BaseActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("single post activity","onbackpressed")
        finish()
    }

    private fun setupPost(extras: Bundle) {
        val profilePicIV = findViewById<ImageView>(R.id.singlePostProfilePic)
        val usernameTV = findViewById<TextView>(R.id.singlePostUsername)
        var postIV = findViewById<ImageView>(R.id.singlePostImage)
        val likesTV = findViewById<TextView>(R.id.singlePostLikesTV)
        val likeButton = findViewById<Button>(R.id.singlePostLikeButton)
        val captionTV = findViewById<TextView>(R.id.singlePostCaption)
        val profilePicID = extras.getString("profilePicID")
        val username = extras.getString("username")
        val postImage = extras.getString("imageID")
        val likes = extras.getInt("likes")
        val caption = extras.getString("caption")

        // Set profile pic.
        //

        usernameTV.text = username
        // Set image pic.
        //

        val tmp = "$likes Likes"
        likesTV.text = tmp
        captionTV.text = caption

        likeButton.setOnClickListener {
            Log.d("SinglePostActivity", "backButtonPressed")
        }

    }

    // note to self: this might be more work than needed
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_single_post)
        displayOptionsMenu(true)

        // Get extra information.
        var extras = intent.extras
        if(extras != null) {
            setupPost(extras)
        }

    }

}
