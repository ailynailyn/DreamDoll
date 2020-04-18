package com.example.edu.utap.dreamdoll

data class DollItem(val name: String, val imgID: Int)

class Repository {

    companion object {
        private var eyeColorList = listOf (
            DollItem("green", R.drawable.doll_face),
            DollItem("blue", R.drawable.doll_face),
            DollItem("black", R.drawable.doll_face),
            DollItem("red", R.drawable.doll_face),
            DollItem("white", R.drawable.doll_face),
            DollItem("brown", R.drawable.doll_face)
        )
    }

    fun fetchEyeColors() : List<DollItem> {
        return eyeColorList
    }
}