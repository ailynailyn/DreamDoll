package com.example.edu.utap.dreamdoll

import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.login_signup.*
import kotlinx.android.synthetic.main.options_menu.*

// OptionsMenuFrag.kt & options_menu.xml
class OptionsMenuFrag : Fragment() {

    var buttonListener: OptionsMenuButtonListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the root view and cache references to vital UI elements.
        return inflater.inflate(R.layout.options_menu, container, false)
    }

    // Notifies activity which button was clicked.
    interface OptionsMenuButtonListener {
        fun createCharButtonClicked()
        fun editCharButtonClicked()
        fun buyItemsButtonClicked()
        fun earnCoinsButtonClicked()
        fun logoutButtonClicked()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Sets up the buttons.
        create_character.setOnClickListener {
            Log.d("options menu fragment", "create character button clicked")
            buttonListener?.createCharButtonClicked()
        }
        edit_character.setOnClickListener {
            Log.d("options menu fragment", "edit character button clicked")
            buttonListener?.editCharButtonClicked()
        }
        buy_items.setOnClickListener {
            Log.d("options menu fragment", "buy items button clicked")
            buttonListener?.buyItemsButtonClicked()
        }
        earn_coins.setOnClickListener {
            Log.d("options menu fragment", "earn coins button clicked")
            buttonListener?.earnCoinsButtonClicked()
        }
        logout.setOnClickListener {
            Log.d("options menu fragment", "logout button clicked")
            buttonListener?.logoutButtonClicked()
        }

    }

    // Makes sure interfaces are implemented.
    override fun onAttach(context: Context) {
        super.onAttach(context)
        buttonListener = context as? OptionsMenuButtonListener
        if(buttonListener == null) {
            throw ClassCastException("$context must implement OptionsMenuButtonListener")
        }
    }
}
