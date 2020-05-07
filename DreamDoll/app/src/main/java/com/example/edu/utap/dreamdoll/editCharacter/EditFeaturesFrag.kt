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
        if(arguments?.getInt("hairFull") != null) {
            hairInt = arguments?.getInt("hairFull")!!
            eyeInt = arguments?.getInt("eyesFull")!!
            browInt = arguments?.getInt("browsFull")!!
            noseInt = arguments?.getInt("noseFull")!!
            lipInt = arguments?.getInt("lipsFull")!!
            hatInt = arguments?.getInt("hatFull")!!
            hatBackInt = arguments?.getInt("hatBackFull")!!
            topInt = arguments?.getInt("topFull")!!
            bottomInt = arguments?.getInt("bottomFull")!!
            shoeInt = arguments?.getInt("shoeFull")!!
            hairFeature = arguments?.getInt("hairFeature")!!
            eyeFeature = arguments?.getInt("eyesFeature")!!
            browFeature = arguments?.getInt("browsFeature")!!
            noseFeature = arguments?.getInt("noseFeature")!!
            lipFeature = arguments?.getInt("lipsFeature")!!
            topFeature = arguments?.getInt("topFeature")!!
            hatFeature = arguments?.getInt("hatFeature")!!
            hatBackFeature = arguments?.getInt("hatBackFeature")!!
        }

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
            editActivity.beginFullBodyFrag(hairTransfer.getFullBodyID(), hairTransfer.getFaceFeatureID(), eyeTransfer.getFullBodyID(), eyeTransfer.getFaceFeatureID(), browTransfer.getFullBodyID(), browTransfer.getFaceFeatureID(), noseTransfer.getFullBodyID(), noseTransfer.getFaceFeatureID(), lipTransfer.getFullBodyID(), lipTransfer.getFaceFeatureID(), hatInt, hatFeature, hatBackInt, hatBackFeature, topInt, topFeature, bottomInt, shoeInt)
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

        if(hairInt == 0) {
            hairInt = R.drawable.brown_center_full
            hairFeature = R.drawable.center_brown_demo
        } else {
            hairDisplay.setImageResource(hairFeature)
        }
        if(eyeInt == 0) {
            eyeInt = R.drawable.oval_eyes_full
            eyeFeature = R.drawable.oval_eyes_demo
        } else {
            eyeDisplay.setImageResource(eyeFeature)
        }
        if(browInt == 0) {
            browInt = R.drawable.angled_brows_full
            browFeature = R.drawable.angle_brow_demo
        } else {
            browDisplay.setImageResource(browFeature)
        }
        if(noseInt == 0) {
            noseInt = R.drawable.button_nose_full
            noseFeature = R.drawable.nose_button_demo
        } else {
            noseDisplay.setImageResource(noseFeature)
        }
        if(lipInt == 0) {
            lipInt = R.drawable.round_lips_full
            lipFeature = R.drawable.lips_round_demo
        } else {
            lipDisplay.setImageResource(lipFeature)
        }

        hairTransfer = ImageTransferItem(hairDisplay, hairInt, hairFeature)
        eyeTransfer = ImageTransferItem(eyeDisplay, eyeInt, eyeFeature)
        browTransfer = ImageTransferItem(browDisplay, browInt, browFeature)
        noseTransfer = ImageTransferItem(noseDisplay, noseInt, noseFeature)
        lipTransfer = ImageTransferItem(lipDisplay, lipInt, lipFeature)

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
                    "Hairstyle" -> {
                        Log.d("xxx", "hair pressed")
                        rvAdapter.setItemList(repository.fetchHairDemo())
                    }
                    "Eyes" -> {
                        Log.d("xxx", "eyes pressed")
                        rvAdapter.setItemList(repository.fetchEyeDemo())
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
