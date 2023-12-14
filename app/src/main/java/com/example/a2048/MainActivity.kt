package com.example.a2048

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener {
    private lateinit var gestureDetector: GestureDetector
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100
    
    var game: Game = Game(this)
    val itemsList = ArrayList<Int>()
    val adapter = GridAdapter(itemsList, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"
        gestureDetector = GestureDetector(this)

        val mainView = findViewById<RecyclerView>(R.id.main_view)
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
                        Toast.makeText(applicationContext, "Право", Toast.LENGTH_SHORT).show()
                        game.grid.shiftCellsRight()
                    }
                    else {
                        Toast.makeText(applicationContext, "Лево", Toast.LENGTH_SHORT).show()
                        game.grid.shiftCellsLeft()
                    }
                }
            }else {
                if (diffY > 0) {
                    Toast.makeText(applicationContext, "Вниз", Toast.LENGTH_SHORT).show()
                    game.grid.shiftCellsDown()
                } else {
                    Toast.makeText(applicationContext, "Вверх", Toast.LENGTH_SHORT).show()
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
    }
}
