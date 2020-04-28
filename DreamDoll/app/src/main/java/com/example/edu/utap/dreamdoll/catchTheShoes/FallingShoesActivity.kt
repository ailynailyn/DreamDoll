package com.example.edu.utap.dreamdoll


import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.edu.utap.dreamdoll.catchTheShoes.ShoeBuilder
import kotlinx.android.synthetic.main.falling_shoes.*
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

// EditFaceFrag.kt & edit_features.xml
class FallingShoesActivity : BaseActivity(), CoroutineScope by MainScope()  {

    private val rows = 23
    private val cols = 20
    private var curScore = 0
    private var curLevel = 1
    private var shoesCaught = 0
    private var dropDelay = 1000L
    private lateinit var grid : SGrid
    private lateinit var curShoe: Shoe
    private lateinit var nextShoe: Shoe
    private var playing = false

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d("falling shoes activity","onbackpressed")
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.falling_shoes)
        displayOptionsMenu(true)
        // INIT BUTTONS
//        initButtons()

        // Get extra information.
        var extras = intent.extras
        if(extras != null) {
            var title = intent.extras!!.getString("title")
            Log.d("oncreate falling shoes", "$title")
        }

        // Create the grid.
        grid = SGrid(cols, rows)
        sgrid_view.setGrid(grid)

        // Set on click listener for the play button.
        fallingShoesPlayButton.setOnClickListener {
            // New game.
            playFallingPlayLayout.visibility = View.GONE
            playFallingShoes()
        }

        resetGame()

    }

    // Resets the game data.
    private fun resetGame() {
        coroutineContext.cancelChildren()
        grid.clear()
        sgrid_view.refresh()
        dropDelay = 1000L
        curScore = 0
        shoesCaught = 0
        curLevel = 1
        var scoreTV = findViewById<TextView>(R.id.shoesScoreTV)
        scoreTV.text = curScore.toString()
        playing = false

        // Also displays the play now layout for preparing a new game.
        fallingShoesPlayButton.visibility = View.VISIBLE
    }

    // Falling Shoes game.
    private fun playFallingShoes() {

        // Get the current shoe and place at top.
        if(!playing) {
            // Initial.
            curShoe = ShoeBuilder.random()!!
        } else {
            curShoe = nextShoe
        }
        var added = curShoe.insertIntoGrid(4, 0, grid)
        if(!added) {
            // Game over.
            Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show()
            playing = false
            coroutineContext.cancelChildren()
        } else {
            playing = true
            // Get the next tetronimo ready.
            nextShoe = ShoeBuilder.random()!!
//            nextGrid.clear()
//            nextTetromino.insertIntoGrid(1, 4, nextGrid)
//            nextTgrid_view.refresh()
            // Begin the slow drop down.
            launch {
                dropShoe()
            }
        }

    }

    // Level up.
    private fun levelUp() {
        curLevel += 1
//        level.text = curLevel.toString()
        dropDelay = (dropDelay * .8).toLong()
    }


    // Begins the tetronimo's slow drop down.
    private suspend fun dropShoe() {
        while(coroutineContext.isActive) {
            var shifted = curShoe.shiftDown()
            sgrid_view.refresh()
            if(!shifted) {
                Log.d("dropShoe", "shoe can not keep dropping.")
                // Check for any cleared rows.

                var fullRow = grid.firstFullRow
                while(fullRow > 0) {
                    grid.deleteRow(fullRow)
                    shoesCaught++
                    if(shoesCaught % 5 == 0) {
                        levelUp()
                    }
//                    cleared.text = curRows.toString()
                    curScore += curLevel
                    var scoreTV = findViewById<TextView>(R.id.shoesScoreTV)
                    scoreTV.text = curScore.toString()
                    fullRow = grid.firstFullRow
                    sgrid_view.refresh()
                }
                coroutineContext.cancelChildren()
                playFallingShoes()
            }
            Log.d("Dropped. delaying:", "$dropDelay")
            delay(dropDelay.toLong())
        }
    }


}
