package com.samplural.dailyplanner.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodosStream(): Flow<List<Todo>>
    fun getTodoStream(id: Int): Flow<Todo?>
    suspend fun insertTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteAllTodos()
    suspend fun getTodoById(id: Int): Todo
}