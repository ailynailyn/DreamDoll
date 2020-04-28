package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
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


open class SGrid(val columns: Int, val rows: Int) {

    protected var grid: ArrayList<ArrayList<SCell?>> = ArrayList()

    val width get() = columns
    val height get() = rows

    //look for the first row that is full
    //returns -1 if not found
    val firstFullRow: Int
        get() {
            for (j in 0 until this.height) {
                var fullRow = true
                val r = this.grid[j]
                for (i in 0 until this.width) {
                    if (r[i] == null) {
                        fullRow = false
                        break
                    }
                }
                if (fullRow) {
                    return j
                }
            }
            return -1
        }

    interface CellVisitor {
        fun visitCell(canvas: Canvas, paint: Paint, cell: SCell?)
    }

    init {
        //Generate an empty grid
        for (i in 0 until this.height) {
            val nextRow = ArrayList<SCell?>()
            for (j in 0 until this.width) {
                nextRow.add(null)
            }
            this.grid.add(nextRow)
        }
    }

    fun visitCells(canvas: Canvas, paint: Paint, visitor: CellVisitor) {
        for (i in 0 until this.height) {
            for (j in 0 until this.width)
                visitor.visitCell(canvas, paint, grid[i][j])
        }
    }

    //return a pointer to the cell at a position (X,Y)
    fun getCellAt(X: Int, Y: Int): SCell? {
        return if (X < 0 || X >= this.width || Y < 0 || Y >= this.height) {
            null
        } else this.grid[Y][X]
    }

    //same as getCellAt(X,Y), except this function also removes it from the grid
    private fun extractCellAt(X: Int, Y: Int): SCell? {
        if (X < 0 || X >= this.width || Y < 0 || Y >= this.height) {
            return null
        }
        val result = this.grid[Y][X]
        this.grid[Y][X] = null
        return result
    }

    //insert a cell into the grid
    open fun putCell(X: Int, Y: Int, cell: SCell): Boolean {
        if (X < 0 || X >= this.width || Y < 0 || Y >= this.height) {
            return false
        }
        cell.xPosition = X
        cell.yPosition = Y
        this.grid[Y][X] = cell
        return true
    }

    //remove a cell from the grid
    fun removeCell(X: Int, Y: Int): Boolean {
        if (X < 0 || X >= this.width || Y < 0 || Y >= this.height) {
            return false
        }
        val theCell = this.grid[Y][X]
        if (theCell != null) {
            this.grid[Y][X] = null
            theCell.yPosition = -1
            theCell.xPosition = -1
        }
        return true
    }

    //deletes a row, shifts everything down
    fun deleteRow(row: Int): Boolean {
        for (i in 0 until this.width) {
            if (!this.removeCell(i, row)) {
                return false
            }
        }
        for (i in 0 until this.width) {
            for (j in row downTo 0) {
                val cellToMove = this.extractCellAt(i, j - 1)
                if (cellToMove != null) {
                    if (!this.putCell(i, j, cellToMove)) {
                        return false
                    }
                }
            }
        }
        return true
    }

    //delete all cells
    fun clear() {
        for (i in 0 until this.height) {
            val r = this.grid[i]
            for (j in 0 until this.width) {
                r[j] = null
            }
        }
    }
}
