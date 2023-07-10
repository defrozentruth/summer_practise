package com.example.path_finding_viz

import Alg
import android.util.Log
import startA_star
import startA_star_single

class State (var height: Int, var width: Int, var startPosition: ExtraPosition, var finishPosition: ExtraPosition) {
    private var gridState: MutableList<MutableList<CellData>> = mutableListOf()
    var log = ""
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
        Log.d("beda", "refresh_grid")
        isVisualizing = false
        log = ""
        addStartAndFinishGrids()
        Log.d("bedaExtra", "${this.startPosition.column.value} ------- ${this.startPosition.row.value}")

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
Log.d("beda", "start ${this.startPosition.column.value} ------- ${this.startPosition.row.value}")
        gridState[startPosition.row.value][startPosition.column.value] =
            CellData(CellType.START, Position(startPosition.row.value, startPosition.column.value), distance = 0)
        gridState[finishPosition.row.value][finishPosition.column.value] =
            CellData(CellType.FINISH, Position(finishPosition.row.value, finishPosition.column.value))
        Log.d("beda", "finish ${this.finishPosition.column.value} ------- ${this.finishPosition.row.value}")

    }
    fun getCellAtPosition(p: Position) = gridState[p.row][p.column]
    fun getCellAtPosition(p: ExtraPosition) = gridState[p.row.value][p.column.value]
    fun getCells() = gridState
    fun getCurrentGrid(): List<List<CellData>> = gridState
    fun setCellVisitedAtPosition(p: Position) {
        gridState[p.row][p.column] = getCellAtPosition(p).copy(isVisited = true)
    }
    fun setCellShortestAtPosition(p: Position) {
        gridState[p.row][p.column] = getCellAtPosition(p).copy(isShortestPath = true)
    }

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
    fun getFinishCell() = getCellAtPosition(finishPosition)
    suspend fun animatedShortestPath() {
        isVisualizing = true
        val value = startA_star(this)
        val shortestPath = value.first
        log = value.second
//        shortestPath.forEach {
//            val p = it.position
//            Log.d("detonator", shortestPath.size.toString())
//         this.setCellShortestAtPosition(p)
//            //delay(10.toLong())
//        }
    }
    suspend fun animatedShortestPath_single(alg : Alg) {
        isVisualizing = true
        val value = startA_star_single(this, alg)
        log = value.second
    }
    @JvmName("getFinishPositionMethod")
    fun getFinishPosition() = finishPosition

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

