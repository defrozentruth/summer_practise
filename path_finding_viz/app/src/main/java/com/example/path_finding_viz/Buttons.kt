package com.example.path_finding_viz

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color



@ExperimentalFoundationApi
@Composable
fun PathFind(modifier: Modifier = Modifier, onClick: () -> (Unit), enabled: Boolean = true) {
    ButtonWithText(
        modifier,
        onClick = onClick,
        label = "PathFind",
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
    )
}

@ExperimentalFoundationApi
@Composable
fun StepPathFind(modifier: Modifier = Modifier, onClick: () -> (Unit), enabled: Boolean = true) {
    ButtonWithText(
        modifier,
        onClick = onClick,
        label = "StepFind",
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta)
    )
}

@ExperimentalFoundationApi
@Composable
fun ClearButton(modifier: Modifier = Modifier, onClick: () -> (Unit)) {
    ButtonWithText(
        modifier,
        onClick = onClick,
        label = "Clear",
        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
    )
}

@ExperimentalFoundationApi
@Composable
fun OpenFile(
    modifier: Modifier = Modifier,
    onClick: () -> (Unit),
    enabled: Boolean = true
) {
    ButtonWithText(
        modifier,
        onClick = onClick,
        label = "Open file",
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
    )
}

@ExperimentalFoundationApi
@Composable
fun SaveMap(
    modifier: Modifier = Modifier,
    onClick: () -> (Unit),
    enabled: Boolean = true
) {
    ButtonWithText(
        modifier,
        onClick = onClick,
        label = "Save map",
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
    )
}

@ExperimentalFoundationApi
@Composable
private fun ButtonWithText(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    onClick: () -> (Unit),
    colors: ButtonColors
) {
    Button(onClick = onClick, modifier = modifier, colors = colors, enabled = enabled) {
        Text(text = label, color = Color.White)
    }
}
