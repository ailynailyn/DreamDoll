package com.example.edu.utap.dreamdoll

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.signup.*

// SignupFrag.kt & signup.xml
class SignupFrag : Fragment() {

    private var mAuth = FirebaseAuth.getInstance();
    var signUpSuccessListener: SignUpSuccessListener? = null

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Set up login button.
        signup_button.setOnClickListener {
            var email = signup_emailET.text.toString()
            var password = signup_passwordET.text.toString()
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                // Check firebase.
                Log.d("sign up", "email: $email. password: $password")
                mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign up success.
                            Log.d("XXX", "signUPWithEmail:success");
                            // Go to main screen.
                            signUpSuccessListener?.signUpSuccessful()
                        } else {
                            // Sign up failed. Display message.
                            Log.w("XXX", "signUPWithEmail:failure", task.getException());
                            Toast.makeText(this.context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
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
