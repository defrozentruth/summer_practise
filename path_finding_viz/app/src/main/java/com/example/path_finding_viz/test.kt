
import android.util.Log
import com.example.path_finding_viz.CellData
import com.example.path_finding_viz.CellType
import com.example.path_finding_viz.ExtraPosition
import com.example.path_finding_viz.Position
import com.example.path_finding_viz.State
import com.example.path_finding_viz.toLinearGrid

suspend fun startA_star(gridState: State, alg:Alg): Pair<List<CellData>, String> {
    //val alg = Alg(gridState)
    val value = alg.AStarWhole()
    val map = value.first
    val path = alg.retrievePathWhole(map)

    return Pair (path,value.second)
    //animatedDijkstra(gridState)
    //return getShortestPathOrder(gridState.getFinishCell())
}

suspend fun startA_star_single(gridState: State, alg: Alg): Pair<List<CellData>, String> {
    val value = alg.AStarSingle()
    val map = value.first
    val path = alg.retrievePathSingle(map)
    Log.d("checknu", "${alg.smallLog} -------- ${value.second}")
    return Pair (path,value.second)
    //animatedDijkstra(gridState)
    //return getShortestPathOrder(gridState.getFinishCell())
}

private fun animatedDijkstra(gridState: State): List<CellData> {
    val visitedNodesInOrder = mutableListOf<CellData>()
    val unvisitedNodes = gridState.getCurrentGrid().toLinearGrid()

    while (unvisitedNodes.isNotEmpty()) {
        sortNodesByDistance(unvisitedNodes)

        val closestCell = unvisitedNodes.shift()
        if (closestCell.type == CellType.WALL) {
            continue
        }
        if (closestCell.distance == Int.MAX_VALUE) return visitedNodesInOrder

        gridState.setCellVisitedAtPosition(closestCell.position)
        visitedNodesInOrder.add(gridState.getCellAtPosition(closestCell.position))

        if (closestCell.isAtPosition(gridState.getFinishPosition())) return visitedNodesInOrder
        updateUnvisitedNeighbors(closestCell, gridState, unvisitedNodes)

        //delay(GAME_DELAY_IN_MS)
    }

    return visitedNodesInOrder
}

private fun getShortestPathOrder(finishedCell: CellData): List<CellData> {
    val nodesInShortestPathOrder = mutableListOf<CellData>()
    var currentCell: CellData? = finishedCell
    while (currentCell != null) {
        nodesInShortestPathOrder.add(0, currentCell)
        currentCell = currentCell.previousShortestCell
    }
    return nodesInShortestPathOrder
}

private fun sortNodesByDistance(unvisitedNodes: MutableList<CellData>) {
    unvisitedNodes.sortBy { it.distance }
}

private fun updateUnvisitedNeighbors(
    cell: CellData,
    gridState: State,
    gridList: MutableList<CellData>
) {
    val unvisitedNeighbors = getUnvisitedNeighbors(cell, gridState)

    for (neighbor in unvisitedNeighbors) {
        val index = gridList.findIndexByCell(neighbor)

        if (index != -1) {
            gridList[index].distance = cell.distance + 1
            gridList[index].previousShortestCell = cell
        }
    }
}

private fun getUnvisitedNeighbors(
    cell: CellData,
    gridState: State
): List<CellData> {
    val neighbors = mutableListOf<CellData>()
    val grid = gridState.getCurrentGrid()
    val (row, column) = cell.position

    if (row > 0) neighbors.add(grid[row - 1][column])
    if (row < grid.size - 1) neighbors.add(grid[row + 1][column])
    if (column > 0) neighbors.add(grid[row][column - 1])
    if (column < grid[0].size - 1) neighbors.add(grid[row][column + 1])

    return neighbors.filter { !it.isVisited }
}

fun MutableList<CellData>.shift(): CellData {
    val first = this.first()
    this.removeAt(0)
    return first
}

fun CellData.isAtPosition(position: Position) =
    this.position.row == position.row && this.position.column == position.column

fun CellData.isAtPosition(position: ExtraPosition) =
    this.position.row == position.row.value && this.position.column == position.column.value

fun MutableList<CellData>.findIndexByCell(cell: CellData): Int {
    for (i in 0 until this.size) {
        if (this[i].id == cell.id) {
            return i
        }
    }
    return -1
}