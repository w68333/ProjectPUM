@file:OptIn(ExperimentalMaterial3Api::class)

package com.samplural.dailyplanner.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.samplural.dailyplanner.R
import com.samplural.dailyplanner.ui.AppViewModelProvider
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoEditScreen(
    modifier: Modifier = Modifier,
    viewModel: TodoEditViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBackClick: () -> Unit,
    onSaveClick: (String, String, String) -> Unit,
    uiState: TodoEditUiState
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val title: MutableState<String> = rememberSaveable { mutableStateOf(uiState.title) }
    val date: MutableState<String> = rememberSaveable { mutableStateOf(uiState.date) }
    val time: MutableState<String> = rememberSaveable { mutableStateOf(uiState.time) }

    if (date.value == "") {
        date.value = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate().toString()
    }
    if (time.value == "") {
        time.value = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")).toString()
    }

    val (hour, minute) = time.value.split(":")
    val timeState = remember {
        mutableStateOf(
            TimePickerState(
                initialHour = hour.toInt(),
                initialMinute = minute.toInt(),
                is24Hour = true
            )
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.TopAppBarTitleEditScreen),
                        modifier = Modifier.padding(48.dp)
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxSize()
            ) {
                item {

                    OutlinedTextField(
                        value = title.value,
                        label = {
                            Text(
                                text = "Wpisz zadanie"
                            )
                        },
                        onValueChange = {
                            title.value = it
                            uiState.title = it
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )


                    TimePicker(
                        modifier = modifier.padding(top = 8.dp),
                        state = timeState.value,
                    )

                    EditDatePicker(date = date, uiState = uiState)

                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = {
                                onBackClick()
                            },
                            modifier = modifier,
                        ) {
                            Text(
                                text = "Powrot",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        Button(
                            onClick = {
                                val formattedTime =
                                    "%02d:%02d".format(timeState.value.hour, timeState.value.minute)
                                onSaveClick(title.value, formattedTime, date.value)
                            },
                            modifier = modifier
                        ) {
                            Text(
                                text = "Dodaj",
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

        }

    }
}

@Composable
fun EditDatePicker(
    modifier: Modifier = Modifier,
    date: MutableState<String>,
    uiState: TodoEditUiState
) {

    val isOpen = remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {

        OutlinedTextField(
            readOnly = true,
            value = date.value,
            label = { Text("Data") },
            onValueChange = {
                date.value = it
                uiState.date = it
            })

        IconButton(
            onClick = { isOpen.value = true }
        ) {
            Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Kalendarz")
        }
    }

    if (isOpen.value) {
        EditDatePickerDialog(
            onAccept = {
                isOpen.value = false
                if (it != null) {
                    date.value = Instant
                        .ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                        .toString()
                }
            },
            onCancel = {
                isOpen.value = false
            }
        )
    }
}

@Composable
fun EditDatePickerDialog(
    modifier: Modifier = Modifier,
    onAccept: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = { },
        confirmButton = {
            Button(onClick = { onAccept(state.selectedDateMillis) }) {
                Text("Akcept")
            }
        },
        dismissButton = {
            Button(onClick = onCancel) {
                Text("Powrot")
            }
        }
    ) {
        DatePicker(state = state)
    }
}


