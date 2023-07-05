package com.practise.astar

class Cell {
    var posX = 0
    var posY = 0
    var visited:Boolean = false
    var accessibility = true
    var up = 0
    var right = 0
    var down = 0
    var left = 0
    var priority = 0
    var distance = -1

    fun setVisit(){
        if(!visited)
            visited = true
    }
    fun setAccessibility(){
        accessibility = !accessibility
    }

    fun setX(x: Int){
        posX = x
    }
    fun setY(y:Int){
        posY = y
    }

//    fun setUp(x:Int){
//        up = x
//    }
//    fun setRight(x:Int){
//        right = x
//    }
//    fun setDown(x:Int){
//        down = x
//    }
//    fun setLeft(x: Int){
//        left = x
//    }

}