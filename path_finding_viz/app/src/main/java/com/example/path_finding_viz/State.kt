package com.example.path_finding_viz

import android.util.Log

class State (var height: Int, var width: Int, var startPosition: Position, var finishPosition: Position) {
    private var gridState: MutableList<MutableList<CellData>> = mutableListOf()

    var isVisualizing = false
        private set

    init {
        if (height == 0)
            height = 1
        if (width == 0)
            width = 1
        clear()

    }

    fun clear() {
        gridState = getInitGridState()
        isVisualizing = false
        addStartAndFinishGrids()
    }

    fun drawCurrentGridState(): List<List<CellData>> {
        val updatedGrid = getInitGridState()

        for (i in 0 until updatedGrid.size) {
            for (j in 0 until updatedGrid[i].size) {
                updatedGrid[i][j] = gridState[i][j]
            }
        }

        return updatedGrid
    }
    private fun getInitGridState() = getGridWithClearBackground(height, width)

    private fun addStartAndFinishGrids() {

        gridState[startPosition.row][startPosition.column] =
            CellData(CellType.START, startPosition, distance = 0)
        gridState[finishPosition.row][finishPosition.column] =
            CellData(CellType.FINISH, finishPosition)
    }
    fun getCellAtPosition(p: Position) = gridState[p.row][p.column]

    fun isPositionNotAtStartOrFinish(p: Position) =
        getCellAtPosition(p).type != CellType.START &&
                getCellAtPosition(p).type != CellType.FINISH

    fun toggleCellTypeToWall(p: Position) {
        if (getCellAtPosition(p).type == CellType.WALL) {
            updateCellTypeAtPosition(p, CellType.BACKGROUND)
        } else {
            updateCellTypeAtPosition(p, CellType.WALL)
        }
    }

    private fun updateCellTypeAtPosition(p: Position, cellType: CellType) {
        gridState[p.row][p.column] = getCellAtPosition(p).copy(type = cellType)
    }
}

fun getGridWithClearBackground(height: Int, width: Int): MutableList<MutableList<CellData>> {
    val mutableGrid = MutableList(height) {
        MutableList(width) {
            CellData(CellType.BACKGROUND, Position(0, 0))
        }
    }
    for (i in 0 until height) {
        for (j in 0 until width) {
            mutableGrid[i][j] = CellData(CellType.BACKGROUND, Position(i, j))
        }
    }

    return mutableGrid
}

