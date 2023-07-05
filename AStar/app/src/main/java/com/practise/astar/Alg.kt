package com.practise.astar
import kotlin.math.abs

class Alg(var field: Field) {
    fun heuristic( x:Int, y:Int):Int{
        return  abs(x-field.finishX)+ abs(y-field.finishY)
    }
    fun AStar(): MutableMap<Cell, Cell?>{
        var x = field.startX
        var y = field.startY
        val finishX = field.finishX
        val finishY = field.finishY
        val res: MutableMap<Cell, Cell?> = mutableMapOf(field.cells[y][x] to null)
        val queue = Heap()
        while(queue.size()!=0){
            val cur = queue.extractMin()
            cur.setVisit()
            x = cur.posX
            y = cur.posY
            if(x == finishX && y == finishY)
                break
            addNextCell(x+1, y, queue, res, field.cells[y][x], field.cells[y][x].right)
            addNextCell(x-1, y, queue, res, field.cells[y][x], field.cells[y][x].left)
            addNextCell(x, y+1, queue, res, field.cells[y][x], field.cells[y][x].down)
            addNextCell(x, y-1, queue, res, field.cells[y][x], field.cells[y][x].up)
        }
        return res
    }

    fun addNextCell(x: Int, y: Int, queue:Heap, res: MutableMap<Cell, Cell?>, previousCell:Cell, roadToNew:Int){
        if( x < 0 || x >= field.sizeX || y < 0 || y >= field.sizeY || !field.cells[y][x].accessibility || field.cells[y][x].visited )
            return
        val newCell = field.cells[y][x]
        val newDistance = previousCell.distance+roadToNew
        if(newCell.distance == -1 || newCell.distance > newDistance){
            res[newCell] = previousCell
            newCell.distance = newDistance
            newCell.priority = heuristic(x, y)
            queue.put(newCell)
        }
    }

    fun retrievePath(res:MutableMap<Cell, Cell?>): MutableList<Cell>{
        val path = emptyList<Cell>().toMutableList()
        var curr: Cell? = field.cells[field.finishY][field.finishX]
        while(curr != null){
            path.add(curr)
            curr = res[curr]
        }
        path.reverse()
        return path
    }
}