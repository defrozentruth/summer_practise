package com.example.path_finding_viz

import android.util.Log

fun List<List<CellData>>.toLinearGrid(): MutableList<CellData> {
    val mutableList = mutableListOf<CellData>()
    for (i in this.indices) {
        for (j in this[i].indices) {
            mutableList.add(this[i][j])
        }
    }
    return mutableList
}

fun refreshCells(cells: List<List<CellData>>, state:State, reverse :Boolean){
    if (!reverse){
    for (i in 0 until state.height){
        for (j in 0 until state.width){
            refreshCell(cells[i][j], state.getCell(j,i))
        }
    }}
    else {
        for (i in 0 until state.height){
            for (j in 0 until state.width){
                Log.d("typeChange?", state.printCell(j,i))
                refreshStateCell(state, cells[i][j], i, j)
                Log.d("typeChange?", state.printCell(j,i))

            }
        }
    }
Log.d("abaldet", " start kletka v itoge ${ state.printCell(0, 0) }")
}


fun refreshCell (cell1 :CellData, cell2:CellData){
    cell1.type = cell2.type
    cell1.isShortestPath = cell2.isShortestPath
    cell1.distance = cell2.distance
    cell1.isVisited = cell2.isVisited
    cell1.previousShortestCell = cell2.previousShortestCell
    cell1.priority = cell2.priority
    cell1.leftJump = cell2.leftJump
    cell1.rightJump = cell2.rightJump
    cell1.downJump = cell2.downJump
    cell1.uppJump = cell2.uppJump
}

fun refreshStateCell (state:State, cell2:CellData, y: Int, x:Int){
    state.refreshCellAtPosition(Position(y,x), cell2.type, cell2.isVisited, cell2.isShortestPath, cell2.distance, cell2.previousShortestCell, cell2.leftJump, cell2.rightJump, cell2.downJump, cell2.uppJump, cell2.priority)
}