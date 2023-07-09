import com.example.path_finding_viz.CellData
import com.example.path_finding_viz.CellType
import com.example.path_finding_viz.State
import kotlinx.coroutines.delay
import kotlin.math.abs

class Alg(var field: State) {
    var nextX = field.startPosition.column.value
    var nextY = field.startPosition.row.value
    var endedOnX = field.finishPosition.column.value
    var endedOnY = field.finishPosition.row.value
    val cells = field.getCells()
    val queue = Heap()
    val res: MutableMap<CellData, CellData?> = mutableMapOf(cells[nextY][nextX] to null)
    var finSingle:Boolean = false
    var log: String = ""

    private fun heuristic(x:Int, y:Int):Int{
        return  abs(x-field.finishPosition.column.value)+ abs(y-field.finishPosition.row.value)
    }

    suspend fun AStarWhole():Pair<MutableMap<CellData, CellData?>, String>{
        var x = nextX
        var y = nextY
        val finishX = field.finishPosition.column.value
        val finishY = field.finishPosition.row.value
        val res: MutableMap<CellData, CellData?> = mutableMapOf(cells[y][x] to null)

        queue.put(cells[y][x])
        while(!finSingle){
            val cur = queue.extractMin()
            println("${cur.position.column} ${cur.position.row}")
            field.setCellShortestAtPosition(cur.position)
            delay(10.toLong())
            x = cur.position.column
            y = cur.position.row
            log += "Рассматриваем клетку ($x, $y) с приоритетом ${cur.priority}\n"
            if(x == finishX && y == finishY) {
                log += "Дошли до конечной клетки\n"
                finSingle = true
                break
            }
            addNextCell(x+1, y, queue, res, cells[y][x], cells[y][x].rightJump)
            addNextCell(x-1, y, queue, res, cells[y][x], cells[y][x].leftJump)
            addNextCell(x, y+1, queue, res, cells[y][x], cells[y][x].downJump)
            addNextCell(x, y-1, queue, res, cells[y][x], cells[y][x].uppJump)
        }
        return Pair (res,log)
    }

    suspend fun AStarSingle(): Pair<MutableMap<CellData, CellData?>, String>{
        if(!finSingle){
            var x = nextX
            var y = nextY
            val finishX = field.finishPosition.column.value
            val finishY = field.finishPosition.row.value
            queue.put(cells[y][x])
            val cur = queue.extractMin()
            field.setCellShortestAtPosition(cur.position)
            x = cur.position.column
            y = cur.position.row
            log += "Рассматриваем клетку ($x, $y) с приоритетом ${cur.priority}\n"
            if (x == finishX && y == finishY) {
                log += "Дошли до конечной клетки\n"
                finSingle = true
                return Pair (res,log)
            }
            addNextCell(x + 1, y, queue, res, cells[y][x], cells[y][x].rightJump)
            addNextCell(x - 1, y, queue, res, cells[y][x], cells[y][x].leftJump)
            addNextCell(x, y + 1, queue, res, cells[y][x], cells[y][x].downJump)
            addNextCell(x, y - 1, queue, res, cells[y][x], cells[y][x].uppJump)
            endedOnX = x
            endedOnY = y
        }
        return Pair(res, log)
    }

    private suspend fun addNextCell(x: Int, y: Int, queue:Heap, res: MutableMap<CellData, CellData?>, previousCell:CellData, roadToNew:Int){
        if( x < 0 || x >= field.width || y < 0 || y >= field.height) {
            log += "Не можем добавить в очередь клетку ($x, $y), т.к. ее не существует\n"
            return
        }
        if(field.getCells()[y][x].type == CellType.WALL) {
            log += "Не можем добавить в очередь клетку ($x, $y), т.к.она непроходима\n"
            return
        }
        if(field.getCells()[y][x].isVisited) {
            log += "Не можем добавить в очередь клетку ($x, $y), т.к.она уже рассмотрена\n"
            return
        }
        val newCell = field.getCells()[y][x]
        val newDistance = previousCell.distance+roadToNew
        if(newCell.distance == -1 || newCell.distance > newDistance){
            res[newCell] = previousCell
            newCell.distance = newDistance
            newCell.priority = heuristic(x, y)
            log += "Добавляем в очередь клетку (${newCell.position.column}, ${newCell.position.row}) с приоритетом ${newCell.priority}\n"
            queue.put(newCell)
            delay(10.toLong())
        }
        val cur = queue.extractMin()
        nextX = cur.position.column
        nextY = cur.position.row
        queue.put(cur)
        field.setCellVisitedAtPosition(cur.position)
    }

    fun retrievePathWhole(res:MutableMap<CellData, CellData?>): MutableList<CellData>{
        val path = emptyList<CellData>().toMutableList()
        var curr: CellData? = cells[field.finishPosition.row.value][field.finishPosition.column.value]
        while(curr != null){
            path.add(curr)
            curr = res[curr]
        }
        path.reverse()
        return path
    }
    fun retrievePathSingle(res:MutableMap<CellData, CellData?>): MutableList<CellData>{
        val path = emptyList<CellData>().toMutableList()
        var curr: CellData? = cells[endedOnY][endedOnX]
        while(curr != null){
            path.add(curr)
            curr = res[curr]
        }
        path.reverse()
        return path
    }
}