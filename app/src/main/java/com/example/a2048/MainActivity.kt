package com.example.a2048

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import kotlin.math.abs


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    
    var game: Game = Game(this)
    private val itemsList = ArrayList<Int>()
    private val adapter = GridAdapter(itemsList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "2048 Гнебедюк"
        gestureDetector = GestureDetector(this)

        val mainView = findViewById<RecyclerView>(R.id.main_view)
        mainView.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return true
            }
        })
        val restartBtn = findViewById<Button>(R.id.restart_btn)
        val undoBtn = findViewById<Button>(R.id.undo_btn)

        game.grid.table.forEach{ col ->
            col.forEach { row -> itemsList.add(row)}
        }

        mainView.layoutManager = GridLayoutManager(this, 4);
        mainView.adapter = adapter

        restartBtn.setOnClickListener {
            game.restart()
            updateView()
        }
        undoBtn.setOnClickListener {
            game.undo()
            updateView()
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (gestureDetector.onTouchEvent(event)) {
            true
        }
        else {
            super.onTouchEvent(event)
        }
    }
    override fun onDown(e: MotionEvent): Boolean {
        return false
    }
    override fun onShowPress(e: MotionEvent) {
        return
    }
    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return false
    }
    override fun onScroll(p0: MotionEvent?, e2: MotionEvent, p2: Float, p3: Float): Boolean {
        return false
    }
    override fun onLongPress(e: MotionEvent) {
        return
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        try {
            val diffY = e2.y - e1!!.y
            val diffX = e2.x - e1.x
            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        game.grid.shiftCellsRight()
                    }
                    else {
                        game.grid.shiftCellsLeft()
                    }
                }
            }else {
                if (diffY > 0) {
                    game.grid.shiftCellsDown()
                } else {
                    game.grid.shiftCellsUp()
                }
            }
            updateView()
        }
        catch (exception: Exception) {
            exception.printStackTrace()
        }
        return true
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateView(){
        val bestScore = findViewById<TextView>(R.id.best_score)
        val currentScore = findViewById<TextView>(R.id.current_score)

        bestScore.text = game.bestScore.toString()
        currentScore.text = game.grid.currentScore.toString()

        itemsList.clear()
        game.grid.table.forEach{ col ->
            col.forEach { row -> itemsList.add(row)}
        }
        adapter.notifyDataSetChanged()
        if (isGridSolved(game.grid.table))  {
            game.status = Game.EStatus.Win
            val dialog = Dialog("Вы выиграли!", this)
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "GAME_DIALOG")
        }

        else if (isGridFull(game.grid.table)) {
            game.status = Game.EStatus.Fail
            val dialog = Dialog("Вы проиграли! :(", this)
            dialog.isCancelable = false
            dialog.show(supportFragmentManager, "GAME_DIALOG")
        }
    }

    private fun isGridSolved(grid: Array<Array<Int>>): Boolean = grid.any { row -> row.contains(2048) }

    private fun isGridFull(grid: Array<Array<Int>>): Boolean = grid.all { row -> !row.contains(0) }

}
