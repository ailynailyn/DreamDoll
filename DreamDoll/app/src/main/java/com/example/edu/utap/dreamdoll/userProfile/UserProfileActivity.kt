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
class UserProfileActivity : BaseActivity() {

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("profile activity","onbackpressed")
        finish()
    }

    private fun beginProfileFrag(username: String) {
        var userProfileFrag = UserProfileFrag(username)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.container, userProfileFrag)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayOptionsMenu(true)

        // Get extra information.
        var extras = intent.extras
        if(extras != null) {
            var currentUsername = intent.extras!!.getString("username")
            Log.d("oncreate user profile activity", "$currentUsername")
            if(!currentUsername.isNullOrEmpty()) {
                beginProfileFrag(currentUsername)
            }
        }

    }

}
