package com.example.edu.utap.dreamdoll


import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SVAdapter(curSavedFace: SavedFace)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<SavedFace>()

    var savedFace = curSavedFace

    inner class VH(itemView: View, curSavedFace: SavedFace) : RecyclerView.ViewHolder(itemView) {
        internal var hairView = itemView.findViewById<ImageView>(R.id.saveSlots_hair)
        internal var eyeView = itemView.findViewById<ImageView>(R.id.saveSlots_eyes)
        internal var browView = itemView.findViewById<ImageView>(R.id.saveSlots_brows)
        internal var noseView = itemView.findViewById<ImageView>(R.id.saveSlots_nose)
        internal var lipView = itemView.findViewById<ImageView>(R.id.saveSlots_lips)
        internal var topView = itemView.findViewById<ImageView>(R.id.saveSlots_top)
        internal var hatView = itemView.findViewById<ImageView>(R.id.saveSlots_hat)
        internal var hatViewBack = itemView.findViewById<ImageView>(R.id.saveSlots_hat_back)
        internal var captionTV = itemView.findViewById<EditText>(R.id.gridItem_caption)

        init {
            itemView.setOnClickListener {
                Log.d("SVAdapter", "item clicked ${captionTV.text}")
                // save over the slot
                hairView.setImageResource(curSavedFace.hairFeature)
                eyeView.setImageResource(curSavedFace.eyesFeature)
                browView.setImageResource(curSavedFace.browsFeature)
                noseView.setImageResource(curSavedFace.noseFeature)
                lipView.setImageResource(curSavedFace.lipsFeature)
                topView.setImageResource(curSavedFace.topFeature)
                hatView.setImageResource(curSavedFace.hatFeature)
                hatViewBack.setImageResource(curSavedFace.hatFeatureBack)

                Repository.defaultSaveSlots[adapterPosition] = curSavedFace
            }
        }

        fun bindView(item: SavedFace) {
            Log.d("SVAdapter", "bindView(item: SavedFace)")
            hairView.setImageResource(item.hairFeature)
            eyeView.setImageResource(item.eyesFeature)
            browView.setImageResource(item.browsFeature)
            noseView.setImageResource(item.noseFeature)
            lipView.setImageResource(item.lipsFeature)
            topView.setImageResource(item.topFeature)
            hatView.setImageResource(item.hatFeature)
            hatViewBack.setImageResource(item.hatFeatureBack)
            if(item.saveTitle != null && item.saveTitle != "") {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.save_item, parent, false), savedFace)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemVH = holder as VH
        itemVH.bindView(listOfItems[position])
    }

    fun setItemList(listOfItems: List<SavedFace>) {
        this.listOfItems = listOfItems
        notifyDataSetChanged()
    }
}
