package com.example.a2048

import android.content.Context

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
        grid.undo()
    }
}