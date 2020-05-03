package com.example.edu.utap.dreamdoll

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.falling_shoes.*
import kotlinx.coroutines.*
import java.util.*


// EditFaceFrag.kt & edit_features.xml
class FallingShoesActivity : BaseActivity(), CoroutineScope by MainScope()  {

    private val rows = 240
    private val cols = 200
    private var curScore = 0
    private var curLevel = 1
    private var shoesCaught = 0
    private val initDropDelay = 20L
    private var curDropDelay = initDropDelay
    private lateinit var grid : SGrid
    private lateinit var curShoe: Shoe
    private lateinit var nextShoe: Shoe
    private var playing = false
    private var curHighScore = 0
    private val db = Firebase.firestore

    private val seed = 2
    private var rand = Random(seed.toLong())

    private fun pinkShoe(): Shoe {
        val pinkHeelsBtmp = BitmapFactory.decodeResource(
            resources, R.drawable.pink_heel_game)
        Log.d("pinkShoe()", "btmp: $pinkHeelsBtmp")
        return Shoe(30, 20).apply {
            putCell(0, 0, SCell(pinkHeelsBtmp))
        }
    }

    private fun blackPlatforms(): Shoe {
        val blackPlatformsBtmp = BitmapFactory.decodeResource(
            resources, R.drawable.black_platforms_game)
        Log.d("blackPlatforms()", "btmp: $blackPlatformsBtmp")
        return Shoe(30, 20).apply {
            putCell(0, 0, SCell(blackPlatformsBtmp))
        }
    }

    // Returns a random shoe object.
    private fun randomShoe(): Shoe? {
        var tet = rand.nextInt(2)
        return when (tet) {
            0 -> pinkShoe()
            1 -> blackPlatforms()
            else -> null
        }
    }

    // Resets the random with the given seed.
    fun resetRandom() {
        rand = Random(seed.toLong())
    }

    // Called when back is pressed.
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
            resetStats()
            playFallingPlayLayout.visibility = View.INVISIBLE
            playFallingShoes()
        }

        var resultsLayout = findViewById<LinearLayout>(R.id.fallingShoesResultsLayout)
        resultsLayout.visibility = View.GONE
        resetGameBoard()

    }

    private fun updateScoreTV() {
        var scoreTV = findViewById<TextView>(R.id.shoesScoreTV)
        scoreTV.text = curScore.toString()
    }

    private fun resetStats() {
        curScore = 0
        var resultScoreTV = findViewById<TextView>(R.id.fallingShoesResultScoreTV)
        resultScoreTV.text = curScore.toString()
        shoesCaught = 0
        curLevel = 1
    }

    // Resets the game data.
    private fun resetGameBoard() {
        Log.d("reset game", "xxx")
        coroutineContext.cancelChildren()
        grid.clear()
        sgrid_view.refresh()
        curDropDelay = initDropDelay
        updateScoreTV()
        playing = false
    }

    // Updates the hihg score tv and the database.
    private fun updateHighScore(curScore: Int) {
        curHighScore = curScore
        var hsTV = findViewById<TextView>(R.id.shoesHighScore)
        hsTV.text = "high score: $curHighScore"
        val highScore = hashMapOf("fallingShoesHighScore" to curScore.toString())

        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .set(highScore)
            .addOnSuccessListener {
                Log.d("added highscore", "$curScore")
            }
            .addOnFailureListener {
                Log.d("couldnt add highscore to database", "failed")
            }
    }

    // Adds text view with score.
    private fun displayResults() {
        Log.d("displayResults()", "d")
        var resultsLayout = findViewById<LinearLayout>(R.id.fallingShoesResultsLayout)
        var resultScoreTV = findViewById<TextView>(R.id.fallingShoesResultScoreTV)
        var resultsCoinsTV = findViewById<TextView>(R.id.fallingShoesResultsCoinsTV)
        resultScoreTV.text = curScore.toString()
        var coinsEarned = curScore  // Depends on how we want to award coins.
        playFallingPlayLayout.visibility = View.VISIBLE
        resultsCoinsTV.text = coinsEarned.toString()
        resultsLayout.visibility = View.VISIBLE
        if(curScore > curHighScore) {
            updateHighScore(curScore)
        }
        // Also displays the play now layout for preparing a new game.
        fallingShoesPlayButton.visibility = View.VISIBLE
    }

    // Falling Shoes game.
    private fun playFallingShoes() {

        // Get the current shoe and place at top.
        if(!playing) {
            // Initial.
            curShoe = randomShoe()!!
            Log.d("Inital.", "curShoe: ${curShoe.toString()}")
        } else {
            curShoe = nextShoe
            Log.d("swapping next", "nextshoe -> curShoe: ${curShoe.toString()}")
        }
        var randX = rand.nextInt(cols - curShoe.width) // width of a shoe
        var added = curShoe.insertIntoGrid(randX, 0, grid)
        Log.d("Added?", "$added x: $randX, y: 0")
        if(!added) {
            Log.d("couldnt add", "the shoe")
            // Game over.
            playing = false
            coroutineContext.cancelChildren()
            gameOver()
        } else {
            playing = true
            // Get the next tetronimo ready.
            nextShoe = randomShoe()!!
            launch {
                dropShoe()
            }
        }

    }

    // Level up.
    private fun levelUp() {
        curLevel += 1
        curDropDelay = (curDropDelay * .8).toLong()
    }

    // Called when a shoe was successfully caught. Updates stats.
    private fun caughtShoe() {
        shoesCaught++
        if(shoesCaught % 5 == 0) {  // THIS IS ALL UP TO US. RANDOM NUMS FOR NOW
            levelUp()  // Might want to level up based on time instead of shoes cleared?
        }
        // Update score.
        curScore += curLevel    // THIS ALL DEPENDS ON US. HOW WE WANT TO COUNT POINTS
        updateScoreTV()
    }

    private fun gameOver() {
        displayResults()
        resetGameBoard()
    }

    // Begins the tetronimo's slow drop down.
    private suspend fun dropShoe() {
        while(coroutineContext.isActive) {
            Log.d("dropShoe", "while coroutine is active")
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
                var seekbar = findViewById<SeekBar>(R.id.shoesGameSeekBar)
                var bagPos = seekbar.progress
                Log.d("progress", "$bagPos")
                var bagWidth = 80
                var shoeWidth = curShoe.width
                Log.d("droppedshoe", "bagWidth:$bagWidth, shoeWdth:$shoeWidth")
                var shoeMin = bagPos - (shoeWidth / 2)
                var shoeMax = bagPos + bagWidth + (shoeWidth / 2)
                Log.d("shoe at bottom", "shoeMin: $shoeMin, shoeMax: $shoeMax")
                if((xPos > shoeMin) && (xPos < shoeMax)) {
                    caught = true
                }

                // Clear the shoe.
                //grid.deleteRow(0)   // Should delete ?
                curShoe.removeFromGrid()
                sgrid_view.refresh()

                if(caught) {
                    caughtShoe()
                    playFallingShoes()
                } else {
                    coroutineContext.cancelChildren()
                    gameOver()
                }
            }
            Log.d("Dropped. delaying:", "$curDropDelay")
            delay(curDropDelay.toLong())
        }
    }


}
