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

open class BaseActivity :
    AppCompatActivity(),
    StartupFrag.StartupButtonListener,
    LoginFrag.SignInSuccessListener
    {

        lateinit var mAuth : FirebaseAuth
    var startup_frag = StartupFrag()   // for login and signup
    var newsfeed_frag = NewsFeedFrag()
        var login_frag = LoginFrag()
        var signin_frag = SignupFrag()

    var displayOptions = false;

    // Menu.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d("creating options menu", "yup")
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        var optionsButton = menu.findItem(R.id.options)
        if(displayOptions) {
            supportActionBar?.show()
            optionsButton.setVisible(true)
        } else {
            supportActionBar?.hide()
            optionsButton.setVisible(false)
        }
        return true
    }

//        override fun onBackPressed() {
//            super.onBackPressed()
//            Log.d("Back pressed", "yup")
//            if(menuFragIsPresent()) {
//                displayOptionsMenu(true)
//                supportFragmentManager.beginTransaction().remove(optionsMenu_frag).commit()
//            } else {
//                Log.d("should have", "done something else...?")
//            }
//        }

        fun menuFragIsPresent() : Boolean {
            var fragment = supportFragmentManager.findFragmentByTag("menu_frag")
            if(fragment == null) {
                return false
            }
            return true
        }

    // Menu options.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("in onOptionsItemSelected", "yup")
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.options) {
//            Toast.makeText(this, "Options Clicked: menuisopen: $menuIsOpen. will do opposite", Toast.LENGTH_LONG).show()
//            if(!menuFragIsPresent()) {
                // Display the options.
//                displayOptionsMenu(false)
//                supportFragmentManager
//                    .beginTransaction()
//                    .add(R.id.container, optionsMenu_frag, "menu_frag")
//                    .commit()
                // Start the activity
                val intent = Intent(this, MenuActivity::class.java)
                intent.putExtra("going to options menu", "madeit")
                startActivity(intent)
//            } else {
//                displayOptionsMenu(true)
//                // Close the options.
////                supportFragmentManager.beginTransaction().remove(optionsMenu_frag).commit()
//            }
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    // Hides the menu options.
    fun displayOptionsMenu(shouldDisplay: Boolean) {
        Log.d("displaying menu: ", "$shouldDisplay")
        displayOptions = shouldDisplay
        invalidateOptionsMenu()
    }

        // Forgot password was clicked.
        override fun forgotPassword() {
            Log.d("MainActivity", "forgotPassword")
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, ForgotPasswordFrag())
                .addToBackStack(null)
                .commit()
        }

        // Signin button was clicked.
        override fun signupButtonClicked() {
            Log.d("MainActivity", "signin button clicked listener in mainactivity")
            // Go to login screen.
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, signin_frag)
                .addToBackStack(null)
                .commit()
        }

        // Login button was clicked.
        override fun loginButtonClicked() {
            Log.d("MainActivity", "login button clicked listener in mainactivity")
            // Go to login screen.
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, login_frag)
                .addToBackStack(null)
                .commit()
        }

        // Sign in. Do any necessary updates.
        override fun signInSuccessful() {
            Log.d("MainActivity", "signInSuccessful")
            displayOptionsMenu(true)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, newsfeed_frag)
                .addToBackStack(null)
                .commit()
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        displayOptionsMenu(false)
        mAuth = FirebaseAuth.getInstance()


    }
}
