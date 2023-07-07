import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FieldReader() {
    fun readField(filename: String):Field{
        val file = File(filename)
        val lines = file.readLines()
        val sizeX = lines[0].split(" ")[0].toInt()
        val sizeY = lines[0].split(" ")[1].toInt()
        val startX = lines[1].split(" ")[0].toInt()
        val startY = lines[1].split(" ")[1].toInt()
        val finishX = lines[2].split(" ")[0].toInt()
        val finishY = lines[2].split(" ")[1].toInt()
        val field = Field(sizeX, sizeY, startX, startY, finishX, finishY)
        var i = 0
        var j = 0
        for(k in 3 until lines.size){
            if(k % 2 != 0){
                field.cells[i][j].up = lines[k].split(" ")[0].toInt()
                field.cells[i][j].right = lines[k].split(" ")[1].toInt()
                field.cells[i][j].down = lines[k].split(" ")[2].toInt()
                field.cells[i][j].left = lines[k].split(" ")[3].toInt()
            }else{
                if(lines[k] == "Unpassable") {
                    field.cells[i][j].accessibility = false
                }
                if(j == sizeX-1){
                    j = 0
                    i+=1
                    //ЗАЩИТА ОТ ПУСТЫХ СТРОК!!!!!!!!
                }else{
                    j+=1
                }
            }
        }

        return field
    }
}