package com.example.edu.utap.dreamdoll


import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.falling_shoes.*
import kotlinx.coroutines.*
import java.util.*

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

    private val seed = 2
    private var rand = Random(seed.toLong())

    private fun pinkShoe(): Shoe {
        val pinkHeelsBtmp = BitmapFactory.decodeResource(
            resources, R.drawable.pink_heels)
        Log.d("pinkShoe()", "btmp: $pinkHeelsBtmp")
        return Shoe(2, 2).apply {
            putCell(0, 0, SCell(pinkHeelsBtmp))
            putCell(0, 1, SCell(null))
            putCell(1, 0, SCell(null))
            putCell(1, 1, SCell(null))
        }
    }

    private fun blackPlatforms(): Shoe {
        val blackPlatformsBtmp = BitmapFactory.decodeResource(
            resources, R.drawable.black_platforms)
        Log.d("blackPlatforms()", "btmp: $blackPlatformsBtmp")
        return Shoe(2, 2).apply {
            putCell(0, 0, SCell(blackPlatformsBtmp))
            putCell(0, 1, SCell(null))
            putCell(1, 0, SCell(null))
            putCell(1, 1, SCell(null))
        }
    }

    private fun pinkBowPlatforms(): Shoe {
        val pinkBowPlatformsBtmp = BitmapFactory.decodeResource(
            resources, R.drawable.pink_bow_platforms)
        Log.d("pinkBowPlatforms()", "btmp: $pinkBowPlatformsBtmp")
        return Shoe(2, 2).apply {
            putCell(0, 0, SCell(pinkBowPlatformsBtmp))
            putCell(0, 1, SCell(null))
            putCell(1, 0, SCell(null))
            putCell(1, 1, SCell(null))
        }
    }

    fun randomShoe(): Shoe? {
        var tet = rand.nextInt(3)
        return when (tet) {
            0 -> pinkShoe()
            1 -> blackPlatforms()
            2 -> pinkBowPlatforms()
            else -> null
        }
    }

    fun resetRandom() {
        rand = Random(seed.toLong())
    }

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

    private fun updateScoreTV() {
        var scoreTV = findViewById<TextView>(R.id.shoesScoreTV)
        scoreTV.text = curScore.toString()
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
        updateScoreTV()
        playing = false

        // Also displays the play now layout for preparing a new game.
        fallingShoesPlayButton.visibility = View.VISIBLE
    }

    // Falling Shoes game.
    private fun playFallingShoes() {

        // Get the current shoe and place at top.
        if(!playing) {
            // Initial.
            curShoe = randomShoe()!!//ShoeBuilder.random()!!
            Log.d("Inital.", "curShoe: ${curShoe.toString()}")
        } else {
            curShoe = nextShoe
            Log.d("swapping next", "nextshoe -> curShoe: ${curShoe.toString()}")
        }
        var added = curShoe.insertIntoGrid(4, 8, grid)
        Log.d("Added?", "$added")
        if(!added) {
            // Game over.
            Toast.makeText(this, "You lose", Toast.LENGTH_SHORT).show()
            playing = false
            coroutineContext.cancelChildren()
        } else {
            playing = true
            // Get the next tetronimo ready.
            nextShoe = randomShoe()!!//ShoeBuilder.random()!!
            launch {
                dropShoe()
            }
        }

    }

    // Level up.
    private fun levelUp() {
        curLevel += 1
        dropDelay = (dropDelay * .8).toLong()
    }

    private fun caughtShoe() {
        shoesCaught++
        if(shoesCaught % 5 == 0) {  // THIS IS ALL UP TO US. RANDOM NUMS FOR NOW
            levelUp()  // Might want to level up based on time instead of shoes cleared?
        }
        // Update score.
        curScore += curLevel    // THIS ALL DEPENDS ON US. HOW WE WANT TO COUNT POINTS
        updateScoreTV()

    }


    // Begins the tetronimo's slow drop down.
    private suspend fun dropShoe() {
        while(coroutineContext.isActive) {
            var shifted = curShoe.shiftDown()
            sgrid_view.refresh()
            if(!shifted) {
                Log.d("dropShoe", "shoe can not keep dropping.")
                // SHOES
                // Check if the shoe is at the same position that the basket is in.
                var xPos = curShoe.xPos
                var yPos = curShoe.yPos
                var caught = false
                Log.d("shoe landed", "($xPos, $yPos) is it caught? will check")

                // Clear the shoe.
                grid.deleteRow(1)   // Should delete ?
                sgrid_view.refresh()

                if(caught) {
                    caughtShoe()
                }

                coroutineContext.cancelChildren()
                playFallingShoes()
            }
            Log.d("Dropped. delaying:", "$dropDelay")
            delay(dropDelay.toLong())
        }
    }


}
