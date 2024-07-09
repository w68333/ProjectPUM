package com.samplural.dailyplanner.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.dailyplanner.data.Todo
import com.samplural.dailyplanner.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoEditViewModel(
    private val todoRepository: TodoRepository, val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _todoEditUiState: MutableStateFlow<TodoEditUiState> =
        MutableStateFlow(TodoEditUiState())
    val todoEditUiState: StateFlow<TodoEditUiState> = _todoEditUiState.asStateFlow().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TodoEditUiState.TIMEOUT_MILLIS),
            initialValue = TodoEditUiState()
        )

    init {
        viewModelScope.launch {
            val id = savedStateHandle.get<Int>("Id")
            val todo = id?.let { todoRepository.getTodoById(it) }
            _todoEditUiState.value = TodoEditUiState(
                id = todo?.id,
                title = todo?.title ?: "",
                time = todo?.time ?: "",
                date = todo?.date ?: ""
            )
        }
    }

    fun insertTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.insertTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo)
        }
    }
}

data class TodoEditUiState(
    var id: Int? = -1,
    var title: String = "",
    var time: String = "00:00",
    var date: String = "",
) {
    companion object {
        const val TIMEOUT_MILLIS = 5_000L
    }
}

