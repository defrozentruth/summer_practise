import android.util.Log
import com.example.path_finding_viz.CellData
import com.example.path_finding_viz.CellType
import com.example.path_finding_viz.Position
import com.example.path_finding_viz.State
import kotlinx.coroutines.delay
import kotlin.math.abs

class Alg(var field: State) {
    var nextX = field.startPosition.column.value
    var nextY = field.startPosition.row.value
    var endedOnX = field.finishPosition.column.value
    var endedOnY = field.finishPosition.row.value
    //val cells = field.getCells()
    var queue = Heap()
    val res: MutableMap<CellData, CellData?> = mutableMapOf(field.getCells()[nextY][nextX] to null)
    var finSingle:Boolean = false
    var log: String = ""
    var smallLog: String = ""
    var way: Int = 0
    var processing = true

    private fun heuristic(x:Int, y:Int):Int{
        return  abs(x-field.finishPosition.column.value)+ abs(y-field.finishPosition.row.value)
    }

    suspend fun AStarWhole():Pair<MutableMap<CellData, CellData?>, String>{
        Log.d("ves", field.getCells()[0][0].rightJump.toString())
        var x = nextX
        var y = nextY
        val finishX = field.finishPosition.column.value
        val finishY = field.finishPosition.row.value
        val res: MutableMap<CellData, CellData?> = mutableMapOf(field.getCells()[y][x] to null)
        field.setCellVisitedAtPosition(field.getCells()[y][x].position)
        queue.put(field.getCells()[y][x])
        while(queue.size() != 0){
            val cur = queue.extractMin()
            if (cur.distance != -1)
                way = cur.distance
            //println("${cur.position.column} ${cur.position.row}")
            //field.setCellShortestAtPosition(cur.position)
            Log.d("tired", "${cur.position.row} ------- ${cur.position.column}")
            delay(10.toLong())
            x = cur.position.column
            y = cur.position.row
            log += "Рассматриваем клетку ($x, $y) с приоритетом ${cur.priority}\n"
            if(x == finishX && y == finishY) {
                log += "Дошли до конечной клетки\n"
                finSingle = true
                break
            }
            queue = Heap()
            addNextCell(x+1, y, queue, res, field.getCells()[y][x], field.getCells()[y][x].rightJump)
            addNextCell(x-1, y, queue, res, field.getCells()[y][x], field.getCells()[y][x].leftJump)
            addNextCell(x, y+1, queue, res, field.getCells()[y][x], field.getCells()[y][x].downJump)
            addNextCell(x, y-1, queue, res, field.getCells()[y][x], field.getCells()[y][x].uppJump)
        }
        val shortestPath = retrievePathWhole(res)
        Log.d("chego", "${res.size}")
        shortestPath.forEach{
            field.setCellShortestAtPosition(it.position)
        }
        if(!finSingle){
            log += "Пути от старта до финиша не существует\n"
        }
        //retrievePathWhole(res)
        return Pair (res,log)
    }

    suspend fun AStarSingle(): Pair<MutableMap<CellData, CellData?>, String>{
        if(!finSingle){
            var x = nextX
            var y = nextY
            val finishX = field.finishPosition.column.value
            val finishY = field.finishPosition.row.value
            field.setCellVisitedAtPosition(field.getCells()[y][x].position)
            queue.put(field.getCells()[y][x])
            val cur = queue.extractMin()
            way = cur.distance
            //field.setCellShortestAtPosition(cur.position)
            x = cur.position.column
            y = cur.position.row
            log += "Рассматриваем клетку ($x, $y) с приоритетом ${cur.priority}\n"
            smallLog = "Рассматриваем клетку ($x, $y) с приоритетом ${cur.priority}\n"
            if (x == finishX && y == finishY) {
                log += "Дошли до конечной клетки\n"
                smallLog += "Дошли до конечной клетки\n"
                finSingle = true
                retrievePathSingle(res)
                return Pair (res,smallLog)
            }
            queue = Heap()
            addNextCell(x + 1, y, queue, res, field.getCells()[y][x], field.getCells()[y][x].rightJump)
            addNextCell(x - 1, y, queue, res, field.getCells()[y][x], field.getCells()[y][x].leftJump)
            addNextCell(x, y + 1, queue, res, field.getCells()[y][x], field.getCells()[y][x].downJump)
            addNextCell(x, y - 1, queue, res, field.getCells()[y][x], field.getCells()[y][x].uppJump)
            endedOnX = x
            endedOnY = y
        }
        val shortestPath = retrievePathSingle(res)
        Log.d("gde log", smallLog)

        shortestPath.forEach{
            field.setCellShortestAtPosition(it.position)
        }
        if(!finSingle && queue.size() == 0){
            log += "Пути от старта до финиша не существует\n"
            smallLog += "Пути от старта до финиша не существует\n"
        }
        //retrievePathSingle(res)
        return Pair(res, smallLog)
    }

    private suspend fun addNextCell(x: Int, y: Int, queue:Heap, res: MutableMap<CellData, CellData?>, previousCell:CellData, roadToNew:Int){
        if( x < 0 || x >= field.width || y < 0 || y >= field.height) {
            log += "Не можем добавить в очередь клетку ($x, $y), т.к. ее не существует\n"
            smallLog += "Не можем добавить в очередь клетку ($x, $y), т.к. ее не существует\n"
            return
        }
        if(field.getCells()[y][x].type == CellType.WALL) {
            log += "Не можем добавить в очередь клетку ($x, $y), т.к.она непроходима\n"
            smallLog += "Не можем добавить в очередь клетку ($x, $y), т.к.она непроходима\n"
            return
        }
        if(field.getCells()[y][x].isVisited) {
            log += "Не можем добавить в очередь клетку ($x, $y), т.к.она уже рассмотрена\n"
            smallLog += "Не можем добавить в очередь клетку ($x, $y), т.к.она уже рассмотрена\n"
            return
        }
        field.setCellVisitedAtPosition(Position(y,x))
        val newCell = field.getCells()[y][x]

        val newDistance = previousCell.distance+roadToNew
        if(newCell.distance == -1 || newCell.distance > newDistance){
            res[newCell] = previousCell
            newCell.distance = newDistance
            newCell.priority = heuristic(x, y) + newCell.distance
            log += "Добавляем в очередь клетку (${newCell.position.column}, ${newCell.position.row}) с приоритетом ${newCell.priority}\n"
            smallLog += "Добавляем в очередь клетку (${newCell.position.column}, ${newCell.position.row}) с приоритетом ${newCell.priority}\n"
            queue.put(newCell)
            delay(10.toLong())
        }
        val cur = queue.extractMin()
        nextX = cur.position.column
        nextY = cur.position.row
        queue.put(cur)
    }

    fun retrievePathWhole(res:MutableMap<CellData, CellData?>): MutableList<CellData>{
        val path = emptyList<CellData>().toMutableList()
        var curr: CellData? = field.getCells()[field.finishPosition.row.value][field.finishPosition.column.value]
        while(curr != null){
            path.add(curr)
            curr = res[curr]

        }
        path.reverse()
        log += "Итоговый путь:\n"
        for (elem in path)
            log += "x:${elem.position.column} y:${elem.position.row}\n"
        log += "Цена итогового пути: ${way}\n"
        return path
    }
    fun retrievePathSingle(res:MutableMap<CellData, CellData?>): MutableList<CellData>{
        val path = emptyList<CellData>().toMutableList()
        var curr: CellData? = field.getCells()[endedOnY][endedOnX]
        while(curr != null){
            path.add(curr)
            curr = res[curr]
        }
        path.reverse()

        if(finSingle && processing){
            smallLog += "Итоговый путь:\n"
            for (elem in path)
                smallLog += "x:${elem.position.column} y:${elem.position.row}\n"
            smallLog += "Цена итогового пути: ${way}\n"
        processing = false
        }
        return path
    }
}