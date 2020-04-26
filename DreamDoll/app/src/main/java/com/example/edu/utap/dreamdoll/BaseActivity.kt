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
    OptionsMenuFrag.OptionsMenuButtonListener,
    StartupFrag.StartupButtonListener,
    LoginFrag.SignInSuccessListener
    {

    private lateinit var mAuth : FirebaseAuth
    var startup_frag = StartupFrag()   // for login and signup
    var newsfeed_frag = NewsFeedFrag()
    var optionsMenu_frag = OptionsMenuFrag()
        var login_frag = LoginFrag()
        var signin_frag = SignupFrag()

    var displayOptions = false;

    // Menu.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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

    // Menu options.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.options) {
//            Toast.makeText(this, "Options Clicked: menuisopen: $menuIsOpen. will do opposite", Toast.LENGTH_LONG).show()
            var fragment = supportFragmentManager.findFragmentByTag("menu_frag")
            if(fragment == null) {
                // Display the options.
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, optionsMenu_frag, "menu_frag")
                    .commit()
            } else {
                // Close the options.
                supportFragmentManager.beginTransaction().remove(optionsMenu_frag).commit()
            }
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    // Hides the menu options.
    fun displayOptionsMenu(shouldDisplay: Boolean) {
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

    // Newsfeed button was clicked.
    override fun newsFeedButtonClicked() {
        // Go to newsfeed page.
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, newsfeed_frag)
            .commit()
    }

    // Create a character button was clicked.
    override fun createCharButtonClicked() {

    }

    // Edit a character button was clicked.
    override fun editCharButtonClicked() {
        val intent = Intent(this, EditCharacterActivity::class.java)
        intent.putExtra("title", "madeit")
        startActivity(intent)
    }

    // Buy items button was clicked.
    override fun buyItemsButtonClicked() {

    }

    // Earn coins button was clicked.
    override fun earnCoinsButtonClicked() {

    }

    // Logout button was clicked.
    override fun logoutButtonClicked() {
        mAuth.signOut()
        displayOptionsMenu(false)
        // Go to sign in screen.
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, startup_frag)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        displayOptionsMenu(false)
        mAuth = FirebaseAuth.getInstance()


    }
}
