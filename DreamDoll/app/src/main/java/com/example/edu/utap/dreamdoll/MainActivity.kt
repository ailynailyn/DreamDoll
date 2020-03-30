package com.example.edu.utap.dreamdoll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), StartupFrag.StartupButtonListener {

    private lateinit var mAuth : FirebaseAuth
    var startup_frag = StartupFrag()   // for login and signup
    var login_frag = LoginFrag()
    var signin_frag = SignupFrag()

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
                .replace(R.id.container, startup_frag)
                .commit()
        }
    }
}
