package com.example.edu.utap.dreamdoll
//
//import android.os.Bundle
//import android.util.Log
//
//// EditFaceFrag.kt & edit_features.xml
//class EditCharacterActivity : BaseActivity() {
//
//    var editFeaturesFrag = EditFeaturesFrag()
//    var editFullBodyFrag = EditFullBodyFrag()
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        Log.d("edit char activity","onbackpressed")
//        startNewsfeed()
//    }
//
//    private fun beginFeaturesFrag() {
//        supportFragmentManager.beginTransaction()
//            .addToBackStack(null)
//            .add(R.id.container, editFeaturesFrag)
//            .commit()
//    }
//
//    fun beginFullBodyFrag(hairFull: Int, hairFeature: Int, eyesFull: Int, eyesFeature: Int, browsFull: Int, browsFeature: Int, noseFull: Int, noseFeature: Int, lipsFull: Int, lipsFeature: Int) {
//        Log.d("xx", "beginFullBodyFrag was called!!")
//
//        // add features
//        val bundle = Bundle()
//        bundle.putInt("hairFull", hairFull)
//        bundle.putInt("hairFeature", hairFeature)
//        bundle.putInt("eyesFull", eyesFull)
//        bundle.putInt("eyesFeature", eyesFeature)
//        bundle.putInt("browsFull", browsFull)
//        bundle.putInt("browsFeature", browsFeature)
//        bundle.putInt("noseFull", noseFull)
//        bundle.putInt("noseFeature", noseFeature)
//        bundle.putInt("lipsFull", lipsFull)
//        bundle.putInt("lipsFeature", lipsFeature)
//
//        val transaction = supportFragmentManager.beginTransaction()
//        editFullBodyFrag.arguments = bundle
//
//        transaction
//            .addToBackStack(null)
//            .add(R.id.container, editFullBodyFrag)
//            .commit()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        displayOptionsMenu(true)
//
//        // Get extra information.
//        var extras = intent.extras
//        if(extras != null) {
//            var title = intent.extras!!.getString("title")
//            Log.d("oncreate editfeatures", "$title")
//        }
//
//        beginFeaturesFrag()
//
//    }
//
//}
