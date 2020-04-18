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
import android.widget.*
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

    // Initializes the options spinner.
    private fun initSpinner() {
        val spinner: Spinner = view!!.findViewById(R.id.editFeatures_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this.context!!,
            R.array.edit_options,
            R.layout.spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("item selected", resources.getStringArray(R.array.edit_options)[position])
            }

        }

    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        recyclerView = view!!.findViewById(R.id.editFeatures_recyclerView)
        gridLayoutManager = GridLayoutManager(this.context, numCols)
        recyclerView.layoutManager = gridLayoutManager
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
        initSpinner()

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
