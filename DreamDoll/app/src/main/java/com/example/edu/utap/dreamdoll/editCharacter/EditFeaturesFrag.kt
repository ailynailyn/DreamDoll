package com.example.edu.utap.dreamdoll

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.edit_features.*

// EditFeaturesFrag.kt & edit_features.xml
class EditFeaturesFrag() : Fragment() {

    // feature variables
    lateinit var hairDisplay : ImageView
    lateinit var eyeDisplay : ImageView
    lateinit var browDisplay : ImageView
    lateinit var noseDisplay : ImageView
    lateinit var lipDisplay : ImageView

    // transfer feature variables
    lateinit var hairTransfer : ImageTransferItem
    lateinit var eyeTransfer : ImageTransferItem
    lateinit var browTransfer : ImageTransferItem
    lateinit var noseTransfer : ImageTransferItem
    lateinit var lipTransfer : ImageTransferItem


    private lateinit var recyclerView : RecyclerView
    private lateinit var gridLayoutManager : GridLayoutManager
    private lateinit var rvAdapter : GVAdapter
    private val repository = Repository()
    private val numCols = 3
    private var curCategoryIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.edit_features, container, false)
    }

    fun goToFullBody() {
        Log.d("XXX", "goToFullBody was called!!")

        if(activity is CreateCharacterActivity) {
            var createActivity : CreateCharacterActivity = activity as CreateCharacterActivity
            createActivity.beginFullBodyFrag(hairTransfer.getFullBodyID(), hairTransfer.getFaceFeatureID(), eyeTransfer.getFullBodyID(), eyeTransfer.getFaceFeatureID(), browTransfer.getFullBodyID(), browTransfer.getFaceFeatureID(), noseTransfer.getFullBodyID(), noseTransfer.getFaceFeatureID(), lipTransfer.getFullBodyID(), lipTransfer.getFaceFeatureID())
            Log.d("XXX", "got past the featureList!!")

        } else {
            var editActivity : EditCharacterActivity = activity as EditCharacterActivity
            editActivity.beginFullBodyFrag(hairTransfer.getFullBodyID(), hairTransfer.getFaceFeatureID(), eyeTransfer.getFullBodyID(), eyeTransfer.getFaceFeatureID(), browTransfer.getFullBodyID(), browTransfer.getFaceFeatureID(), noseTransfer.getFullBodyID(), noseTransfer.getFaceFeatureID(), lipTransfer.getFullBodyID(), lipTransfer.getFaceFeatureID(), 0, 0, 0, 0, 0, 0, 0, 0)
            Log.d("XXX", "got past the featureList!!")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        curCategoryIdx = 0
        initInvisibleFeatures()
        initGrid()
        initSpinner()
        initButtons()

        hairTransfer = ImageTransferItem(hairDisplay, 0, 0)
        eyeTransfer = ImageTransferItem(eyeDisplay, 0, 0)
        browTransfer = ImageTransferItem(browDisplay, 0, 0)
        noseTransfer = ImageTransferItem(noseDisplay, 0, 0)
        lipTransfer = ImageTransferItem(lipDisplay, 0, 0)

        rvAdapter = GVAdapter(hairTransfer, eyeTransfer, browTransfer, noseTransfer, lipTransfer)

        // Set adapter.
        recyclerView.adapter = rvAdapter

        // Init with eyes
        rvAdapter.setItemList(repository.fetchEyeDemo())
    }

    /// makes sure interfaces are implemented
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }


    // Initializes the options spinner.
    private fun initSpinner() {
        val spinner: Spinner = view!!.findViewById(R.id.editFeatures_spinner)
        ArrayAdapter.createFromResource(
            this.context!!,
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
                        rvAdapter.setItemList(repository.fetchEyeDemo())
                    }
                    "Hair" -> {
                        Log.d("xxx", "hair pressed")
                        rvAdapter.setItemList(repository.fetchHairDemo())
                    }
                    "Eyebrows" -> {
                        Log.d("xxx", "eyebrows pressed")
                        rvAdapter.setItemList(repository.fetchBrows())
                    }
                    "Nose" -> {
                        Log.d("xxx", "nose pressed")
                        rvAdapter.setItemList(repository.fetchNoses())
                    }
                    "Lips" -> {
                        Log.d("xxx", "lips pressed")
                        rvAdapter.setItemList(repository.fetchLips())
                    }
                    "Hats" -> {
                        Log.d("xxx", "hats pressed")
                        rvAdapter.setItemList(repository.fetchHats())
                        Log.d("xx", "hats. going to listen full body frag")
                        goToFullBody()
                    }
                    "Tops" -> {
                        Log.d("xxx", "tops pressed")
                        rvAdapter.setItemList(repository.fetchTops())
                        goToFullBody()
                    }
                    "Bottoms" -> {
                        Log.d("xxx", "bottoms pressed")
                        rvAdapter.setItemList(repository.fetchBottoms())
                        goToFullBody()
                    }
                    "Shoes" -> {
                        Log.d("xxx", "shoes pressed")
                        rvAdapter.setItemList(repository.fetchShoes())
                        goToFullBody()
                    }
                }
            }

        }

    }

    private fun initInvisibleFeatures() {
        hairDisplay = view!!.findViewById<ImageView>(R.id.editFeatures_hair)
        eyeDisplay = view!!.findViewById<ImageView>(R.id.editFeatures_eyes)
        browDisplay = view!!.findViewById<ImageView>(R.id.editFeatures_brows)
        noseDisplay = view!!.findViewById<ImageView>(R.id.editFeatures_nose)
        lipDisplay = view!!.findViewById<ImageView>(R.id.editFeatures_lips)
    }

    // Initializes the grid recycler view of items.
    private fun initGrid() {
        recyclerView = view!!.findViewById(R.id.editFeatures_recyclerView)
        gridLayoutManager = GridLayoutManager(this.context, numCols)
        recyclerView.layoutManager = gridLayoutManager
    }

    private fun initButtons() {
        var prev = view!!.findViewById<Button>(R.id.editFeature_prev)
        var next = view!!.findViewById<Button>(R.id.editFeature_next)
        prev.setOnClickListener {
            var prevPos = curCategoryIdx - 1
            if(prevPos >= 0) {
                var prevCategory = resources.getStringArray(R.array.edit_options)[prevPos]
                when(prevCategory) {
                    "Hair", "Eyes", "Eyebrows", "Nose", "Lips" -> {
                        editFeatures_spinner.setSelection(prevPos)
                        curCategoryIdx = prevPos
                    }

                    else -> {
                        goToFullBody()
                    }
                }
            }
        }

        next.setOnClickListener {
            var nextPos = curCategoryIdx + 1
            if(nextPos < resources.getStringArray(R.array.edit_options).size) {
                var prevCategory = resources.getStringArray(R.array.edit_options)[nextPos]
                when(prevCategory) {
                    "Hair", "Eyes", "Eyebrows", "Nose", "Lips" -> {
                        editFeatures_spinner.setSelection(nextPos)
                        curCategoryIdx = nextPos
                    }
                    else -> {
                        goToFullBody()
                    }
                }
            }
        }
    }

}
