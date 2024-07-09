package com.samplural.dailyplanner.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samplural.dailyplanner.data.Todo
import com.samplural.dailyplanner.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeAppViewModel(
    private val todoRepository: TodoRepository,
) : ViewModel() {

    private val _homeUiState: MutableStateFlow<HomeAppUiState> = MutableStateFlow(HomeAppUiState())
    val uiState: StateFlow<HomeAppUiState> = _homeUiState.asStateFlow()

    suspend fun updateTodo(id: Int) {
        val todo = todoRepository.getTodoById(id)
        _homeUiState.value = HomeAppUiState(
            todo = todo, todoEditUiState = TodoEditUiState(
                id = todo.id,
                title = todo.title,
                time = todo.time,
                date = todo.date
            )
        )
    }

    fun insertTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.insertTodo(todo)
            _homeUiState.value = HomeAppUiState()
        }
    }

    fun saveTodoExisting(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo)
            _homeUiState.value = HomeAppUiState()
        }
    }

}

data class HomeAppUiState(
    val lastId: Int = -1,
    val todo: Todo? = null,
    val todoEditUiState: TodoEditUiState = TodoEditUiState()
)
