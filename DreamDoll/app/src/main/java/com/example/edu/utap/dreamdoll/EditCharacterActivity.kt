package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
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
import kotlinx.android.synthetic.main.edit_features.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*

// EditFaceFrag.kt & edit_features.xml
class EditCharacterActivity : AppCompatActivity() {

    private var mAuth = FirebaseAuth.getInstance();
    private lateinit var recyclerView : RecyclerView
    private lateinit var gridLayoutManager : GridLayoutManager
    private val rvAdapter = GVAdapter()
    private val repository = Repository()
    private val numCols = 3
    var editFullBody_frag = EditFullBodyFrag()
    private var curCategoryIdx = 0

    private fun beginFullBodyFrag() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.editCharacter_container, editFullBody_frag)
            .commit()
    }

    // Initializes the options spinner.
    private fun initSpinner() {
        val spinner: Spinner = findViewById(R.id.editFeatures_spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.edit_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                var item = resources.getStringArray(R.array.edit_options)[position]
                Log.d("item selected", item)
                // XXX
                // Set data.
                curCategoryIdx = position
                when(item) {
                    "Eyes" -> {
                        Log.d("xxx", "eyes pressed")
                        rvAdapter.setItemList(repository.fetchEyeColors())
                    }
                    "Hair" -> {
                        Log.d("xxx", "hair pressed")
                        rvAdapter.setItemList(repository.fetchHairColors())
                    }
                    "Head" -> {
                        Log.d("xxx", "head pressed")
                        beginFullBodyFrag()
                    }
                    "Tops" -> {
                        Log.d("xxx", "tops pressed")
                        beginFullBodyFrag()
                    }
                    "Bottoms" -> {
                        Log.d("xxx", "bottoms pressed")
                        beginFullBodyFrag()
                    }
                    "Shoes" -> {
                        Log.d("xxx", "shoes pressed")
                        beginFullBodyFrag()
                    }
                }
            }

        }

    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        recyclerView = findViewById(R.id.editFeatures_recyclerView)
        gridLayoutManager = GridLayoutManager(this, numCols)
        recyclerView.layoutManager = gridLayoutManager
    }

    private fun initButtons() {
        var prev = findViewById<Button>(R.id.editFeature_prev)
        var next = findViewById<Button>(R.id.editFeature_next)
        prev.setOnClickListener {
            var prevPos = curCategoryIdx - 1
            if(prevPos >= 0) {
                var prevCategory = resources.getStringArray(R.array.edit_options)[prevPos]
                when(prevCategory) {
                    "Eyes" -> {
                        editFeatures_spinner.setSelection(prevPos)
                        curCategoryIdx = prevPos
                    }
                    "Hair" -> {
                        editFeatures_spinner.setSelection(prevPos)
                        curCategoryIdx = prevPos
                    }
                    else -> {
                        beginFullBodyFrag()
                    }
                }
            }
        }

        next.setOnClickListener {
            var nextPos = curCategoryIdx + 1
            if(nextPos < resources.getStringArray(R.array.edit_options).size) {
                var prevCategory = resources.getStringArray(R.array.edit_options)[nextPos]
                when(prevCategory) {
                    "Eyes" -> {
                        editFeatures_spinner.setSelection(nextPos)
                        curCategoryIdx = nextPos
                    }
                    "Hair" -> {
                        editFeatures_spinner.setSelection(nextPos)
                        curCategoryIdx = nextPos
                    }
                    else -> {
                        beginFullBodyFrag()
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_character)
        // Get extra information.
        var extras = intent.extras
        if(extras != null) {
            var title = intent.extras!!.getString("title")
            Log.d("oncreate editfeatures", "$title")
        }
        curCategoryIdx = 0
        initGrid()
        initSpinner()
        initButtons()

        // Set adapter.
        recyclerView.adapter = rvAdapter

        // Init with eyes
        rvAdapter.setItemList(repository.fetchEyeColors())
    }

}
