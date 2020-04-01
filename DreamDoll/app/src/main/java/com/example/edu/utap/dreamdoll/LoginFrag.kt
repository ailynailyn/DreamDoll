package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*

// LoginFrag.kt & login.xml
class LoginFrag : Fragment() {

    private var mAuth = FirebaseAuth.getInstance();
    var signInSuccessListener: SignInSuccessListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.login, container, false)
    }

    interface SignInSuccessListener {
        fun signInSuccessful()
        fun forgotPassword()
    }

    fun reset() {
        login_emailET.getText().clear()
        login_passwordET.getText().clear()
        login_invalidEmailTV.visibility = View.INVISIBLE
        login_invalidPasswordTV.visibility = View.INVISIBLE
    }

    // Code to hide the keyboard.
    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reset()
        // Set up forgot password button.
        login_forgotButton.setOnClickListener {
            Log.d("Loginfrag", "forgot button clicked")
            signInSuccessListener?.forgotPassword()
        }

        // Set up login button.
        login_button.setOnClickListener {
            hideKeyboard()
            var email = login_emailET.text.toString()
            var password = login_passwordET.text.toString()
            login_invalidEmailTV.visibility = View.INVISIBLE
            login_invalidPasswordTV.visibility = View.INVISIBLE
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                // Check firebase.
                Log.d("login", "email: $email. password: $password")
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success.
                                Log.d("XXX", "signInWithEmail:success");
                                // Go to main screen.
                                signInSuccessListener?.signInSuccessful()
                                Log.d("XXX", "did sign in successful");
                            } else {
                                // Sign in failed. Display message.
                                Log.w("XXX", "signInWithEmail:failure", task.getException());
                                Log.w("XXX", "signInWithEmail:localizedMessage: ${task.getException()!!.localizedMessage}");
                                var errorMsg = task.getException()!!.localizedMessage
                                if(Regex("email address is badly formatted").containsMatchIn(errorMsg)) {
                                    login_invalidEmailTV.visibility = View.VISIBLE
                                    Log.d("XXX", "in email address badly format")
                                } else if (Regex("password is invalid").containsMatchIn(errorMsg)) {
                                    login_invalidPasswordTV.text = "Invalid password."
                                    login_invalidPasswordTV.visibility = View.VISIBLE
                                } else if (Regex("no user record corresponding to this identifier").containsMatchIn(errorMsg)) {
                                    login_invalidEmailTV.visibility = View.VISIBLE
                                } else if (Regex("Too many unsuccessful login attempts").containsMatchIn(errorMsg)) {
                                    login_invalidPasswordTV.text = "Too many unsuccessful login attempts.\nPlease try again later."
                                    login_invalidPasswordTV.visibility = View.VISIBLE
                                } else {
                                    login_invalidPasswordTV.text = errorMsg
                                    login_invalidPasswordTV.visibility = View.VISIBLE
                                }
                            }
                    }
            }
        }
    }

    /// makes sure interfaces are implemented
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signInSuccessListener = context as? SignInSuccessListener
        if(signInSuccessListener == null) {
            throw ClassCastException("$context must implement SignInSuccessListener")
        }
    }
}
