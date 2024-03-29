package com.example.edu.utap.dreamdoll

import android.widget.ImageView
import com.google.firebase.Timestamp

open class ImageTransferItem(val image: ImageView, var fullID: Int, var featureID: Int) {

    fun setFullBodyID(identifier: Int) {
        fullID = identifier
    }

    fun getFullBodyID(): Int {
        return fullID
    }

    fun setFaceFeatureID(magnified: Int) {
        featureID = magnified
    }

    fun getFaceFeatureID() : Int {
        return featureID
    }
}

data class SavedLook(var face: SavedFace, var hairFull: Int, var eyesFull: Int, var browsFull: Int, var noseFull: Int, var lipsFull: Int, var topFull: Int, var hatFull: Int, var hatBackFull: Int, var bottomsFull: Int, var shoesFull: Int, var saveTitle: String?, var saved: Boolean) {
    fun update(save: SavedLook) {
        face = save.face
        hairFull = save.hairFull
        eyesFull = save.eyesFull
        browsFull = save.browsFull
        noseFull = save.noseFull
        lipsFull = save.lipsFull
        topFull = save.topFull
        hatFull = save.hatFull
        hatBackFull = save.hatBackFull
        bottomsFull = save.bottomsFull
        shoesFull = save.shoesFull
        saveTitle = save.saveTitle
        saved = save.saved
    }
}
data class SavedFace(val hairFeature: Int, val eyesFeature: Int, val browsFeature: Int, val noseFeature: Int, val lipsFeature: Int, val topFeature: Int, val hatFeature: Int, val hatFeatureBack: Int) {
    fun getHairInt(): Int {
       return hairFeature
    }
    fun getEyeInt(): Int {
        return eyesFeature
    }
    fun getBrowInt(): Int {
        return browsFeature
    }
    fun getNoseInt(): Int {
        return noseFeature
    }
    fun getLipInt(): Int {
        return lipsFeature
    }
    fun getTopInt(): Int {
        return topFeature
    }
    fun getHatInt(): Int {
        return hatFeature
    }
    fun getHatBackInt(): Int {
        return hatFeatureBack
    }
}

data class DollItem(val name: String, val imgID: Int, val previewID: Int?, val fullBodyID: Int, val backFull: Int?, val backID: Int?)

data class NewsfeedItem(val username: String,
                        val profilePicID: String?,
                        val imageID: String?,
                        val likes: Int,
                        val caption: String,
                        val postID: String,
                        val userID: String,
                        val timestamp: String
                        )

data class HighScoreItem(val username: String, val score: String)

class Repository {

    companion object {
//        private var eyeColorList = listOf (
//            DollItem("green oval", R.drawable.oval_eyes, null, null),
//            DollItem("blue round", R.drawable.round_eyes, null, null),
//            DollItem("black", R.drawable.doll_face, null, null),
//            DollItem("red", R.drawable.doll_face, null, null),
//            DollItem("white", R.drawable.doll_face, null, null),
//            DollItem("brown", R.drawable.doll_face, null, null)
//        )
//        private var hairColorList = listOf (
//            DollItem("pink", R.drawable.pink_bangs, null, null),
//            DollItem("brown", R.drawable.brown_center, null, null),
//            DollItem("red", R.drawable.doll_face, null, null),
//            DollItem("blonde", R.drawable.doll_face, null, null),
//            DollItem("blue", R.drawable.doll_face, null, null),
//            DollItem("white", R.drawable.doll_face, null, null)
//        )

        private var defaultSaveFace = SavedFace(R.drawable.brown_center, R.drawable.oval_eyes, R.drawable.round_brows, R.drawable.long_nose, R.drawable.lips_oval, R.drawable.black_wrap_top_prev, R.drawable.black_hat_prev, R.drawable.black_hat_back_prev)

        var defaultSaveSlots = arrayListOf(
            SavedLook(defaultSaveFace, R.drawable.pink_bang_full, R.drawable.oval_eyes_full, R.drawable.angled_brows_full, R.drawable.button_nose_full, R.drawable.round_lips_full, R.drawable.black_wrap_top, R.drawable.hat_front, R.drawable.hat_back, R.drawable.black_bell_pants, R.drawable.black_platforms, "Save Slot 1", false),
            SavedLook(defaultSaveFace, R.drawable.pink_bang_full, R.drawable.oval_eyes_full, R.drawable.angled_brows_full, R.drawable.button_nose_full, R.drawable.round_lips_full, R.drawable.black_wrap_top, R.drawable.hat_front, R.drawable.hat_back, R.drawable.black_bell_pants, R.drawable.black_platforms, "Save Slot 1", false),
            SavedLook(defaultSaveFace, R.drawable.pink_bang_full, R.drawable.oval_eyes_full, R.drawable.angled_brows_full, R.drawable.button_nose_full, R.drawable.round_lips_full, R.drawable.black_wrap_top, R.drawable.hat_front, R.drawable.hat_back, R.drawable.black_bell_pants, R.drawable.black_platforms, "Save Slot 1", false)
        )

        private var hairDemoList = listOf(
            DollItem("pink bangs hair", R.drawable.pink_bangs, R.drawable.pink_bang_demo, R.drawable.pink_bang_full, null, null),
            DollItem("brown center part hair", R.drawable.brown_center, R.drawable.center_brown_demo, R.drawable.brown_center_full, null, null)
        )

        private var eyeDemoList = listOf(
            DollItem("green oval eyes", R.drawable.oval_eyes, R.drawable.oval_eyes_demo, R.drawable.oval_eyes_full, null, null),
            DollItem("blue round eyes", R.drawable.round_eyes, R.drawable.round_eyes_demo, R.drawable.round_eyes_full, null, null)
        )

        private var lipsList = listOf(
            DollItem("oval lips", R.drawable.lips_oval, null, R.drawable.oval_lips_full, null, null),
            DollItem("round lips", R.drawable.lips_round, null, R.drawable.round_lips_full, null, null)
        )

        private var browsList = listOf(
            DollItem("round brows", R.drawable.round_brows, null, R.drawable.round_brows_full, null, null),
            DollItem("angled brows", R.drawable.angled_brows, null, R.drawable.angled_brows_full, null, null)
        )

        private var noseList = arrayListOf(
            DollItem("long nose", R.drawable.long_nose, null, R.drawable.long_nose_full, null,null),
            DollItem("button nose", R.drawable.button_nose, null, R.drawable.button_nose_full, null, null)
        )

        private var hatList = arrayListOf(
            DollItem("black large brim", R.drawable.black_hat_prev, null, R.drawable.black_large_brim, R.drawable.hat_back, R.drawable.black_hat_back_prev),
            DollItem("pink beret", R.drawable.pink_beret_prev, null, R.drawable.pink_beret, null, null)
        )

        private var bottomsList = arrayListOf(
            DollItem("bell bottoms", R.drawable.black_bell_pants, null, R.drawable.black_bell_pants, null, null),
            DollItem("pink ruffle skirt", R.drawable.pink_ruffle_skirt, null, R.drawable.pink_ruffle_skirt, null, null)
        )

        private var topsList = arrayListOf(
            DollItem("wrap top", R.drawable.black_wrap_top_prev, null, R.drawable.black_wrap_top, null, null),
            DollItem("pink ruffle top", R.drawable.pink_top_prev, null, R.drawable.pink_lace_top, null, null)
        )

        private var shoesList = arrayListOf(
            DollItem("black platforms", R.drawable.black_platforms, null, R.drawable.black_platforms, null, null),
            DollItem("pink bow platforms", R.drawable.pink_bow_platforms, null, R.drawable.pink_bow_platforms, null, null)
        )

        private var sampleUserPics = listOf(
            DollItem("", R.drawable.doll_face, null, R.drawable.doll_face, null, null),
            DollItem("", R.drawable.doll_face, null, R.drawable.doll_face, null, null)
        )
    }

//    fun fetchEyeColors() : List<DollItem> {
//        return eyeColorList
//    }
//
//    fun fetchHairColors() : List<DollItem> {
//        return hairColorList
//    }

    fun fetchLips() : List<DollItem> {
        return lipsList
    }

    fun fetchBrows() : List<DollItem> {
        return browsList
    }

    fun fetchNoses() : List<DollItem> {
        return noseList
    }

    fun fetchHats() : ArrayList<DollItem> {
        return hatList
    }

    fun fetchBottoms() : ArrayList<DollItem> {
        return bottomsList
    }

    fun fetchTops() : ArrayList<DollItem> {
        return topsList
    }

    fun fetchShoes() : ArrayList<DollItem> {
        return shoesList
    }

    fun fetchHairDemo() : List<DollItem> {
        return hairDemoList
    }

    fun fetchEyeDemo() : List<DollItem> {
        return eyeDemoList
    }

    fun fetchUserPics() : List<DollItem> {
        return sampleUserPics
    }

    fun fetchDefaultSlots() : ArrayList<SavedLook> {
        return defaultSaveSlots
    }
}