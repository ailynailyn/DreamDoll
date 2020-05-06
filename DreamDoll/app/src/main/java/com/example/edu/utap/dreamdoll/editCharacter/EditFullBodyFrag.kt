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

// EditFullBodyFrag.kt & edit_full_body.xml
class EditFullBodyFrag : Fragment() {

    // feature variables
    lateinit var hairDisplay : ImageView
    lateinit var eyeDisplay : ImageView
    lateinit var browDisplay : ImageView
    lateinit var noseDisplay : ImageView
    lateinit var lipDisplay : ImageView

    // feature storage from bundle
    var hairInt = 0
    var eyeInt = 0
    var browInt = 0
    var noseInt = 0
    var lipInt = 0

    // clothing variables
//    lateinit var hatDisplay : ImageView
//    lateinit var topDisplay : ImageView
//    lateinit var pantDisplay : ImageView
//    lateinit var shoeDisplay : ImageView

    private var mAuth = FirebaseAuth.getInstance();
    private val repository = Repository()
    private var hatIdx = 0
    private var topIdx = 0
    private var bottomIdx = 0
    private var shoeIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hairInt = arguments?.getInt("hair")!!
        eyeInt = arguments?.getInt("eyes")!!
        browInt = arguments?.getInt("brows")!!
        noseInt = arguments?.getInt("nose")!!
        lipInt = arguments?.getInt("lips")!!

        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.edit_full_body, container, false)
    }

    fun reset() {

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
        initFace()
        reset()
    }

    /// makes sure interfaces are implemented
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    fun initFace() {
        hairDisplay = view!!.findViewById<ImageView>(R.id.editFull_hair)
        Log.d("HHH", "$hairInt")
        hairDisplay.setImageResource(hairInt)

        eyeDisplay = view!!.findViewById<ImageView>(R.id.editFull_eyes)
        eyeDisplay.setImageResource(eyeInt)

        browDisplay = view!!.findViewById<ImageView>(R.id.editFull_brows)
        browDisplay.setImageResource(browInt)

        noseDisplay = view!!.findViewById<ImageView>(R.id.editFull_nose)
        noseDisplay.setImageResource(noseInt)

        lipDisplay = view!!.findViewById<ImageView>(R.id.editFull_lips)
        lipDisplay.setImageResource(lipInt)
    }

}
