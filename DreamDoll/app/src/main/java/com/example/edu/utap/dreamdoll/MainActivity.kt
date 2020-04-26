package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity :
    BaseActivity(),
    SignupFrag.SignUpSuccessListener
     {



    // Hides keyboard.
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    // Hides keyboard.
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // Hides keyboard.
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }







    // Sign in. Do any necessary updates.
    override fun signUpSuccessful() {
        Log.d("MainActivity", "signInSuccessful")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, newsfeed_frag)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("main activity", "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var curUser = mAuth.currentUser

        if(curUser == null) {
            Log.d("cur user is null", "going to signuplogin")
            // Start in the login/signup screen.
            displayOptionsMenu(false)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, startup_frag)
                .commit()
        } else {
            Log.d("signed in.", "going to newsfeed")
            // else bring them to the main feed
            displayOptionsMenu(true)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, newsfeed_frag)
                .addToBackStack(null)
                .commit()
        }
    }
}
