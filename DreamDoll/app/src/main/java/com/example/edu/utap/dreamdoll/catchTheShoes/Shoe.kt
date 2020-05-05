package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import kotlinx.android.synthetic.main.edit_features.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.login_signup.*
import java.util.ArrayList


class Shoe(columns: Int, rows: Int) : SGrid(columns, rows) {

    private var parent: SGrid? = null
    var xPos: Int = 0
    var yPos: Int = 0

    init {
        this.xPos = -1
        this.yPos = -1
    }

    //do not set the cell position, this should be set by the parent grid
    override fun putCell(X: Int, Y: Int, cell: SCell): Boolean {
        if (X < 0 || X >= this.columns || Y < 0 || Y >= this.rows) {
            return false
        }
        this.grid[Y][X] = cell
        return true
    }

    //attempt to insert this Tetromino into a larger grid
    //returns true if successful, else false
    fun insertIntoGrid(X: Int, Y: Int, parent: SGrid?): Boolean {
        if (parent == null) return false
        //ensure that every spot we need is empty and is on the grid
        for (i in 0 until this.rows) {
            for (j in 0 until this.columns) {
                if (this.getCellAt(j, i) != null) {
                    if (X + j < 0 || X + j >= parent.width ||
                        Y + i < 0 || Y + i >= parent.height ||
                        parent.getCellAt(X + j, Y + i) != null
                    ) {
                        return false
                    }
                }
            }
        }
        //go ahead and insert the Tetromino
        for (i in 0 until this.rows) {
            for (j in 0 until this.columns) {
                val nextCell = this.getCellAt(j, i)
                if (nextCell != null) {
                    parent.putCell(X + j, Y + i, nextCell)
                }
            }
        }
        this.xPos = X
        this.yPos = Y
        this.parent = parent
        return true
    }

    fun removeFromGrid() {
        if (this.parent == null) return
        for (i in 0 until this.rows) {
            for (j in 0 until this.columns) {
                val nextCell = this.getCellAt(j, i)
                if (nextCell != null) {
                    parent!!.removeCell(nextCell.xPosition, nextCell.yPosition)
                }
            }
        }
        this.parent = null
        this.xPos = -1
        this.yPos = -1
    }

    private fun shift(distX: Int, distY: Int): Boolean {
        val myParent = this.parent
        val x = this.xPos
        val y = this.yPos
        this.removeFromGrid()
        val result = this.insertIntoGrid(x + distX, y + distY, myParent)
        //if we fail to move it then put it back where it was
        if (!result) {
            this.insertIntoGrid(x, y, myParent)
        }
        return result
    }

    fun shiftDown(): Boolean {
        return shift(0, 1)
    }

    //attempt to move down as far as possible
    fun zoomDown() {
        while (shiftDown()) {}
    }
}
