import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FieldWriter {
    fun writeField(field:Field, filename: String){
        val file = File(filename)
        file.bufferedWriter().use { out ->
            out.write("${field.sizeX} ${field.sizeY}\n")
            out.write("${field.startX} ${field.startY}\n")
            out.write("${field.finishX} ${field.finishY}\n")
            for(i in 0 until field.sizeY)
                for(j in 0 until field.sizeX){
                    out.write("${field.cells[i][j].up} ${field.cells[i][j].left} ${field.cells[i][j].down} ${field.cells[i][j].right}\n")
                    if(field.cells[i][j].accessibility){
                        if(i == field.sizeY-1 && j == field.sizeX-1)
                            out.write("Normal")
                        else
                            out.write("Normal\n")
                    }
                    else{
                        if(i == field.sizeY-1 && j == field.sizeX-1)
                            out.write("Unpassable")
                        else
                            out.write("Unpassable\n")
                    }
                }
        }

    }
}