package com.example.edu.utap.dreamdoll

import android.os.Bundle
import android.util.Log

// EditFaceFrag.kt & edit_features.xml
class EditCharacterActivity : BaseActivity() {

    var editFeaturesFrag = EditFeaturesFrag()
    var editFullBody_frag = EditFullBodyFrag()

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("edit char activity","onbackpressed")
        startNewsfeed()
    }

    private fun beginFeaturesFrag() {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.container, editFeaturesFrag)
            .commit()
    }

    fun beginFullBodyFrag(hair: Int, eyes: Int, brows: Int, nose: Int, lips: Int) {
        Log.d("xx", "beginFullBodyFrag was called!!")

        // add features
        val bundle = Bundle()
        bundle.putInt("hair", hair)
        bundle.putInt("eyes", eyes)
        bundle.putInt("brows", brows)
        bundle.putInt("nose", nose)
        bundle.putInt("lips", lips)

        val transaction = supportFragmentManager.beginTransaction()
        editFullBody_frag.arguments = bundle

        transaction
            .addToBackStack(null)
            .add(R.id.container, editFullBody_frag)
            .commit()
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
