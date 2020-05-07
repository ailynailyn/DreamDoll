package com.example.edu.utap.dreamdoll.editCharacter

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.*
import java.io.File
import java.io.FileInputStream

class SaveFrag: Fragment() {

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
    var hatBackInt = 0
    var topInt = 0
    var bottomInt = 0
    var shoeInt = 0

    private lateinit var savedList : ArrayList<SavedLook>
    private lateinit var curSavedFace: SavedFace
    private lateinit var curSavedLook: SavedLook

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
        hatInt = arguments?.getInt("hatFull")!!
        hatBackInt = arguments?.getInt("hatBackFull")!!
        topInt = arguments?.getInt("topFull")!!
        bottomInt = arguments?.getInt("bottomsFull")!!
        shoeInt = arguments?.getInt("shoesFull")!!

        hairFeature = arguments?.getInt("hairFeature")!!
        eyeFeature = arguments?.getInt("eyesFeature")!!
        browFeature = arguments?.getInt("browsFeature")!!
        noseFeature = arguments?.getInt("noseFeature")!!
        lipFeature = arguments?.getInt("lipsFeature")!!
        topFeature = arguments?.getInt("topFeature")!!
        hatFeature = arguments?.getInt("hatFeature")!!
        hatBackFeature = arguments?.getInt("hatBackFeature")!!

        Log.d("BBB", bottomInt.toString())
        Log.d("SSS", shoeInt.toString())

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
        savedList = arrayListOf<SavedLook>()

        val file = File(context?.filesDir, "SAVESLOTS7.txt")
        file.createNewFile()
        val readSaves = FileInputStream(file).bufferedReader().use { it.readText() }
        val savesList = readSaves.lines()
        Log.d("YYY", savesList.size.toString())

        for(save in 0 until savesList.size) {
            var saveVals = savesList[save].split(" ")
            val size = saveVals.size
            Log.d("XXX", "it's loading...$size")
            if(saveVals.size >= 20) {
                val value = saveVals[0]
                Log.d("Reading..." ,"$value")
                var readSavedFace = SavedFace(saveVals[0].toInt(), saveVals[1].toInt(), saveVals[2].toInt(), saveVals[3].toInt(), saveVals[4].toInt(), saveVals[5].toInt(), saveVals[6].toInt(), saveVals[7].toInt())
                if(readSavedFace != null) {
                    Log.d("got the face", "face")
                }
                var newTitle = saveVals[18].replace(",", " ")
                var readSavedLook = SavedLook(readSavedFace, saveVals[8].toInt(), saveVals[9].toInt(), saveVals[10].toInt(), saveVals[11].toInt(), saveVals[12].toInt(), saveVals[13].toInt(), saveVals[14].toInt(),
                    saveVals[15].toInt(), saveVals[16].toInt(), saveVals[17].toInt(), newTitle, true)
                if(readSavedLook != null) {
                    Log.d("got the look", "look")
                }
                savedList.add(readSavedLook)
            }
        }

        Log.d("Size", "${savedList.size}")
        // default
        if(savedList.size == 0) {
            savedList = repository.fetchDefaultSlots()
        }

        if(savedList.size <= 5) {
            var blankSlots = repository.fetchDefaultSlots()
            for(blanks in 0 until blankSlots.size) {
                savedList.add(blankSlots[blanks])
            }
        }
    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        recyclerView = view!!.findViewById(R.id.save_recyclerView)
        gridLayoutManager = GridLayoutManager(this.context, numCols)
        recyclerView.layoutManager = gridLayoutManager
    }

    private fun initCurSavedFaceLook() {
        curSavedFace = SavedFace(hairFeature, eyeFeature, browFeature, noseFeature, lipFeature, topFeature, hatFeature, hatBackFeature)
        curSavedLook = SavedLook(curSavedFace, hairInt, eyeInt, browInt, noseInt, lipInt, topInt, hatInt, hatBackInt, bottomInt, shoeInt, "NEW LOOK", false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initSavedList()
        initCurSavedFaceLook()
        initGrid()

        rvAdapter = SVAdapter(curSavedLook, savedList)

        // Set adapter.
        recyclerView.adapter = rvAdapter

        rvAdapter.setItemList(savedList)
    }

}