package com.example.edu.utap.dreamdoll

import com.example.edu.utap.dreamdoll.BaseActivity
import com.example.edu.utap.dreamdoll.EditFeaturesFrag
import com.example.edu.utap.dreamdoll.EditFullBodyFrag
import com.example.edu.utap.dreamdoll.R

import android.os.Bundle
import android.util.Log
import com.example.edu.utap.dreamdoll.editCharacter.SaveFrag

// EditFaceFrag.kt & edit_features.xml
class CreateCharacterActivity : BaseActivity() {

    var editFeaturesFrag = EditFeaturesFrag()
    var editFullBodyFrag = EditFullBodyFrag()
    var saveFrag = SaveFrag()
    private val editFullTag = "fullBodyFrag"

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("create char activity","onbackpressed")
        startNewsfeed()
    }

    private fun beginFeaturesFrag() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.container, editFeaturesFrag)
            .commit()
    }

    fun beginFullBodyFrag(hairFull: Int, hairFeature: Int, eyesFull: Int, eyesFeature: Int, browsFull: Int, browsFeature: Int, noseFull: Int, noseFeature: Int, lipsFull: Int, lipsFeature: Int) {
        Log.d("xx", "beginFullBodyFrag was called!!")

        var frag = supportFragmentManager.findFragmentByTag("fullBodyFrag")
        if(frag == null) {

            // add features
            val bundle = Bundle()
            bundle.putInt("hairFull", hairFull)
            bundle.putInt("hairFeature", hairFeature)
            bundle.putInt("eyesFull", eyesFull)
            bundle.putInt("eyesFeature", eyesFeature)
            bundle.putInt("browsFull", browsFull)
            bundle.putInt("browsFeature", browsFeature)
            bundle.putInt("noseFull", noseFull)
            bundle.putInt("noseFeature", noseFeature)
            bundle.putInt("lipsFull", lipsFull)
            bundle.putInt("lipsFeature", lipsFeature)

            val transaction = supportFragmentManager.beginTransaction()
            editFullBodyFrag.arguments = bundle

            transaction
                .addToBackStack(null)
                .add(R.id.container, editFullBodyFrag, editFullTag)
                .commit()
        }
    }

    fun beginSaveFrag(hairFull: Int, hairFeature: Int, eyesFull: Int, eyesFeature: Int, browsFull: Int, browsFeature: Int, noseFull: Int, noseFeature: Int, lipsFull: Int, lipsFeature: Int,
    hatFull: Int, hatFeature: Int, hatBackFull: Int, hatBackFeature: Int, topFull: Int, topFeature: Int, bottomsFull: Int, bottomsFeature: Int, shoesFull:Int, shoesFeature: Int) {

        Log.d("xx", "beginSaveFrag was called!!")
        val bundle = Bundle()
        bundle.putInt("hairFull", hairFull)
        bundle.putInt("hairFeature", hairFeature)
        bundle.putInt("eyesFull", eyesFull)
        bundle.putInt("eyesFeature", eyesFeature)
        bundle.putInt("browsFull", browsFull)
        bundle.putInt("browsFeature", browsFeature)
        bundle.putInt("noseFull", noseFull)
        bundle.putInt("noseFeature", noseFeature)
        bundle.putInt("lipsFull", lipsFull)
        bundle.putInt("lipsFeature", lipsFeature)
        bundle.putInt("hatFull", hatFull)
        bundle.putInt("hatFeature", hatFeature)
        bundle.putInt("hatBackFull", hatBackFull)
        bundle.putInt("hatBackFeature", hatBackFeature)
        bundle.putInt("topFull", topFull)
        bundle.putInt("topFeature", topFeature)
        bundle.putInt("bottomsFull", bottomsFull)
        bundle.putInt("bottomsFeature", bottomsFeature)
        bundle.putInt("shoesFull", shoesFull)
        bundle.putInt("shoesFeature", shoesFeature)

        val transaction = supportFragmentManager.beginTransaction()
        saveFrag.arguments = bundle

        if(!saveFrag.isAdded) {
            transaction
                .addToBackStack(null)
                .add(R.id.container, saveFrag)
                .commit()
        } else {
            transaction.show(saveFrag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        displayOptionsMenu(true)

        // Get extra information.
        var extras = intent.extras
        if(extras != null) {
            var title = intent.extras!!.getString("title")
            Log.d("oncreate editfeatures", "$title")
        }

        beginFeaturesFrag()

    }

}
