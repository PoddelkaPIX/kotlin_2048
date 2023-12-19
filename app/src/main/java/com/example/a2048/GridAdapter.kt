package com.example.a2048

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.graphics.drawable.DrawableWrapperCompat
import androidx.recyclerview.widget.RecyclerView

class GridAdapter(var items: ArrayList<Int>, var context: Context): RecyclerView.Adapter<GridAdapter.MyViewHolder>() {
    private val colors = mapOf(
        0 to Color.rgb( 250, 250, 250),
        2 to  Color.rgb( 245, 239, 184),
        4 to  Color.rgb( 219, 205, 72),
        8 to  Color.rgb(229, 162, 60 ),
        16 to  Color.rgb(229, 122, 60 ),
        32 to  Color.rgb(229, 71, 60 ),
        64 to  Color.rgb(235, 50, 9 ),
        128 to  Color.rgb(242, 40, 10 ),
        256 to  Color.rgb(245, 30, 30 ),
        512 to  Color.rgb(245, 20, 60 ),
        1024 to  Color.rgb(204, 10, 31 ),
        2048 to  Color.rgb( 255, 0, 31),
    )

    class MyViewHolder(view: View): RecyclerView.ViewHolder (view){
        val cell: LinearLayout = view.findViewById(R.id.sell)
        val cellValue: TextView = view.findViewById(R.id.sell_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 16
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var value = items[position]
        holder.cellValue.text = if (value == 0) "" else value.toString()
        if (colors.containsKey(items[position]))
        colors[items[position]]?.let { holder.cell.setBackgroundColor(it) };
        else holder.cell.setBackgroundColor(Color.rgb(255, 0,0))
    }
}