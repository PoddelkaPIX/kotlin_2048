package com.example.a2048

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlin.properties.Delegates

class Game(context: Context) {
    enum class EStatus { Ready, Fail, Win}

    var bestScore = 0
    var grid: Grid = Grid()
    var status = EStatus.Ready

    fun restart(){
        if (bestScore <  grid.currentScore){
            bestScore = grid.currentScore
        }
        grid = Grid()
    }

    fun undo(){
//        grid.setTable(grid.oldTable!!)
    }
}