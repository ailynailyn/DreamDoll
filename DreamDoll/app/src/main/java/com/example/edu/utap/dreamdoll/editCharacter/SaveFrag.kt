package com.example.edu.utap.dreamdoll.editCharacter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.*

class SaveFrag: Fragment() {

    // feature variables
    lateinit var hairDisplay : ImageView
    lateinit var eyeDisplay : ImageView
    lateinit var browDisplay : ImageView
    lateinit var noseDisplay : ImageView
    lateinit var lipDisplay : ImageView

    // clothing views
    lateinit var hatDisplay : ImageView
    lateinit var hatDisplayBack: ImageView
    lateinit var topDisplay : ImageView
    lateinit var bottomDisplay : ImageView
    lateinit var shoeDisplay : ImageView

    // store face feature ids
    var hairFeature = 0
    var eyeFeature = 0
    var browFeature = 0
    var noseFeature = 0
    var lipFeature = 0


    // clothes features
    var hatFeature = 0
    var topFeature = 0
    var hatBackFeature = 0
    var bottomFeature = 0
    var shoeFeature = 0

    // full face storage from bundle
    // these will not be used here
    var hairInt = 0
    var eyeInt = 0
    var browInt = 0
    var noseInt = 0
    var lipInt = 0

    // full clothes
    // these will not be used here
    var hatInt = 0
    var topInt = 0
    var bottomInt = 0
    var shoeInt = 0

    private lateinit var savedList : ArrayList<SavedFace>
    private lateinit var curSavedFace: SavedFace

    private lateinit var recyclerView : RecyclerView
    private lateinit var gridLayoutManager : GridLayoutManager
    private lateinit var rvAdapter : SVAdapter
    private val repository = Repository()

    private val numCols = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hairInt = arguments?.getInt("hairFull")!!
        eyeInt = arguments?.getInt("eyesFull")!!
        browInt = arguments?.getInt("browsFull")!!
        noseInt = arguments?.getInt("noseFull")!!
        lipInt = arguments?.getInt("lipsFull")!!

        hairFeature = arguments?.getInt("hairFeature")!!
        eyeFeature = arguments?.getInt("eyesFeature")!!
        browFeature = arguments?.getInt("browsFeature")!!
        noseFeature = arguments?.getInt("noseFeature")!!
        lipFeature = arguments?.getInt("lipsFeature")!!
        topFeature = arguments?.getInt("topFeature")!!
        hatFeature = arguments?.getInt("hatFeature")!!
        hatBackFeature = arguments?.getInt("hatBackFeature")!!

        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.save_slots, container, false)
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

    /// makes sure interfaces are implemented
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private fun initSavedList() {
        savedList = arrayListOf<SavedFace>()
        if(savedList.size == 0) {
            savedList = repository.fetchDefaultSlots()
        }
    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        recyclerView = view!!.findViewById(R.id.save_recyclerView)
        gridLayoutManager = GridLayoutManager(this.context, numCols)
        recyclerView.layoutManager = gridLayoutManager
    }

    private fun initCurSavedFace() {
        curSavedFace = SavedFace(hairFeature, eyeFeature, browFeature, noseFeature, lipFeature, topFeature, hatFeature, hatBackFeature, "NEW LOOK")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSavedList()
        initCurSavedFace()
        initGrid()

        rvAdapter = SVAdapter(curSavedFace)

        // Set adapter.
        recyclerView.adapter = rvAdapter

        rvAdapter.setItemList(savedList)
    }

}