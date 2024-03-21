package com.example.todoapp.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.todoapp.di.DBContainer
import com.example.todoapp.domain.model.Todo
import com.example.todoapp.domain.usecases.DeleteTodoUseCase
import com.example.todoapp.domain.usecases.GetAllTodoUseCase
import com.example.todoapp.domain.usecases.GetTodoByIdUseCase
import com.example.todoapp.domain.usecases.InsertTodoUsecase
import com.example.todoapp.domain.usecases.UpdateTodoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class addScreenViewModel( val insertTodoUsecase: InsertTodoUsecase, val getTodoByIdUseCase: GetTodoByIdUseCase,val updateTodoUseCase: UpdateTodoUseCase,): ViewModel()  {
    fun insertTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        insertTodoUsecase.execute(todo)
    }

    fun getTodoById(id: Int): Todo? {
        return getTodoByIdUseCase.execute(id)
    }

    fun updateTodo(todo: Todo) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("Here for update", todo.toString())
        updateTodoUseCase.execute(todo)
    }

}

class addScreenStdVMFactory(val app: Application, val container: DBContainer): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = container.getDatabase(app)
        val repository = container.provideTodoRepository(db)
        val insertTodoUsecase = InsertTodoUsecase(repository)
        val getTodoByIdUseCase = GetTodoByIdUseCase(repository)
        val updateTodoUseCase = UpdateTodoUseCase(repository)
        return addScreenViewModel(insertTodoUsecase,getTodoByIdUseCase, updateTodoUseCase,) as T
    }
}