package com.example.edu.utap.dreamdoll

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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
    private var curCoins = 0
    private var shoesCaught = 0
    private val initDropDelay = 20L
    private var curDropDelay = initDropDelay
    private var playing = false
    private var curHighScore = 0
    private val db = Firebase.firestore
    private val seed = 2
    private var rand = Random(seed.toLong())
    private lateinit var grid : SGrid
    private lateinit var curShoe: Shoe
    private lateinit var nextShoe: Shoe

    // Called when the activity is resumed.
    override fun onResume() {
        super.onResume()
        Log.d("onresume", "yup")
        setInitialData()
    }

    // Returns a pink ribbon shoe.
    private fun pinkShoe(): Shoe {
        val pinkHeelsBtmp = BitmapFactory.decodeResource(
            resources, R.drawable.pink_heel_game)
        return Shoe(30, 20).apply {
            putCell(0, 0, SCell(pinkHeelsBtmp))
        }
    }

    // Returns a black platform shoe.
    private fun blackPlatforms(): Shoe {
        val blackPlatformsBtmp = BitmapFactory.decodeResource(
            resources, R.drawable.black_platforms_game)
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
        // If the menu frag is preseent, remove it.
        var menuFrag = supportFragmentManager.findFragmentByTag("fallingShoesMenuFrag")
        if(menuFrag != null) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
            Log.d("falling shoes activity", "onbackpressed")
            finish()
        }
    }

    // Sets the user high score tv and coin tv.
    private fun setInitialData() {
        // High Score and coins.
        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnSuccessListener { document ->
                var data = document.data
                if(data != null) {
                    // High Score
                    Log.d("get user data", "$data")
                    var hs = data!!["fallingShoesHighScore"]
                    if(hs != null && hs is Number) {
                        updateHighScore(hs.toInt())
                    }
                    // Coins
                    var coins = data!!["coins"]
                    if(coins != null && coins is Number) {
                        updateCoins(coins.toInt())
                    }
                }

            }
            .addOnFailureListener {
                Log.d("Could not get user data from database", "FAILED")
            }
    }

    private fun initButtons() {
        var menuButton = findViewById<Button>(R.id.shoesMenuButton)
        menuButton.setOnClickListener {
            Log.d("Menu clicked", "will open menu")
            var menuFrag = supportFragmentManager.findFragmentByTag("fallingShoesMenuFrag")
            if(menuFrag != null) {
                supportFragmentManager.popBackStack()
            } else {
//                coroutineContext.
                supportFragmentManager.beginTransaction()
                    .add(R.id.menuContainer, FallingShoesMenuFrag(), "fallingShoesMenuFrag")
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    // Called on Activity creation.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.falling_shoes)
        displayOptionsMenu(true)
        // INIT BUTTONS
        initButtons()

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

        setInitialData()

        var resultsLayout = findViewById<LinearLayout>(R.id.fallingShoesResultsLayout)
        resultsLayout.visibility = View.GONE
        resetGameBoard()

    }

    // Updates the score tv.
    private fun updateScoreTV() {
        var scoreTV = findViewById<TextView>(R.id.shoesScoreTV)
        scoreTV.text = curScore.toString()
    }

    // Resets per-game stats.
    private fun resetStats() {
        curScore = 0
        var resultScoreTV = findViewById<TextView>(R.id.fallingShoesResultScoreTV)
        resultScoreTV.text = curScore.toString()
        shoesCaught = 0
        curLevel = 1
    }

    // Resets the game data.
    private fun resetGameBoard() {
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

        db.collection("users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .update("fallingShoesHighScore", curScore)
            .addOnSuccessListener {
                Log.d("added highscore", "$curScore")
            }
            .addOnFailureListener {
                Log.d("Could not add highscore to database", "FAILED")
            }
    }

    // Updates the coins tv and the database.
    private fun updateCoins(newCoins: Int) {
        curCoins = newCoins
        var coinsTV = findViewById<TextView>(R.id.shoesCoinsTV)
        coinsTV.text = "coins\n$newCoins"

        db.collection("users")
            .document(mAuth.currentUser!!.uid)
            .update("coins", curCoins)
            .addOnSuccessListener {
                Log.d("added coins", "$curCoins")
            }
            .addOnFailureListener {
                Log.d("Could not add coins to database", "FAILED")
            }
    }

    // Adds text view with score.
    private fun displayResults() {
        var resultsLayout = findViewById<LinearLayout>(R.id.fallingShoesResultsLayout)
        var resultScoreTV = findViewById<TextView>(R.id.fallingShoesResultScoreTV)
        var resultsCoinsTV = findViewById<TextView>(R.id.fallingShoesResultsCoinsTV)

        resultScoreTV.text = curScore.toString()

        var coinsEarned = curScore  // Depends on how we want to award coins.

        // Update total coins.
        updateCoins(curCoins + coinsEarned)

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
        } else {
            curShoe = nextShoe
        }
        var randX = rand.nextInt(cols - curShoe.width) // width of a shoe
        var added = curShoe.insertIntoGrid(randX, 0, grid)
        Log.d("Added?", "$added x: $randX, y: 0")
        if(!added) {
            Log.d("Could not add", "the shoe")
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
        curDropDelay = (curDropDelay - 1).toLong()
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

    // Called when the game is over.
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
