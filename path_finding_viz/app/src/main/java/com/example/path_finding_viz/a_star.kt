
import android.util.Log
import com.example.path_finding_viz.CellData
import com.example.path_finding_viz.ExtraPosition
import com.example.path_finding_viz.Position
import com.example.path_finding_viz.State

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



fun CellData.isAtPosition(position: Position) =
    this.position.row == position.row && this.position.column == position.column

fun CellData.isAtPosition(position: ExtraPosition) =
    this.position.row == position.row.value && this.position.column == position.column.value
