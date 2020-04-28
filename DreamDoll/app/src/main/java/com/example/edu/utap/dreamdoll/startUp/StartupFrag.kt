package com.example.edu.utap.dreamdoll.startUp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.edu.utap.dreamdoll.R
import kotlinx.android.synthetic.main.login_signup.*

// StartupFrag.kt & login_signup.xml
class StartupFrag : Fragment() {

    var buttonListener: StartupButtonListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.login_signup, container, false)
    }

    // Notifies activity which button was clicked.
    interface StartupButtonListener {
        fun loginButtonClicked()
        fun signupButtonClicked()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Sets up the login and signup buttons.
        loginButton.setOnClickListener {
            Log.d("Startup fragment", "login button clicked")
            buttonListener?.loginButtonClicked()
        }

        signupButton.setOnClickListener {
            Log.d("Startup fragment", "sign up button clicked")
            buttonListener?.signupButtonClicked()
        }

    }

    // Makes sure interfaces are implemented.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        buttonListener = context as? StartupButtonListener
        if(buttonListener == null) {
            throw ClassCastException("$context must implement StartupButtonListener")
        }
    }
}
