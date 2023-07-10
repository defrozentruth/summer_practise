package com.example.path_finding_viz

import Alg
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.path_finding_viz.ui.theme.Path_finding_vizTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val scope = CoroutineScope(Dispatchers.Default)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Path_finding_vizTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PathFindingApp()
                }
            }
        }
    }
}

@Composable
fun PathFindingApp(){
    val height = remember { mutableStateOf(10) }
    val width = remember { mutableStateOf(15) }
    val startPos = ExtraPosition(remember {
        mutableStateOf(0)
    }, remember {
        mutableStateOf(0)
    })
    val finPos = ExtraPosition(remember {
        mutableStateOf(1)
    }, remember {
        mutableStateOf(1)
    })

            val state = remember(height.value, width.value, startPos, finPos) { State(height.value, width.value, startPos, finPos) }
            val currentGridState = remember(state) { mutableStateOf(state.drawCurrentGridState()) }
            val alg = remember {
                mutableStateOf(Alg(state))
            }
            val onCellClicked = { p: Position -> // пока не используется, перерисовка клеток при изменении перехоов в них не предусмотрена
                if (state.isPositionNotAtStartOrFinish(p) && !state.isVisualizing) {
                    currentGridState.value = state.drawCurrentGridState()
                }
            }

            PathFindingUi(state, currentGridState.value, onCellClicked, height, width, startPos, finPos, alg.value)

    LaunchedEffect(Unit) {
                while (true) {
                    delay(10.toLong())
                    currentGridState.value = state.drawCurrentGridState()
                }
            }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PathFindingUi(state: State, cells: List<List<CellData>>, onClick: (Position) -> Unit, height: MutableState<Int>, width: MutableState<Int>, startPos : ExtraPosition, finPos: ExtraPosition, alg:Alg) {
    val isVisualizeEnabled = remember { mutableStateOf(true) }
    val onPathfind: () -> Unit = {
        scope.launch { state.animatedShortestPath() }
        isVisualizeEnabled.value = false
    }
    val onStepPathfind: () -> Unit = {
        scope.launch { state.animatedShortestPath_single(alg) }
        isVisualizeEnabled.value = true
    }
    val onCleared: () -> Unit = {
        state.clear()
        isVisualizeEnabled.value = true
    }
    val onOpenFile: () -> Unit = {
        //state.openFile()
    }
    val onSaveMap: () -> Unit = {
        //state.saveMap()
    }

    LazyColumn(
            modifier = Modifier
                .padding(8.dp),

    verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        item {
            PathFindingGrid(height.value, width.value, cells.toLinearGrid(), onClick) // рисует поле
        }
        Log.d("mypain", "alive")

        item {
            Text(text = state.log, color = Color.Black)
        }
        item{
            Row(modifier = Modifier.padding(8.dp)) {

                PathFind(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = onPathfind,
                    enabled = isVisualizeEnabled.value
                )
                StepPathFind(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = onStepPathfind,
                    enabled = isVisualizeEnabled.value
                )

                OpenFile(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = onOpenFile,
                    enabled = isVisualizeEnabled.value
                )
                SaveMap(
                    modifier = Modifier.padding(start = 16.dp),
                    onClick = onSaveMap,
                    enabled = isVisualizeEnabled.value
                )
                ClearButton(modifier = Modifier.padding(horizontal = 16.dp), onCleared)
                SetField(
                    height = height.value,
                    width = width.value,
                    onSubmit = { n1 :Int, n2:Int ->
                        height.value = n1
                        width.value = n2
                    }, startPos.column.value,startPos.row.value, finPos.column.value,finPos.row.value,

                    { startPosX :Int->
                        cells[startPos.row.value][startPos.column.value].type = CellType.BACKGROUND
                        if (startPosX < width.value)
                            startPos.column.value = startPosX
                        else
                            startPos.column.value = width.value-1
                        onCleared()
                        cells[startPos.row.value][startPos.column.value].type = CellType.START
                    },

                    { startPosY :Int->
                        cells[startPos.row.value][startPos.column.value].type = CellType.BACKGROUND
                        if (startPosY <height.value)
                            startPos.row.value = startPosY
                        else
                            startPos.row.value = height.value -1
                        onCleared()
                        cells[startPos.row.value][startPos.column.value].type = CellType.START
                    },

                    { finishPosX :Int->
                        cells[finPos.row.value][finPos.column.value].type = CellType.BACKGROUND
                        if (finishPosX < height.value)
                            finPos.column.value = finishPosX
                        else
                            finPos.column.value = width.value -1

                        onCleared()
                        cells[finPos.row.value][finPos.column.value].type = CellType.FINISH
                    },
                    { finishPosY :Int->
                        cells[finPos.row.value][finPos.column.value].type = CellType.BACKGROUND
                        if (finishPosY < height.value)
                            finPos.row.value = finishPosY
                        else
                            finPos.row.value = height.value-1
                        onCleared()
                        cells[finPos.row.value][finPos.column.value].type = CellType.FINISH
                    }
                )

            }
            Row(modifier = Modifier.padding(4.dp)) {
            Legend("Start", CELL_START)
            Legend("Finish", CELL_FINISH)
            Legend("Visited", CELL_VISITED)
            Legend("Wall", CELL_WALL)}
        }
    }
        }




