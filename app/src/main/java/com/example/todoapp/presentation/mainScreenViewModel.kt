package com.example.todoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope

import com.example.todoapp.di.DBContainer
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.repository.TodoRepository
import com.example.todoapp.domain.repository.TodoRepositoryImpl
import com.example.todoapp.domain.usecases.DeleteTodoUseCase
import com.example.todoapp.domain.usecases.GetAllTodoUseCase
import com.example.todoapp.domain.usecases.GetTodoByIdUseCase
import com.example.todoapp.domain.usecases.InsertTodoUsecase
import com.example.todoapp.domain.usecases.UpdateTodoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class todoViewModel(getAllTodoUseCase: GetAllTodoUseCase, val updateTodoUseCase: UpdateTodoUseCase, val deleteTodoUseCase: DeleteTodoUseCase,val insertTodoUsecase: InsertTodoUsecase): ViewModel() {
    var allTodo: LiveData<List<Todo>>

    init {
        allTodo = getAllTodoUseCase.execute()
    }



    fun insertTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        insertTodoUsecase.execute(todo)
    }
    fun updateTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("Here for update", todo.toString())
        updateTodoUseCase.execute(todo)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        deleteTodoUseCase.execute(todo)
    }

}



class StdVMFactory(val app: Application, val container: DBContainer): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = container.getDatabase(app)
        val repository = container.provideTodoRepository(db)
        val getAllTodoUseCase = GetAllTodoUseCase(repository)
        val updateTodoUseCase = UpdateTodoUseCase(repository)
        val deleteTodoUseCase = DeleteTodoUseCase(repository)
        val insertTodoUsecase = InsertTodoUsecase(repository)
        return todoViewModel(getAllTodoUseCase, updateTodoUseCase, deleteTodoUseCase, insertTodoUsecase ) as T
    }
}