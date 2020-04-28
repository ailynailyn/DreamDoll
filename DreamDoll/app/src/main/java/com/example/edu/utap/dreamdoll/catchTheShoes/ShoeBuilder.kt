package com.example.edu.utap.dreamdoll.catchTheShoes

import android.graphics.Color
import com.example.edu.utap.dreamdoll.SCell
import com.example.edu.utap.dreamdoll.Shoe
import java.util.*

object ShoeBuilder {

    //Feel free to modify these colors
    //http://www.colorpicker.com/
    private val IColor = Color.parseColor("#96E1FA")
    private val JColor = Color.parseColor("#304FC9")
    private val LColor = Color.parseColor("#F7CB05")
    private val OColor = Color.parseColor("#FFFF0D")
    private val SColor = Color.parseColor("#6ED654")
    private val TColor = Color.parseColor("#C267DB")
    private val ZColor = Color.parseColor("#F54747")

    private const val seed = 3
    val debug = 0
    private var cur_cmd = 0

    private var rand = Random(seed.toLong())
    private val debug_seq2 = intArrayOf(
        5, 5, 6, 4, 0, 1, 5, 3, 1, 6, 4, 0, 4, 5, 5, 3, 6, 3, 5, 4, 1, 2, 3
    )
    private val debug_seq3 = intArrayOf(
        5, 5, 6, 4, 0, 1, 5, 3, 1, 6, 4, 0, 4, 5, 5, 3, 6, 3, 5, 4, 1, 2, 3, 5, 2, 0, 2, 5, 5, 6, 1
        , 6, 4, 4, 1, 5, 3, 3
    )

    fun i(): Shoe {
        return Shoe(4, 4).apply {
            putCell(0, 1, SCell(IColor))
            putCell(1, 1, SCell(IColor))
            putCell(2, 1, SCell(IColor))
            putCell(3, 1, SCell(IColor))
        }
    }

    fun j(): Shoe {
        return Shoe(3, 3).apply {
            putCell(0, 0, SCell(JColor))
            putCell(0, 1, SCell(JColor))
            putCell(1, 1, SCell(JColor))
            putCell(2, 1, SCell(JColor))
        }
    }

    fun l(): Shoe {
        return Shoe(3, 3).apply {
            putCell(0, 1, SCell(LColor))
            putCell(1, 1, SCell(LColor))
            putCell(2, 1, SCell(LColor))
            putCell(2, 0, SCell(LColor))
        }
    }

    fun o(): Shoe {
        return Shoe(4, 3).apply {
            putCell(1, 0, SCell(OColor))
            putCell(2, 0, SCell(OColor))
            putCell(1, 1, SCell(OColor))
            putCell(2, 1, SCell(OColor))
        }
    }

    fun s(): Shoe {
        return Shoe(3, 3).apply {
            putCell(0, 1, SCell(SColor))
            putCell(1, 1, SCell(SColor))
            putCell(1, 0, SCell(SColor))
            putCell(2, 0, SCell(SColor))
        }
    }

    fun t(): Shoe {
        return Shoe(3, 3).apply {
            putCell(0, 1, SCell(TColor))
            putCell(1, 1, SCell(TColor))
            putCell(2, 1, SCell(TColor))
            putCell(1, 0, SCell(TColor))
        }
    }

    fun z(): Shoe {
        return Shoe(3, 3).apply {
            putCell(0, 0, SCell(ZColor))
            putCell(1, 0, SCell(ZColor))
            putCell(1, 1, SCell(ZColor))
            putCell(2, 1, SCell(ZColor))
        }
    }

    fun random(): Shoe? {
        var tet = 0
        if (debug == 2) {
            tet = debug_seq2[cur_cmd]
            cur_cmd += 1
        } else if (debug == 3) {
            tet = debug_seq3[cur_cmd]
            cur_cmd += 1
        } else {
            tet = rand.nextInt(7)
        }
        return when (tet) {
            0 -> i()
            1 -> j()
            2 -> l()
            3 -> o()
            4 -> s()
            5 -> t()
            6 -> z()
            else -> null
        }
    }

    fun resetRandom() {
        if ((debug == 2) || (debug == 3)) {
            cur_cmd = 0
        }
        rand = Random(seed.toLong())
    }
}
