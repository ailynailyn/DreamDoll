package com.example.edu.utap.dreamdoll

data class DollItem(val name: String, val imgID: Int, val previewID: Int?)

data class NewsfeedItem(val username: String,
                        val profilePicID: String?,
                        val imageID: String?,
                        val likes: Int,
                        val caption: String
                        )

data class HighScoreItem(val username: String, val score: String)

class Repository {

    companion object {
        private var eyeColorList = listOf (
            DollItem("green oval", R.drawable.oval_eyes, null),
            DollItem("blue round", R.drawable.round_eyes, null),
            DollItem("black", R.drawable.doll_face, null),
            DollItem("red", R.drawable.doll_face, null),
            DollItem("white", R.drawable.doll_face, null),
            DollItem("brown", R.drawable.doll_face, null)
        )
        private var hairColorList = listOf (
            DollItem("pink", R.drawable.pink_bangs, null),
            DollItem("brown", R.drawable.brown_center, null),
            DollItem("red", R.drawable.doll_face, null),
            DollItem("blonde", R.drawable.doll_face, null),
            DollItem("blue", R.drawable.doll_face, null),
            DollItem("white", R.drawable.doll_face, null)
        )

        private var hairDemoList = listOf(
            DollItem("pink bangs hair", R.drawable.pink_bangs, R.drawable.pink_bang_demo),
            DollItem("brown center part hair", R.drawable.brown_center, R.drawable.center_brown_demo)
        )

        private var eyeDemoList = listOf(
            DollItem("green oval eyes", R.drawable.oval_eyes, R.drawable.oval_eyes_demo),
            DollItem("blue round eyes", R.drawable.round_eyes, R.drawable.round_eyes_demo)
        )

        private var lipsList = listOf(
            DollItem("oval lips", R.drawable.lips_oval, null),
            DollItem("round lips", R.drawable.lips_round, null)
        )

        private var browsList = listOf(
            DollItem("round brows", R.drawable.round_brows, null),
            DollItem("angled brows", R.drawable.angled_brows, null)
        )

        private var noseList = listOf(
            DollItem("long nose", R.drawable.long_nose, null),
            DollItem("button nose", R.drawable.button_nose, null)
        )

        private var hatList = listOf(
            DollItem("pink beret", R.drawable.pink_beret, null),
            DollItem("black large brim", R.drawable.black_large_brim, null)
        )

        private var bottomsList = listOf(
            DollItem("bell bottoms", R.drawable.black_bell_pants, null),
            DollItem("pink ruffle skirt", R.drawable.pink_ruffle_skirt, null)
        )

        private var topsList = listOf(
            DollItem("wrap top", R.drawable.black_wrap_top, null),
            DollItem("pink ruffle top", R.drawable.pink_lace_top, null)
        )

        private var shoesList = listOf(
            DollItem("black platforms", R.drawable.black_platforms, null),
            DollItem("pink bow platforms", R.drawable.pink_bow_platforms, null)
        )

        private var sampleUserPics = listOf(
            DollItem("", R.drawable.doll_face, null),
            DollItem("", R.drawable.doll_face, null)
        )
    }

    fun fetchEyeColors() : List<DollItem> {
        return eyeColorList
    }

    fun fetchHairColors() : List<DollItem> {
        return hairColorList
    }

    fun fetchLips() : List<DollItem> {
        return lipsList
    }

    fun fetchBrows() : List<DollItem> {
        return browsList
    }

    fun fetchNoses() : List<DollItem> {
        return noseList
    }

    fun fetchHats() : List<DollItem> {
        return hatList
    }

    fun fetchBottoms() : List<DollItem> {
        return bottomsList
    }

    fun fetchTops() : List<DollItem> {
        return topsList
    }

    fun fetchShoes() : List<DollItem> {
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
}