package com.example.path_finding_viz

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
import kotlinx.coroutines.delay


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
    val startPos = remember {mutableStateOf(Position(0,0))}
    val finPos = remember{mutableStateOf( Position(0, 0))}

            val state = remember(height.value, width.value, startPos, finPos) { State(height.value, width.value, startPos.value, finPos.value) }
            val currentGridState = remember(state) { mutableStateOf(state.drawCurrentGridState()) }

            val onCellClicked = { p: Position -> // пока не используется, перерисовка клеток при изменении перехоов в них не предусмотрена
                if (state.isPositionNotAtStartOrFinish(p) && !state.isVisualizing) {
                    currentGridState.value = state.drawCurrentGridState()
                }
            }

            PathFindingUi(state, currentGridState.value, onCellClicked, height, width, startPos, finPos)

    LaunchedEffect(Unit) {
                while (true) {
                    delay(10.toLong())
                    currentGridState.value = state.drawCurrentGridState()
                }
            }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PathFindingUi(state: State, cells: List<List<CellData>>, onClick: (Position) -> Unit, height: MutableState<Int>, width: MutableState<Int>, startPos : MutableState<Position>, finPos:MutableState<Position>) {
    val isVisualizeEnabled = remember { mutableStateOf(true) }
    val onPathfind: () -> Unit = {
        // state.animatedShortestPath()
        isVisualizeEnabled.value = false
    }
    val onStepPathfind: () -> Unit = {
        // state.animatedShortestPath()
        isVisualizeEnabled.value = false
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
        /*
        item {
            Text(text = "aboba", color = Color.Black)
        }*/
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
                    }, startPos.value, finPos.value,
                    {
                        start: Position ->
                        startPos.value = start
                    }, {
                        finish: Position ->
                        finPos.value = finish
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




