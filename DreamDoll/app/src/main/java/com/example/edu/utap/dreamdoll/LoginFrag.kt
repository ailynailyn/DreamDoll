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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*

// LoginFrag.kt & login.xml
class LoginFrag : Fragment() {

    private var mAuth = FirebaseAuth.getInstance();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.login, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Set up login button.
        login_button.setOnClickListener {
            var email = login_emailET.text.toString()
            var password = login_passwordET.text.toString()
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                // Check firebase.
                Log.d("login", "email: $email. password: $password")
                mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success.
                                Log.d("XXX", "signInWithEmail:success");
                                // Go to main screen.
                            } else {
                                // Sign in failed. Display message.
                                Log.w("XXX", "signInWithEmail:failure", task.getException());
                                Toast.makeText(this.context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                    }
            }
        }
    }

    /// makes sure interfaces are implemented
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}
