import kotlin.math.abs

class Alg(var field: Field) {
    var nextX = field.startX
    var nextY = field.startY
    var endedOnX = field.startX
    var endedOnY = field.startY
    val queue = Heap()
    val res: MutableMap<Cell, Cell?> = mutableMapOf(field.cells[nextY][nextX] to null)
    var log: String = ""

    private fun heuristic(x:Int, y:Int):Int{
        return  abs(x-field.finishX)+ abs(y-field.finishY)
    }

    fun AStarWhole(): MutableMap<Cell, Cell?>{
        var x = field.startX
        var y = field.startY
        val finishX = field.finishX
        val finishY = field.finishY
        val res: MutableMap<Cell, Cell?> = mutableMapOf(field.cells[y][x] to null)
        queue.put(field.cells[y][x])
        while(queue.size()!=0){
            val cur = queue.extractMin()
            cur.setVisit()
            x = cur.posX
            y = cur.posY
            log += "Рассматриваем клетку ($x, $y)  с приоритетом ${cur.priority}\n"
            if(x == finishX && y == finishY) {
                log += "Дошли до финиша\n"
                break
            }
            addNextCell(x+1, y, queue, res, field.cells[y][x], field.cells[y][x].right)
            addNextCell(x-1, y, queue, res, field.cells[y][x], field.cells[y][x].left)
            addNextCell(x, y+1, queue, res, field.cells[y][x], field.cells[y][x].down)
            addNextCell(x, y-1, queue, res, field.cells[y][x], field.cells[y][x].up)
        }
        return res
    }

    fun AStarSingle(): MutableMap<Cell, Cell?>{
        var x =nextX
        var y = nextY
        val finishX = field.finishX
        val finishY = field.finishY
        queue.put(field.cells[y][x])
        val cur = queue.extractMin()
        cur.setVisit()
        x = cur.posX
        y = cur.posY
        log += "Рассматриваем клетку ($x, $y)  с приоритетом ${cur.priority}\n"
        if(x == finishX && y == finishY){
            log += "Дошли до финиша\n"
            return res
        }
        addNextCell(x+1, y, queue, res, field.cells[y][x], field.cells[y][x].right)
        addNextCell(x-1, y, queue, res, field.cells[y][x], field.cells[y][x].left)
        addNextCell(x, y+1, queue, res, field.cells[y][x], field.cells[y][x].down)
        addNextCell(x, y-1, queue, res, field.cells[y][x], field.cells[y][x].up)
        endedOnX = x
        endedOnY = y
        return res
    }

    private fun addNextCell(x: Int, y: Int, queue:Heap, res: MutableMap<Cell, Cell?>, previousCell:Cell, roadToNew:Int){
        if( x < 0 || x >= field.sizeX || y < 0 || y >= field.sizeY) {
            log += "Клетки ($x, $y) не существует\n"
            return
        }
        if(!field.cells[y][x].accessibility) {
            log += "Клетка ($x, $y) непроходима\n"
            return
        }
        if(field.cells[y][x].visited) {
            log += "Клетка ($x, $y) уже рассмотрена\n"
            return
        }
        val newCell = field.cells[y][x]
        val newDistance = previousCell.distance+roadToNew
        if(newCell.distance == -1 || newCell.distance > newDistance){
            res[newCell] = previousCell
            newCell.distance = newDistance
            newCell.priority = heuristic(x, y)
            log += "Добавляем в очередь клетку ($x, $y)  с приоритетом ${newCell.priority}\n"
            queue.put(newCell)
        }
        val cur = queue.extractMin()
        nextX = cur.posX
        nextY = cur.posY
        queue.put(cur)
    }

    fun retrievePathWhole(res:MutableMap<Cell, Cell?>): MutableList<Cell>{
        val path = emptyList<Cell>().toMutableList()
        var curr: Cell? = field.cells[field.finishY][field.finishX]
        while(curr != null){
            path.add(curr)
            curr = res[curr]
        }
        path.reverse()
        return path
    }
    fun retrievePathSingle(res:MutableMap<Cell, Cell?>): MutableList<Cell>{
        val path = emptyList<Cell>().toMutableList()
        var curr: Cell? = field.cells[endedOnY][endedOnX]
        while(curr != null){
            path.add(curr)
            curr = res[curr]
        }
        path.reverse()
        return path
    }
}