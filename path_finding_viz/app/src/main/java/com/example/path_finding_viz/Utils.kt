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

fun naiveFrameEstimation(height : Int) = height*17

fun refreshCells(cells: List<List<CellData>>, state:State, reverse :Boolean){
    Log.d("daladno", "${state.width} ======= ${state.height}")
    if (!reverse){
        for (i in 0 until state.height){
            for (j in 0 until state.width){
                refreshCell(cells[i][j], state.getCell(j,i))
            }
        }}
    else {
        for (i in 0 until state.height){
            for (j in 0 until state.width){
                refreshCell(state.getCell(j,i),cells[i][j])
            }
        }
    }
}

fun refreshCell (cell1 :CellData, cell2:CellData){
    cell1.type = cell2.type
    cell1.isShortestPath = cell2.isShortestPath
    cell1.distance = cell2.distance
    cell1.isVisited = cell2.isVisited
    cell1.previousShortestCell = cell2.previousShortestCell
    cell1.priority = cell2.priority
}