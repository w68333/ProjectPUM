package com.samplural.dailyplanner.data

import kotlinx.coroutines.flow.Flow

class OfflineTodoRepository(private val todoDao: TodoDao) : TodoRepository {
    override fun getAllTodosStream(): Flow<List<Todo>> = todoDao.getAllItems()
    override fun getTodoStream(id: Int): Flow<Todo?> = todoDao.getTodo(id)
    override suspend fun insertTodo(todo: Todo) = todoDao.insert(todo)
    override suspend fun deleteTodo(todo: Todo) = todoDao.delete(todo)
    override suspend fun updateTodo(todo: Todo) = todoDao.update(todo)
    override suspend fun deleteAllTodos() = todoDao.deleteAllTodos()
    override suspend fun getTodoById(id: Int): Todo = todoDao.getTodoById(id)


}
