package com.example.a2048

import android.util.Log

class Grid {
    var table: Array<Array<Int>> = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )

    private var oldTables: MutableList<Array<Array<Int>>> = mutableListOf()
    val oldCurrentScores: MutableList<Int> = mutableListOf()

    var currentScore = 0

    init {
        updateGrid(table)
    }

    private fun updateGrid (newGrid: Array<Array<Int>>) {
        oldTables.add(table)
        oldCurrentScores.add(currentScore)
        table = spawnNumber(newGrid)
    }

    fun undo(){
        Log.d("1", oldCurrentScores.size.toString())
        Log.d("2", oldCurrentScores.joinToString("==="))

        table = oldTables.last()

        if (oldTables.size >= 2){
            oldTables.removeLast()
        }

        if (oldCurrentScores.size >= 2){
            oldCurrentScores.removeLast()
        }

        currentScore = oldCurrentScores.last()
    }

    fun shiftCellsUp() {
        val rows: Array<Array<Int>> = arrayOf(
            arrayOf(table[0][0], table[1][0], table[2][0], table[3][0]),
            arrayOf(table[0][1], table[1][1], table[2][1], table[3][1]),
            arrayOf(table[0][2], table[1][2], table[2][2], table[3][2]),
            arrayOf(table[0][3], table[1][3], table[2][3], table[3][3])
        )
        val updatedGrid = copyGrid()

        rows.map { row ->
            mergeAndOrganizeCells(row)
        }.forEachIndexed { rowIdx, row ->
            updatedGrid[0][rowIdx] = row[0]
            updatedGrid[1][rowIdx] = row[1]
            updatedGrid[2][rowIdx] = row[2]
            updatedGrid[3][rowIdx] = row[3]
        }
        if (!checkingChanges( updatedGrid)) updateGrid(updatedGrid)
    }

    fun shiftCellsDown(){
        val rows: Array<Array<Int>> = arrayOf(
            arrayOf(table[3][0], table[2][0], table[1][0], table[0][0]),
            arrayOf(table[3][1], table[2][1], table[1][1], table[0][1]),
            arrayOf(table[3][2], table[2][2], table[1][2], table[0][2]),
            arrayOf(table[3][3], table[2][3], table[1][3], table[0][3])
        )
        val updatedGrid = copyGrid()

        rows.map { row ->
            mergeAndOrganizeCells(row)
        }.forEachIndexed { rowIdx, row ->
            updatedGrid[3][rowIdx] = row[0]
            updatedGrid[2][rowIdx] = row[1]
            updatedGrid[1][rowIdx] = row[2]
            updatedGrid[0][rowIdx] = row[3]
        }

        if (!checkingChanges(updatedGrid)) updateGrid(updatedGrid)
    }

    fun shiftCellsLeft(){
        val updatedGrid = copyGrid().map { row -> mergeAndOrganizeCells(row) }.toTypedArray()
        if (!checkingChanges(updatedGrid)) updateGrid(updatedGrid)
    }

    fun shiftCellsRight(){
        val updatedGrid = copyGrid().map { row -> mergeAndOrganizeCells(row.reversed().toTypedArray()).reversed().toTypedArray() }.toTypedArray()
        if (!checkingChanges(updatedGrid)) updateGrid(updatedGrid)
    }

    fun mergeAndOrganizeCells(row: Array<Int>): Array<Int> = organize(merge(row.copyOf()))

    fun organize(row: Array<Int>, idxToMatch: Int = 0, idxToCompare: Int = 1): Array<Int> {
        if (idxToMatch >= row.size)
            return row
        if (idxToCompare >= row.size || row[idxToMatch] != 0)
            return organize(row, idxToMatch + 1, idxToMatch + 2)

        return if (row[idxToCompare] != 0) {
            row[idxToMatch] = row[idxToCompare]
            row[idxToCompare] = 0

            organize(row, idxToMatch + 1, idxToMatch + 2)
        } else {
            organize(row, idxToMatch, idxToCompare + 1)
        }
    }
    private fun merge(row: Array<Int>, idxToMatch: Int = 0, idxToCompare: Int = 1): Array<Int> {
        if (idxToMatch >= row.size)
            return row
        if (idxToCompare >= row.size || row[idxToMatch] == 0)
            return merge(row, idxToMatch + 1, idxToMatch + 2)

        if (row[idxToMatch] == row[idxToCompare]) {
            row[idxToMatch] *= 2
            row[idxToCompare] = 0
            currentScore += row[idxToMatch]

            return merge(row, idxToMatch + 1, idxToMatch + 2)
        } else {
            return if (row[idxToCompare] != 0) merge(row, idxToMatch + 1, idxToMatch + 2)
            else merge(row, idxToMatch, idxToCompare + 1)
        }
    }
    fun spawnNumber(grid: Array<Array<Int>>):Array<Array<Int>> {
        val coordinates = locateSpawnCoordinates(grid)
        val number = generateNumber()
        val updatedGrid = grid.copyOf()
        updatedGrid[coordinates.first][coordinates.second] = number
        return updatedGrid
    }
    private fun locateSpawnCoordinates(grid: Array<Array<Int>>): Pair<Int, Int> {
        val emptyCells = arrayListOf<Pair<Int, Int>>()
        grid.forEachIndexed { x, row ->
            row.forEachIndexed { y, cell ->
                if (cell == 0) emptyCells.add(Pair(x, y))
            }
        }

        return emptyCells[(Math.random() * (emptyCells.size-1)).toInt()]
    }
    private fun generateNumber(): Int = if (Math.random() > 0.10) 2 else 4

    fun checkingChanges(newGrid: Array<Array<Int>>): Boolean{
        return table.contentDeepToString() == newGrid.contentDeepToString()
    }

    private fun copyGrid(): Array<Array<Int>> {
        return arrayOf(
            arrayOf(table[0][0], table[0][1], table[0][2], table[0][3]),
            arrayOf(table[1][0], table[1][1], table[1][2], table[1][3]),
            arrayOf(table[2][0], table[2][1], table[2][2], table[2][3]),
            arrayOf(table[3][0], table[3][1], table[3][2], table[3][3])
        )
    }
}