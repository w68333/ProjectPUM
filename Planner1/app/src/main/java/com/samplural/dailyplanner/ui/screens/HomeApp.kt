package com.samplural.dailyplanner.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.samplural.dailyplanner.R
import com.samplural.dailyplanner.data.Todo
import com.samplural.dailyplanner.ui.AppViewModelProvider
import kotlinx.coroutines.runBlocking


enum class TodoScreen(@StringRes val title: Int) {
    TodoListScreen(title = R.string.todo_list_screen),
    EditTodoScreen(title = R.string.edit_todo),
}

@Composable
fun HomeApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: HomeAppViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = TodoScreen.TodoListScreen.name,
            modifier = modifier.padding(paddingValues)
        ) {
            composable(route = TodoScreen.TodoListScreen.name) {
                TodoListScreen(
                    onTodoClick = { todo ->
                        runBlocking {
                            todo.id?.let { it1 -> viewModel.updateTodo(it1) }
                        }
                        navController.navigate(
                            route = "${TodoScreen.EditTodoScreen.name}/${todo.id}"
                        )
                    },
                    onAddTodoClick = {
                        navController.navigate(route = "${TodoScreen.EditTodoScreen.name}/-1")
                    }
                )
            }
            composable(
                route = TodoScreen.EditTodoScreen.name + "/{todoId}",
                arguments = listOf(
                    navArgument("todoId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val todoId = backStackEntry.arguments?.getInt("todoId")
                if (todoId != -1) {
                    TodoEditScreen(
                        uiState = uiState.value.todoEditUiState,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { title, time, date ->
                            viewModel.saveTodoExisting(
                                Todo(
                                    id = todoId,
                                    title = title,
                                    time = time,
                                    date = date
                                )
                            )
                            navController.popBackStack()
                        }
                    )
                } else {
                    TodoEditScreen(
                        uiState = TodoEditUiState(
                            id = -1,
                            title = "",
                            time = "",
                            date = ""
                        ),
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { title, time, date ->
                            viewModel.insertTodo(
                                Todo(
                                    id = null,
                                    title = title,
                                    time = time,
                                    date = date
                                )
                            )
                            navController.popBackStack()
                        }
                    )

                }
            }
        }
    }
}






