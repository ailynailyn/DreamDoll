package com.example.edu.utap.dreamdoll

data class DollItem(val name: String, val imgID: Int)

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
            DollItem("green oval", R.drawable.oval_eyes),
            DollItem("blue round", R.drawable.round_eyes),
            DollItem("black", R.drawable.doll_face),
            DollItem("red", R.drawable.doll_face),
            DollItem("white", R.drawable.doll_face),
            DollItem("brown", R.drawable.doll_face)
        )
        private var hairColorList = listOf (
            DollItem("pink", R.drawable.pink_bangs),
            DollItem("brown", R.drawable.brown_center),
            DollItem("red", R.drawable.doll_face),
            DollItem("blonde", R.drawable.doll_face),
            DollItem("blue", R.drawable.doll_face),
            DollItem("white", R.drawable.doll_face)
        )

        private var hairDemoList = listOf(
            DollItem("pink bangs", R.drawable.pink_bang_demo),
            DollItem("brown center part", R.drawable.center_brown_demo)
        )

        private var eyeDemoList = listOf(
            DollItem("green oval eyes", R.drawable.oval_eyes_demo),
            DollItem("blue round eyes", R.drawable.round_eyes_demo)
        )

        private var lipsList = listOf(
            DollItem("oval", R.drawable.lips_oval),
            DollItem("round", R.drawable.lips_round)
        )

        private var browsList = listOf(
            DollItem("round", R.drawable.round_brows),
            DollItem("angled", R.drawable.angled_brows)
        )

        private var noseList = listOf(
            DollItem("long", R.drawable.long_nose),
            DollItem("button", R.drawable.button_nose)
        )

        private var hatList = listOf(
            DollItem("pink beret", R.drawable.pink_beret),
            DollItem("black large brim", R.drawable.black_large_brim)
        )

        private var bottomsList = listOf(
            DollItem("bell bottoms", R.drawable.black_bell_pants),
            DollItem("pink ruffle skirt", R.drawable.pink_ruffle_skirt)
        )

        private var topsList = listOf(
            DollItem("wrap top", R.drawable.black_wrap_top),
            DollItem("pink ruffle top", R.drawable.pink_lace_top)
        )

        private var shoesList = listOf(
            DollItem("black platforms", R.drawable.black_platforms),
            DollItem("pink bow platforms", R.drawable.pink_bow_platforms)
        )

        private var sampleUserPics = listOf(
            DollItem("", R.drawable.doll_face),
            DollItem("", R.drawable.doll_face)
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