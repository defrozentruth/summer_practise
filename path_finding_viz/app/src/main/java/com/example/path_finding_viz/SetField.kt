package com.example.path_finding_viz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetField(height: Int, width: Int, onSubmit: (Int, Int) -> Unit, startPositionX: Int,startPositionY: Int, finishPositionX: Int, finishPositionY: Int, startSubmitX:(Int) -> Unit, startSubmitY:(Int) -> Unit, finSubmitX:(Int) -> Unit,  finSubmitY:(Int) -> Unit){  // состояние, которое будет хранить значения, введенные пользователем в диалоговом окне


    val showDialog = remember { mutableStateOf(false) } // состояние, которое будет хранить флаг, показывающий, нужно ли отображать диалоговое окно

    val onClick = {
        showDialog.value = true
    }

    // Определяем диалоговое окно
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Введите значения") },
            text = {
                // Отображаем поля ввода для двух целочисленных значений
                Column (
                    modifier = Modifier.verticalScroll(rememberScrollState())

                ){
                    OutlinedTextField(
                        value = height.toString(),
                        onValueChange = { onSubmit(it.toIntOrNull() ?: 0, width) },
                        label = { Text("Высота ") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )

                    )

                    OutlinedTextField(
                        value = width.toString(),
                        onValueChange = { onSubmit(height, it.toIntOrNull() ?: 0) },
                        label = { Text("Ширина") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    OutlinedTextField(
                        value = startPositionX.toString(),
                        onValueChange = { startSubmitX(it.toIntOrNull()?:0) },
                        label = { Text("Старт X") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    OutlinedTextField(
                        value = startPositionY.toString(),
                        onValueChange = { startSubmitY(it.toIntOrNull()?:0) },
                        label = { Text("Старт Y") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    OutlinedTextField(
                        value = finishPositionX.toString(),
                        onValueChange = { finSubmitX(it.toIntOrNull()?:0) },
                        label = { Text("Финиш X") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    OutlinedTextField(
                        value = finishPositionY.toString(),
                        onValueChange = { finSubmitY(it.toIntOrNull()?:0) },
                        label = { Text("Финиш Y") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            },
            confirmButton = {
                // Определяем кнопку подтверждения, которая сохраняет введенные значения
                Button(
                    onClick = { showDialog.value = false

                    },
                    content = { Text("OK") }
                )
            }
        )
    }
    // Отображаем кнопку на правой стороне экрана
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(
            onClick = onClick,
            content = { Icon(Icons.Default.Edit, null) }
        )
    }

}