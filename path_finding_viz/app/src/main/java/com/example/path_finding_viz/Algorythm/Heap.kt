package com.practise.astar

class Heap(){
    var queue = emptyList<Cell>().toMutableList()

    fun siftUp(index: Int){
        if(index < 0 || index >= this.queue.size){
            return
        }
        var tmpIndex = index
        var parent = (index - 1)/2
        while(tmpIndex > 0 && this.queue[parent].priority >= this.queue[tmpIndex].priority){
            val tmp = this.queue[parent]
            this.queue[parent] = this.queue[tmpIndex]
            this.queue[tmpIndex] = tmp
            val buf = tmpIndex
            tmpIndex = parent
            parent = (buf-1)/2
        }
    }

    fun siftDown(index: Int){
        if (index < 0 || index >= this.queue.size) {
            return
        }
        var minIndex = index
        var tmpIndex = index
        var left: Int
        var right: Int
        while(true) {
            left = 2*tmpIndex+1
            right = 2*tmpIndex+2
            if(right < this.queue.size && this.queue[right].priority < this.queue[minIndex].priority)
                minIndex = right
            if(left < this.queue.size && this.queue[left].priority < this.queue[minIndex].priority)
                minIndex = left
            if(minIndex == tmpIndex)
                return
            else{
                this.queue[tmpIndex]; this.queue[minIndex] = this.queue[minIndex]; this.queue[tmpIndex]
                tmpIndex = minIndex
            }
        }
    }

    fun extractMin(): Cell{
        val min_element = this.queue[0]
        this.queue[0] = this.queue[this.queue.size-1]
        this.queue.removeAt(this.queue.size - 1)
        this.siftDown(0)
        return min_element
    }

    fun put(element: Cell){
        this.queue.add(element)
        this.siftUp(this.size() - 1)
    }

    fun size(): Int{
        return this.queue.size
    }
}