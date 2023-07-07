package com.example.path_finding_viz

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class CellData(
    var type: CellType,
    val position: Position,
    val isVisited: Boolean = false,
    val isShortestPath: Boolean = false,
    var distance: Int = Int.MAX_VALUE,
    var previousShortestCell: CellData? = null,
    var id: Int = (0..Int.MAX_VALUE).random(),
    var leftJump: Int = Int.MAX_VALUE,
    var rightJump: Int = Int.MAX_VALUE,
    var downJump: Int = Int.MAX_VALUE,
    var uppJump: Int = Int.MAX_VALUE

)

enum class CellType {
    START,
    FINISH,
    WALL,
    BACKGROUND,
}

data class Position(
    var row: Int,
    var column: Int
)

data class ExtraPosition(
    var row: MutableState<Int>,
    var column: MutableState<Int>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cell(cellData: CellData, onClick: (Position) -> Unit) {
    val bgColor = animateColorAsState(
        targetValue = getBackgroundByType(cellData),
        animationSpec = tween(durationMillis = 700)
    )
    val showDialog = remember { mutableStateOf(false) }
    val leftJump = remember { mutableStateOf(cellData.leftJump) }
    val rightJump = remember { mutableStateOf(cellData.rightJump) }
    val downJump = remember { mutableStateOf(cellData.downJump) }
    val uppJump = remember { mutableStateOf(cellData.uppJump) }
    val boxModifier = Modifier

        .padding(0.dp)
        .border(BorderStroke(1.dp, Color.Gray))
        .height(16.dp)
        .background(bgColor.value)
        .fillMaxWidth()
        .clickable { showDialog.value = true }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Введите параметры") },
            text = {
                Column {
                    TextField(value = leftJump.value.toString(), onValueChange = { leftJump.value = it.toIntOrNull() ?: 0 }, label = { Text("Шаг влево") })
                    TextField(value = rightJump.value.toString(), onValueChange = { rightJump.value = it.toIntOrNull() ?: 0 }, label = { Text("Шаг вправо") })
                    TextField(value = downJump.value.toString(), onValueChange = { downJump.value = it.toIntOrNull() ?: 0}, label = { Text("Шаг вниз") })
                    TextField(value = uppJump.value.toString(), onValueChange = { uppJump.value = it.toIntOrNull() ?: 0 }, label = { Text("Шаг вверх") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    cellData.leftJump = leftJump.value
                    cellData.rightJump = rightJump.value
                    cellData.downJump = downJump.value
                    cellData.uppJump = uppJump.value
                    showDialog.value = false
                }) {
                    Text("OK")
                }
            }
        )
    }

    Box(modifier = boxModifier)
}

val CELL_BACKGROUND = Color.White
val CELL_START = Color.Red
val CELL_FINISH = Color.Green
val CELL_VISITED = Color.Gray
val CELL_PATH = Color.Yellow
val CELL_WALL = Color.Black

private fun getBackgroundByType(cellData: CellData): Color {
    if (cellData.isShortestPath && cellData.type != CellType.START && cellData.type != CellType.FINISH) return CELL_PATH
    if (cellData.isVisited && cellData.type != CellType.START && cellData.type != CellType.FINISH) return CELL_VISITED

    return when (cellData.type) {
        CellType.BACKGROUND -> CELL_BACKGROUND
        CellType.WALL -> CELL_WALL
        CellType.START -> CELL_START
        CellType.FINISH -> CELL_FINISH
    }
}
