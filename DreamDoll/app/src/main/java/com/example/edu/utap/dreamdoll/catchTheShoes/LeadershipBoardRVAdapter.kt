package com.example.edu.utap.dreamdoll.catchTheShoes

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edu.utap.dreamdoll.HighScoreItem
import com.example.edu.utap.dreamdoll.NewsfeedItem
import com.example.edu.utap.dreamdoll.R
import com.example.edu.utap.dreamdoll.UserProfileActivity

class LeadershipBoardRVAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfItems = listOf<HighScoreItem>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var username = itemView.findViewById<TextView>(R.id.leadershipBoardUsername)
        internal var score = itemView.findViewById<TextView>(R.id.leadershipBoardScore)

        init {
            itemView.setOnClickListener {
                Log.d("RVAdapter", "item clicked ${username.text}")

            }
        }

        fun bindView(item: HighScoreItem) {
            Log.d("LeadershipRVAdapter", "bindView(item: HighScoreItem)")
            username.text = item.username
            score.text = item.score

            username.setOnClickListener {
                Log.d("username clicked", "${username.text}")
                // Go to user profile.
                val intent = Intent(itemView.context, UserProfileActivity::class.java)
                intent.putExtra("username", username.text.toString())
                itemView.context.startActivity(intent)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.leadership_board_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemVH = holder as VH
        itemVH.bindView(listOfItems[position])
    }

    fun setItemList(listOfItems: List<HighScoreItem>) {
        this.listOfItems = listOfItems
        notifyDataSetChanged()
    }
}
