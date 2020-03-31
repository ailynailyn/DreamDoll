package com.example.edu.utap.dreamdoll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity :
    AppCompatActivity(),
    StartupFrag.StartupButtonListener,
    OptionsMenuFrag.OptionsMenuButtonListener,
    SignupFrag.SignUpSuccessListener,
    LoginFrag.SignInSuccessListener {

    private lateinit var mAuth : FirebaseAuth
    var startup_frag = StartupFrag()   // for login and signup
    var login_frag = LoginFrag()
    var signin_frag = SignupFrag()
    var newsfeed_frag = NewsFeedFrag()
    var optionsMenu_frag = OptionsMenuFrag()
    var menuIsOpen = false

    // Menu.
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // Menu options.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == R.id.options) {
            Toast.makeText(this, "Options Clicked: menuisopen: $menuIsOpen. will do opposite", Toast.LENGTH_LONG).show()
            var fragment = supportFragmentManager.findFragmentByTag("menu_frag")
            if(fragment == null) {
                // Display the options.
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, optionsMenu_frag, "menu_frag")
                    .commit()
                menuIsOpen = true
            } else {
                // Close the options.
                supportFragmentManager.beginTransaction().remove(optionsMenu_frag).commit()
                menuIsOpen = false
            }
            return true
        }
        return super.onOptionsItemSelected(item)

    }

    // Create a character button was clicked.
    override fun createCharButtonClicked() {

    }

    // Edit a character button was clicked.
    override fun editCharButtonClicked() {

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
        // Go to sign in screen.
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, startup_frag)
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

    // Sign in. Do any necessary updates.
    override fun signInSuccessful() {
        Log.d("MainActivity", "signInSuccessful")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, newsfeed_frag)
            .addToBackStack(null)
            .commit()
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        var curUser = mAuth.currentUser

        if(curUser == null) {
            // Start in the login/signup screen.
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, startup_frag)
                .commit()
        } else {
            // else bring them to the main feed
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, newsfeed_frag)
                .addToBackStack(null)
                .commit()
        }
    }
}
