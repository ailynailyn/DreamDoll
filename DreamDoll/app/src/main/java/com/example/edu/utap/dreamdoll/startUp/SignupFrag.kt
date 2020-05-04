package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
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
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.signup.*

// SignupFrag.kt & signup.xml
class SignupFrag : Fragment() {

    private var mAuth = FirebaseAuth.getInstance();
    var signUpSuccessListener: SignUpSuccessListener? = null
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.signup, container, false)
    }

    interface SignUpSuccessListener {
        fun signUpSuccessful()
    }

    fun reset() {
        signup_emailET.getText().clear()
        signup_passwordET.getText().clear()
        signup_invalidEmailTV.visibility = View.INVISIBLE
        signup_invalidPasswordTV.visibility = View.INVISIBLE
        signup_invalidUsernameTV.visibility = View.INVISIBLE
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
        // Set up login button.
        signup_button.setOnClickListener {
            hideKeyboard()
            var email = signup_emailET.text.toString()
            var password = signup_passwordET.text.toString()
            var username = signup_usernameET.text.toString()
            signup_invalidEmailTV.visibility = View.INVISIBLE
            signup_invalidPasswordTV.visibility = View.INVISIBLE
            signup_invalidUsernameTV.visibility = View.INVISIBLE
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty() && !username.isNullOrEmpty()) {



                // First check if the provided username is valid.
                var isValid = false
                val usersRef = db.collection("users").document(username)

//                usersRef.get()
//                    .addOnCompleteListener { task ->
//                        if(task.isSuccessful) {
//                            var doc = task.result
//                            if (doc != null && doc.exists()) {
//                                Log.d("document exists", username)
//                                isValid = false
//                            } else {
//                                isValid = true
//                            }
//                        } else {
//                            isValid = false // should this be set as false or true?
//                        }
//                    }

                db.collection("users").document(username).get()
                    .addOnSuccessListener {
                        Log.d("username exists.", "${it.data}")
                        if(it.data == null) {
                            Log.d("username does not exist.", "create user.")
                            isValid = true
                            createUser(email, password, username)
                        } else {
                            isValid = false
                            signup_invalidUsernameTV.visibility = View.VISIBLE
                        }

                    }
                    .addOnFailureListener {
                        Log.d("get doc failure", "couldnt do it?.")
                    }



//
//                if(isValid) {
//                    createUser(email, password)
//                } else {
//                    signup_invalidUsernameTV.visibility = View.VISIBLE
//                }
            }
        }
    }

    private fun createUser(email: String, password: String, username: String) {
        // Check firebase.
        Log.d("sign up", "email: $email. password: $password")
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign up success.
                    Log.d("XXX", "signUPWithEmail:success");
                    // Create user storage.
                    val userData = hashMapOf(
                        "coins" to "0",
                        "fallingShoesHighScore" to "0",
                        "email" to "$email",
                        "userID" to mAuth.currentUser!!.uid
                    )
                    db.collection("users")
                        .document(username)
                        .set(userData)
                        .addOnSuccessListener {
                            Log.d("added data", "initial userdata")
                            // Go to main screen.
                            signUpSuccessListener?.signUpSuccessful()
                        }
                        .addOnFailureListener {
                            Log.d("Could not add initial coins to database", "FAILED")
                        }


                } else {
                    // Sign up failed. Display message.
                    Log.w("XXX", "signUPWithEmail:failure", task.getException());
                    Log.w(
                        "XXX",
                        "signInWithEmail:localizedMessage: ${task.getException()!!.localizedMessage}"
                    );
                    var errorMsg = task.getException()!!.localizedMessage
                    if (Regex("email address is badly formatted").containsMatchIn(
                            errorMsg
                        )
                    ) {
                        signup_invalidEmailTV.visibility = View.VISIBLE
                        Log.d("XXX", "in email address badly format")
                    } else if (Regex("should be at least 6 characters").containsMatchIn(
                            errorMsg
                        )
                    ) {
                        signup_invalidPasswordTV.setText("Password should be at least 6 characters.")
                        signup_invalidPasswordTV.visibility = View.VISIBLE
                        Log.d("XXX", "in should be at least 6 characters")
                    } else {
                        signup_invalidPasswordTV.text = errorMsg
                        signup_invalidPasswordTV.visibility = View.VISIBLE
                    }
                }
            }
    }

    /// makes sure interfaces are implemented
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpSuccessListener = context as? SignUpSuccessListener
        if(signUpSuccessListener == null) {
            throw ClassCastException("$context must implement SignUpSuccessListener")
        }
    }
}
