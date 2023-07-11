import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.path_finding_viz.CellType
import com.example.path_finding_viz.ExtraPosition
import java.io.File
import com.example.path_finding_viz.State

class FieldWriter (private val context: Context){
    fun writeField(field:State, filename: String){
        context.openFileOutput(filename, Context.MODE_PRIVATE).use { out ->
            out.write("${field.width} ${field.height}\n".toByteArray())
            out.write("${field.startPosition.column.value} ${field.startPosition.row.value}\n".toByteArray())
            out.write("${field.finishPosition.column.value} ${field.finishPosition.row.value}\n".toByteArray())
            for(i in 0 until field.height)
                for(j in 0 until field.width){
                    out.write("${field.getCells()[i][j].uppJump} ${field.getCells()[i][j].leftJump} ${field.getCells()[i][j].downJump} ${field.getCells()[i][j].rightJump}\n".toByteArray())
                    if(field.getCells()[i][j].type != CellType.WALL){
                        if(i == field.height-1 && j == field.width-1)
                            out.write("Normal".toByteArray())
                        else
                            out.write("Normal\n".toByteArray())
                    }
                    else{
                        if(i == field.height-1 && j == field.width-1)
                            out.write("Unpassable".toByteArray())
                        else
                            out.write("Unpassable\n".toByteArray())
                    }
                }
        }

    }
}