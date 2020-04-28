package com.example.edu.utap.dreamdoll

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
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

open class SGridView : View {
    private lateinit var grid: SGrid    // changed from protected to private???
    private var borderWidth: Int = 5
    protected var cellSz: Float = 0.toFloat()
    protected var visibleRows: Int = 0
    protected var visibleColumns: Int = 0
    private var backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.WHITE
    }
    private var borderPaint = Paint().apply {
        color = Color.BLACK
    }

    private fun init(rows: Int = 5, cols: Int = 5, invis: Int = 0) {
    }

    // Sets the current grid to provided.
    fun setGrid(g: SGrid) {
        this.grid = g
        invalidate()
    }

    // Draws the current grid.
    fun drawGrid(canvas: Canvas) {

//        var d = resources.getDrawable(R.drawable.pink_bow_platforms, null)
//        d.setBounds(4, 10, 4, 4)
//        d.draw(canvas)

        // Skip first two lines.
        for(x in 0..(visibleColumns - 1)) {
            for(y in 0..(visibleRows - 1)) {
                var drawX = x * cellSz
                var drawY = y * cellSz
                var cell = grid.getCellAt(x, y)
                if(cell != null) {  // Paint non-null cells.
                    Log.d("cell ($x, $y)is not null", "will check if it should draw")

                    if(cell!!.bitmap != null) {
                        Log.d("going to draw cell with bitmap", "x:$x, y:$y, BTMP:${cell!!.bitmap}")
                        var left = drawX.toFloat()
                        var right = drawX + (4* cellSz)
                        var top = drawY.toFloat()
                        var bottom = drawY + (2 * cellSz)
                        var shoeRect = RectF(left, top, right, bottom)
                        canvas.drawRect(drawX, drawY, drawX + (2*cellSz), drawY + (2*cellSz), borderPaint)
//                        canvas.drawBitmap(cell!!.bitmap!!, 10f, 10f, null)
                        canvas.drawBitmap(cell!!.bitmap!!, null, shoeRect, Paint())
                    }
                }
            }
        }
    }

    fun refresh() {
        Log.d("SGridView", "refreshing()")
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        visibleRows = grid.height
        visibleColumns = grid.width
        cellSz = width.toFloat() / visibleColumns

        val w = visibleColumns * cellSz   // NEED TO MULTIPLY BY CELL WIDTH
        val h = visibleRows * cellSz    // NEED TO MULTIPLY BY CELL HEIGHT

        canvas.drawRect(x.toFloat(), y.toFloat(), (x + w).toFloat(), (y + h).toFloat(), backgroundPaint)
        drawGrid(canvas)
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, rows: Int, columns: Int, invis: Int) : super(context, attrs) {
        init(rows, columns, invis)
    }

    constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int,
        rows: Int, columns: Int, invis: Int
    ) : super(context, attrs, defStyleAttr) {
        init(rows, columns, invis)
    }
}
