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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*

// EditFaceFrag.kt & edit_features.xml
class EditFeaturesFrag : Fragment() {

    private var mAuth = FirebaseAuth.getInstance();
    private lateinit var recyclerView : RecyclerView//= view!!.findViewById<RecyclerView>(R.id.editFeatures_recyclerView)
    var numCols = 3
    private lateinit var gridLayoutManager : GridLayoutManager//= GridLayoutManager(this.context, numCols)
    //private lateinit var rvAdapter : GVAdapter
    val rvAdapter = GVAdapter() // should be instantiated here?

    val repository = Repository()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.edit_features, container, false)
    }

    fun reset() {

    }

    // Instantiates the grid recycler view of items.
    fun initGrid() {

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
        Log.d("EditFeaturesFrag", "onActivityCreated()")
        super.onActivityCreated(savedInstanceState)
        reset()
        initGrid()

        recyclerView = view!!.findViewById(R.id.editFeatures_recyclerView)
        gridLayoutManager = GridLayoutManager(this.context, numCols)

        recyclerView.layoutManager = gridLayoutManager  // should glbl vars be instantiated here?

        // Set item decoration.
        //recyclerView.addItemDecoration(GridItemDecoration())

        // Set adapter.
         recyclerView.adapter = rvAdapter
        rvAdapter.setItemList(repository.fetchEyeColors())

    }

    /// makes sure interfaces are implemented
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
}
