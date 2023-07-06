package com.example.path_finding_viz

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@ExperimentalFoundationApi
@Composable
fun PathFindingGrid(
    height : Int,
    width : Int,
    cellData: List<CellData>,
    onClick: (Position) -> Unit
) {
    val fix_wid = if (width != 0) width else 1
    LazyVerticalGrid(
        columns = GridCells.Fixed(fix_wid),
        modifier = Modifier
            .height((17 * (height)).dp)
            .padding(2.dp)
            .border(BorderStroke(5.dp, Color.Black))
    ) {
        items(cellData) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Cell(it, onClick)
            }
        }
    }
}