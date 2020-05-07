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
import android.widget.Button
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

    // store feature ids
    var hairFeature = 0
    var eyeFeature = 0
    var browFeature = 0
    var noseFeature = 0
    var lipFeature = 0

    // extras
    var hatFeature = 0
    var topFeature = 0
    var hatBackFeature = 0
    var hatInt = 0
    var hatBackInt = 0
    var topInt = 0
    var bottomInt = 0
    var shoeInt = 0

    // clothing views
    lateinit var hatDisplay : ImageView
    lateinit var hatDisplayBack: ImageView
    lateinit var topDisplay : ImageView
    lateinit var bottomDisplay : ImageView
    lateinit var shoeDisplay : ImageView

    // clothing lists
    lateinit var hats : ArrayList<DollItem>
    lateinit var tops : ArrayList<DollItem>
    lateinit var bottoms : ArrayList<DollItem>
    lateinit var shoes : ArrayList<DollItem>

    // prev buttons
    lateinit var prevHat : Button
    lateinit var prevTop : Button
    lateinit var prevBottoms : Button
    lateinit var prevShoes : Button

    // next buttons
    lateinit var nextHat : Button
    lateinit var nextTop : Button
    lateinit var nextBottoms : Button
    lateinit var nextShoes : Button

    // save & back buttons
    lateinit var saveButton : Button
    lateinit var backButton : Button

    private var mAuth = FirebaseAuth.getInstance();
    private val repository = Repository()
    private var hatIdx = 0
    private var topIdx = 0
    private var bottomIdx = 0
    private var shoeIdx = 0
    private var hasArguments = false

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

        if(arguments?.getInt("hatFull") != null && arguments?.getInt("hatFull") != 0) {
            hatInt = arguments?.getInt("hatFull")!!
            hatBackInt = arguments?.getInt("hatBackFull")!!
            topInt = arguments?.getInt("topFull")!!
            bottomInt = arguments?.getInt("bottomsFull")!!
            shoeInt = arguments?.getInt("shoesFull")!!
            topFeature = arguments?.getInt("topFeature")!!
            hatFeature = arguments?.getInt("hatFeature")!!
            hatBackFeature = arguments?.getInt("hatBackFeature")!!
            hasArguments = true
        }

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
        initClothingViews()
        initClothingLists()
        initButtons()
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

    private fun initClothingViews() {
        hatDisplay = view!!.findViewById<ImageView>(R.id.editFull_hat)
        hatDisplayBack = view!!.findViewById<ImageView>(R.id.editFull_hat_back)
        topDisplay = view!!.findViewById<ImageView>(R.id.editFull_top)
        bottomDisplay = view!!.findViewById<ImageView>(R.id.editFull_bottom)
        shoeDisplay = view!!.findViewById<ImageView>(R.id.editFull_shoes)
    }

    private fun initClothingLists() {
        hats = repository.fetchHats()
        tops = repository.fetchTops()
        bottoms = repository.fetchBottoms()
        shoes = repository.fetchShoes()

        if(hasArguments) {
            shoes.add(DollItem("saved shoes", shoeInt, null, shoeInt, null, null))
            bottoms.add(DollItem("saved bottoms", bottomInt, null, bottomInt, null, null))
            tops.add(DollItem("saved top", topFeature, null, topInt, null, null))
            hats.add(DollItem("saved hat", hatFeature, null, hatInt, hatBackInt, hatBackFeature))
            shoeIdx = shoes.size-1
            bottomIdx = bottoms.size-1
            topIdx = tops.size-1
            hatIdx = hats.size-1
        }
    }

    private fun initButtons() {
        prevHat = view!!.findViewById<Button>(R.id.editFull_headPrev)
        prevTop = view!!.findViewById<Button>(R.id.editFull_topsPrev)
        prevBottoms = view!!.findViewById<Button>(R.id.editFull_bottomsPrev)
        prevShoes = view!!.findViewById<Button>(R.id.editFull_shoesPrev)

        nextHat = view!!.findViewById<Button>(R.id.editFull_headNext)
        nextTop = view!!.findViewById<Button>(R.id.editFull_topsNext)
        nextBottoms = view!!.findViewById<Button>(R.id.editFull_bottomsNext)
        nextShoes = view!!.findViewById<Button>(R.id.editFull_shoesNext)

        saveButton = view!!.findViewById<Button>(R.id.editFull_saveButton)
        backButton = view!!.findViewById<Button>(R.id.editFull_backButton)

        if(hasArguments) {
            shoeDisplay.setImageResource(shoeInt)
            bottomDisplay.setImageResource(bottomInt)
            topDisplay.setImageResource(topInt)
            hatDisplay.setImageResource(hatInt)
            hatDisplayBack.setImageResource(hatBackInt)
        }

        // hat buttons
        prevHat.setOnClickListener {
            if(hatIdx <= 0) {
                hatIdx = hats.size-1
            } else {
                hatIdx-=1
            }
            var hat = hats[hatIdx]
            hatDisplay.setImageResource(hat.fullBodyID)
            if(hat.backFull != null) {
                hatDisplayBack.visibility = View.VISIBLE
                hatDisplayBack.setImageResource(hat.backFull!!)
            } else {
                hatDisplayBack.visibility = View.GONE
            }
        }

        nextHat.setOnClickListener {
            if(hatIdx >= hats.size-1) {
                hatIdx = 0
            } else {
                hatIdx+=1
            }
            var hat = hats[hatIdx]
            hatDisplay.setImageResource(hat.fullBodyID)
            if(hat.backFull != null) {
                hatDisplayBack.visibility = View.VISIBLE
                hatDisplayBack.setImageResource(hat.backFull!!)
            } else {
                hatDisplayBack.visibility = View.GONE
            }
        }

        // top buttons
        prevTop.setOnClickListener {
            if(topIdx <= 0) {
                topIdx = tops.size-1
            } else {
                topIdx-=1
            }
            var top = tops[topIdx]
            topDisplay.setImageResource(top.fullBodyID)
        }

        nextTop.setOnClickListener {
            if(topIdx >= tops.size-1) {
                topIdx = 0
            } else {
                topIdx+=1
            }
            var top = tops[topIdx]
            topDisplay.setImageResource(top.fullBodyID)
        }

        // bottom buttons
        prevBottoms.setOnClickListener {
            if(bottomIdx <= 0) {
                bottomIdx = bottoms.size-1
            } else {
                bottomIdx-=1
            }
            var bottom = bottoms[bottomIdx]
            bottomDisplay.setImageResource(bottom.fullBodyID)
        }

        nextBottoms.setOnClickListener {
            if(bottomIdx >= bottoms.size-1) {
                bottomIdx = 0
            } else {
                bottomIdx+=1
            }
            var bottom = bottoms[bottomIdx]
            bottomDisplay.setImageResource(bottom.fullBodyID)
        }

        // shoe buttons
        prevShoes.setOnClickListener {
            if(shoeIdx <= 0) {
                shoeIdx = shoes.size-1
            } else {
                shoeIdx-=1
            }
            var shoe = shoes[shoeIdx]
            shoeDisplay.setImageResource(shoe.fullBodyID)
        }

        nextShoes.setOnClickListener {
            if(shoeIdx >= shoes.size-1) {
                shoeIdx = 0
            } else {
                shoeIdx+=1
            }
            var shoe = shoes[shoeIdx]
            shoeDisplay.setImageResource(shoe.fullBodyID)
        }

        saveButton.setOnClickListener {
            Log.d("save button", "Saving...")
            saveLook()
        }

    }

    fun saveLook() {
        if(activity is CreateCharacterActivity) {
            var saveActivity : CreateCharacterActivity = activity as CreateCharacterActivity
            Log.d("save look", "going back to edit character...")
            saveActivity.beginSaveFrag(hairInt, hairFeature, eyeInt, eyeFeature, browInt, browFeature, noseInt, noseFeature, lipInt, lipFeature, hats[hatIdx].fullBodyID, hats[hatIdx].imgID, hats[hatIdx].backFull?:0, hats[hatIdx].backID?:0, tops[topIdx].fullBodyID, tops[topIdx].imgID, bottoms[bottomIdx].fullBodyID, bottoms[bottomIdx].imgID, shoes[shoeIdx].fullBodyID, shoes[shoeIdx].imgID)
        } else {
            var saveActivity : EditCharacterActivity = activity as EditCharacterActivity
            Log.d("save look", "going back to edit character...")
            saveActivity.beginSaveFrag(hairInt, hairFeature, eyeInt, eyeFeature, browInt, browFeature, noseInt, noseFeature, lipInt, lipFeature, hats[hatIdx].fullBodyID, hats[hatIdx].imgID, hats[hatIdx].backFull?:0, hats[hatIdx].backID?:0, tops[topIdx].fullBodyID, tops[topIdx].imgID, bottoms[bottomIdx].fullBodyID, bottoms[bottomIdx].imgID, shoes[shoeIdx].fullBodyID, shoes[shoeIdx].imgID)
        }

    }

}
