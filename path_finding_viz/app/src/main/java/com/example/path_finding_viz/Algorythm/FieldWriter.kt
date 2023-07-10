import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.path_finding_viz.CellType
import com.example.path_finding_viz.ExtraPosition
import java.io.File
import com.example.path_finding_viz.State

class FieldWriter {
    fun writeField(field:State, filename: String){
        val file = File(filename)
        file.bufferedWriter().use { out ->
            out.write("${field.width} ${field.height}\n")
            out.write("${field.startPosition.column} ${field.startPosition.row}\n")
            out.write("${field.finishPosition.column} ${field.finishPosition.row}\n")
            for(i in 0 until field.height)
                for(j in 0 until field.width){
                    out.write("${field.getCells()[i][j].uppJump} ${field.getCells()[i][j].leftJump} ${field.getCells()[i][j].downJump} ${field.getCells()[i][j].rightJump}\n")
                    if(field.getCells()[i][j].type != CellType.WALL){
                        if(i == field.height-1 && j == field.width-1)
                            out.write("Normal")
                        else
                            out.write("Normal\n")
                    }
                    else{
                        if(i == field.height-1 && j == field.width-1)
                            out.write("Unpassable")
                        else
                            out.write("Unpassable\n")
                    }
                }
        }

    }
}