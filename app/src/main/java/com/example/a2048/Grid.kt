package com.example.a2048

import android.util.Log
import java.util.Arrays
import kotlin.properties.Delegates

class Grid {
    var table: Array<Array<Int>> by Delegates.observable(arrayOf(
        arrayOf(2, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )){ _, oldValue, _ -> oldTable = oldValue}

    var oldTable: Array<Array<Int>>? = null

    var currentScore = 0

    fun updateGrid (){
        table = spawnNumber(table)
    }

    fun shiftCellsUp() {
        val rows: Array<Array<Int>> = arrayOf(
            arrayOf(table[0][0], table[1][0], table[2][0], table[3][0]),
            arrayOf(table[0][1], table[1][1], table[2][1], table[3][1]),
            arrayOf(table[0][2], table[1][2], table[2][2], table[3][2]),
            arrayOf(table[0][3], table[1][3], table[2][3], table[3][3])
        )
        val oldGrid: Array<Array<Int>> = arrayOf(
            arrayOf(table[0][0], table[1][0], table[2][0], table[3][0]),
            arrayOf(table[0][1], table[1][1], table[2][1], table[3][1]),
            arrayOf(table[0][2], table[1][2], table[2][2], table[3][2]),
            arrayOf(table[0][3], table[1][3], table[2][3], table[3][3])
        )
        val updatedGrid = table.copyOf()

        rows.map { row ->
            mergeAndOrganizeCells(row)
        }.forEachIndexed { rowIdx, row ->
            updatedGrid[0][rowIdx] = row[0]
            updatedGrid[1][rowIdx] = row[1]
            updatedGrid[2][rowIdx] = row[2]
            updatedGrid[3][rowIdx] = row[3]
        }
        Log.d("cc", checkingChanges(oldGrid, updatedGrid).toString())

        updateGrid()
    }

    fun shiftCellsDown(){
        val rows: Array<Array<Int>> = arrayOf(
            arrayOf(table[3][0], table[2][0], table[1][0], table[0][0]),
            arrayOf(table[3][1], table[2][1], table[1][1], table[0][1]),
            arrayOf(table[3][2], table[2][2], table[1][2], table[0][2]),
            arrayOf(table[3][3], table[2][3], table[1][3], table[0][3])
        )

        val updatedGrid = table.copyOf()

        rows.map { row ->
            mergeAndOrganizeCells(row)
        }.forEachIndexed { rowIdx, row ->
            updatedGrid[3][rowIdx] = row[0]
            updatedGrid[2][rowIdx] = row[1]
            updatedGrid[1][rowIdx] = row[2]
            updatedGrid[0][rowIdx] = row[3]
        }

        table = updatedGrid
        updateGrid()
    }

    fun shiftCellsLeft(){
        table = table.map { row -> mergeAndOrganizeCells(row) }.toTypedArray()
        updateGrid()
    }

    fun shiftCellsRight(){
        table = table.map { row -> mergeAndOrganizeCells(row.reversed().toTypedArray()).reversed().toTypedArray() }.toTypedArray()
        updateGrid()
    }

    fun mergeAndOrganizeCells(row: Array<Int>): Array<Int> = organize(merge(row.copyOf()))

    fun organize(row: Array<Int>, idxToMatch: Int = 0, idxToCompare: Int = 1): Array<Int> {
        if (idxToMatch >= row.size)
            return row
        if (idxToCompare >= row.size)
            return organize(row, idxToMatch + 1, idxToMatch + 2)
        if (row[idxToMatch] != 0)
            return organize(row, idxToMatch + 1, idxToMatch + 2)

        if (row[idxToCompare] != 0) {
            row[idxToMatch] = row[idxToCompare]
            row[idxToCompare] = 0

            return organize(row, idxToMatch + 1, idxToMatch + 2)
        } else {
            return organize(row, idxToMatch, idxToCompare + 1)
        }
    }
    fun merge(row: Array<Int>, idxToMatch: Int = 0, idxToCompare: Int = 1): Array<Int> {
        if (idxToMatch >= row.size)
            return row
        if (idxToCompare >= row.size)
            return merge(row, idxToMatch + 1, idxToMatch + 2)
        if (row[idxToMatch] == 0)
            return merge(row, idxToMatch + 1, idxToMatch + 2)

        if (row[idxToMatch] == row[idxToCompare]) {
            row[idxToMatch] *= 2
            row[idxToCompare] = 0

            return merge(row, idxToMatch + 1, idxToMatch + 2)
        } else {
            return if (row[idxToCompare] != 0) merge(row, idxToMatch + 1, idxToMatch + 2)
            else merge(row, idxToMatch, idxToCompare + 1)
        }
    }
    fun spawnNumber(grid: Array<Array<Int>>):Array<Array<Int>> {
        val coordinates = locateSpawnCoordinates(grid)
        val number = generateNumber()

        return updateGrid(grid, coordinates, number)
    }
    fun locateSpawnCoordinates(grid: Array<Array<Int>>): Pair<Int, Int> {
        val emptyCells = arrayListOf<Pair<Int, Int>>()
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                if (cell == 0) emptyCells.add(Pair(x, y))
            }
        }

        return emptyCells[(Math.random() * (emptyCells.size-1)).toInt()]
    }
    fun generateNumber(): Int = if (Math.random() > 0.10) 2 else 4

    fun updateGrid(grid: Array<Array<Int>>, at: Pair<Int, Int>, value: Int): Array<Array<Int>> {
        val updatedGrid = grid.copyOf()
        updatedGrid[at.first][at.second] = value
        return updatedGrid
    }

    fun checkingChanges(oldGrid: Array<Array<Int>>, newGrid: Array<Array<Int>>): Boolean{
        return table.contentDeepToString() == oldGrid.contentDeepToString()
    }
}