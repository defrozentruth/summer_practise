class Field (val sizeX: Int, val sizeY:Int, val startX:Int, val startY:Int, val finishX:Int, val finishY:Int){
    var cells =  Array(this.sizeY){Array<Cell>(this.sizeX){Cell()}}
    init{
        for(i in 0 until sizeY)
            for(j in 0 until sizeX){
                cells[i][j].posX = j
                cells[i][j].posY = i
            }
    }
}