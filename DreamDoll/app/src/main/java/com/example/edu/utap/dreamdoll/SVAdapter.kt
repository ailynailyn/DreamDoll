package com.example.edu.utap.dreamdoll


import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView

class SVAdapter(curSavedLook: SavedLook)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<SavedLook>()

    var savedLook = curSavedLook

    inner class VH(itemView: View, curSavedLook: SavedLook) : RecyclerView.ViewHolder(itemView) {
        internal var curSavedFace = curSavedLook.face

        internal var hairView = itemView.findViewById<ImageView>(R.id.saveSlots_hair)
        internal var eyeView = itemView.findViewById<ImageView>(R.id.saveSlots_eyes)
        internal var browView = itemView.findViewById<ImageView>(R.id.saveSlots_brows)
        internal var noseView = itemView.findViewById<ImageView>(R.id.saveSlots_nose)
        internal var lipView = itemView.findViewById<ImageView>(R.id.saveSlots_lips)
        internal var topView = itemView.findViewById<ImageView>(R.id.saveSlots_top)
        internal var hatView = itemView.findViewById<ImageView>(R.id.saveSlots_hat)
        internal var hatViewBack = itemView.findViewById<ImageView>(R.id.saveSlots_hat_back)
        internal var dollView = itemView.findViewById<ImageView>(R.id.doll)
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

                hairView.visibility = View.VISIBLE
                eyeView.visibility = View.VISIBLE
                browView.visibility = View.VISIBLE
                lipView.visibility = View.VISIBLE
                noseView.visibility = View.VISIBLE
                topView.visibility = View.VISIBLE
                hatView.visibility = View.VISIBLE
                hatViewBack.visibility = View.VISIBLE
                dollView.visibility = View.VISIBLE

                Repository.defaultSaveSlots[adapterPosition] = curSavedLook
                Repository.defaultSaveSlots[adapterPosition].saved = true
            }

            captionTV.onChange {
                Repository.defaultSaveSlots[adapterPosition].saveTitle = it.toString()
            }

        }

        fun bindView(item: SavedLook) {
            Log.d("SVAdapter", "bindView(item: SavedLook)")
            if(!item.saved) {
                hairView.visibility = View.GONE
                eyeView.visibility = View.GONE
                browView.visibility = View.GONE
                lipView.visibility = View.GONE
                noseView.visibility = View.GONE
                topView.visibility = View.GONE
                hatView.visibility = View.GONE
                hatViewBack.visibility = View.GONE
                dollView.visibility = View.GONE
            } else {
                hairView.visibility = View.VISIBLE
                eyeView.visibility = View.VISIBLE
                browView.visibility = View.VISIBLE
                lipView.visibility = View.VISIBLE
                noseView.visibility = View.VISIBLE
                topView.visibility = View.VISIBLE
                hatView.visibility = View.VISIBLE
                hatViewBack.visibility = View.VISIBLE
                dollView.visibility = View.VISIBLE
            }

            var face = item.face
            hairView.setImageResource(face.hairFeature)
            eyeView.setImageResource(face.eyesFeature)
            browView.setImageResource(face.browsFeature)
            noseView.setImageResource(face.noseFeature)
            lipView.setImageResource(face.lipsFeature)
            topView.setImageResource(face.topFeature)
            hatView.setImageResource(face.hatFeature)
            hatViewBack.setImageResource(face.hatFeatureBack)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.save_item, parent, false), savedLook)
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemVH = holder as VH
        itemVH.bindView(listOfItems[position])
    }

    fun setItemList(listOfItems: List<SavedLook>) {
        this.listOfItems = listOfItems
        notifyDataSetChanged()
    }

    fun EditText.onChange(cb: (String) -> Unit) {
        this.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) { cb(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
