import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.path_finding_viz.CellType
import com.example.path_finding_viz.ExtraPosition
import java.io.File
import com.example.path_finding_viz.State


class FieldReader(private val context: Context) {
    fun readField(filename: String, field: State){
        //val file = File(filename)
        context.openFileInput(filename).bufferedReader().useLines { data ->
        //val lines = lines.first()
            val lines = data.toList()
            val sizeX = lines[0].split(" ")[0].toInt()
            val sizeY = lines[0].split(" ")[1].toInt()
            val startX = lines[1].split(" ")[0].toInt()
            val startY = lines[1].split(" ")[1].toInt()
            val finishX = lines[2].split(" ")[0].toInt()
            val finishY = lines[2].split(" ")[1].toInt()
            field.width = sizeX
            field.height = sizeY
            field.startPosition.column.value = startX
            field.startPosition.row.value = startY
            field.finishPosition.column.value = finishX
            field.finishPosition.row.value = finishY
            field.custom_init()
        //val startPos = ExtraPosition(remember {mutableStateOf(startX)}, remember {mutableStateOf(startY)})
        //val finPos = ExtraPosition(remember {mutableStateOf(finishX)}, remember {mutableStateOf(finishY)})
            var i = 0
            var j = 0
            for(k in 3 until lines.size){
            if(k % 2 != 0){
                field.getCells()[i][j].uppJump = lines[k].split(" ")[0].toInt()
                field.getCells()[i][j].rightJump = lines[k].split(" ")[1].toInt()
                field.getCells()[i][j].downJump = lines[k].split(" ")[2].toInt()
                field.getCells()[i][j].leftJump = lines[k].split(" ")[3].toInt()
                field.getCells()[i][j].isVisited = false
                if(i == startY && j == startX)
                    field.getCells()[i][j].type = CellType.START
                else
                    if (i == finishY && j == finishX)
                        field.getCells()[i][j].type = CellType.FINISH
                    else
                        field.getCells()[i][j].type = CellType.BACKGROUND
            }else{
                if(lines[k] == "Unpassable") {
                    field.getCells()[i][j].type = CellType.WALL
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
    }}
}