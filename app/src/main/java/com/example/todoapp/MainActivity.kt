package com.example.todoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarResult.*
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.todoapp.database.Todo
import com.example.todoapp.ui.theme.TodoAppTheme
import com.example.todoapp.viewmodel.StdVMFactory
import com.example.todoapp.viewmodel.todoViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var vModel: todoViewModel
    var resultLauncherForAdd = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data

            val title = data?.getStringExtra("title")!!
            vModel.insertTodo(Todo( title = title))
        }
    }
    var resultLauncherForUpdate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val title = data?.getStringExtra("title")!!
            val id = data.getIntExtra("id", 0)
            val isDone = data.getBooleanExtra("isDone", false)
            vModel.updateTodo(Todo(id,title,isDone))
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val owner = LocalViewModelStoreOwner.current!!
                    vModel = ViewModelProvider(
                        owner,
                        StdVMFactory(LocalContext.current.applicationContext as Application)
                    )[todoViewModel::class.java]

                    Greeting(vModel, this, resultLauncherForAdd, resultLauncherForUpdate)
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Greeting(viewModel: todoViewModel, context: Context, resultLauncherForAdd: ActivityResultLauncher<Intent>,resultLauncherForUpdate: ActivityResultLauncher<Intent>, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var dismissState: DismissState? = null
    fun dismissed(todoItem: Todo) {
        coroutineScope.launch {
            val snackResult = snackbarHostState.showSnackbar(
                message = "Task deleted",
                actionLabel = "Undo",
                SnackbarDuration.Short
            )
            when (snackResult) {


                androidx.compose.material.SnackbarResult.Dismissed -> {}
                androidx.compose.material.SnackbarResult.ActionPerformed -> {
                    viewModel.insertTodo(todoItem)
                    dismissState?.reset()
                }
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context,AddScreen::class.java)
                resultLauncherForAdd.launch(intent)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {
        Surface(modifier = Modifier.fillMaxWidth()) {
//            Text(text = "Hi")
            val todoList by remember {
                viewModel.allTodo
            }.observeAsState(initial = emptyList())

            Log.d("this", todoList.toString())
            LazyColumn {
                items(key = { todo -> todo.id }, items = todoList) { todo ->

                    dismissState = rememberDismissState(
                        confirmValueChange = {
                            when (it) {
                                DismissValue.DismissedToEnd -> {
                                    viewModel.deleteTodo(todo)
                                    Log.d("Deletijg","done")
                                    Log.d("Current", todoList.toString())
                                    dismissed(todo)
                                    true
                                }

                                else -> true
                            }
                        }
                    )

                    dismissState?.let { dismissState ->
                        SwipeToDismiss(
                            state = dismissState,
                            modifier = Modifier
                                .padding(vertical = 1.dp),
                            directions = setOf(
                                DismissDirection.StartToEnd
                            ),
                            background = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        DismissValue.Default -> Color.Transparent
                                        else -> Color.Red
                                    }
                                )
                                val alignment = Alignment.CenterEnd
                                val icon = Icons.Default.Delete

                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                )

                                if (dismissState.currentValue == DismissValue.Default) {
                                    Box(
                                        Modifier
                                            .fillMaxSize()
                                            .background(color)
                                            .padding(horizontal = Dp(20f)),
                                        contentAlignment = alignment
                                    ) {
                                    }
                                }
                            },
                            dismissContent = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Checkbox(
                                        checked = todo.isDone,
                                        onCheckedChange = { isChecked ->
                                            viewModel.updateTodo(todo.copy(isDone = isChecked))
                                        },
                                        colors = CheckboxDefaults.colors(checkmarkColor = Color.White),
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = todo.title,
                                        textDecoration = if(todo.isDone) TextDecoration.LineThrough else TextDecoration.None,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                val intent = Intent(context, AddScreen::class.java)
                                                intent.putExtra("id", todo.id)
                                                intent.putExtra("title", todo.title)
                                                intent.putExtra("isDone", todo.isDone)
                                                resultLauncherForUpdate.launch(intent)
                                            }
                                    )
                                    IconButton(onClick = {
                                        val intent = Intent(context, AddScreen::class.java)
                                        intent.putExtra("id", todo.id)
                                        intent.putExtra("title", todo.title)
                                        intent.putExtra("isDone", todo.isDone)
                                        resultLauncherForUpdate.launch(intent)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit"
                                        )
                                    }
                                    IconButton(onClick = {
                                        viewModel.deleteTodo(todo)
                                        coroutineScope.launch {
                                            Log.d("Snackbar test", "reached here")
                                            val snackResult =   snackbarHostState.showSnackbar(
                                                message = "Task deleted",
                                                actionLabel = "Undo"
                                            )
                                            when (snackResult) {
                                                androidx.compose.material.SnackbarResult.Dismissed -> TODO()
                                                androidx.compose.material.SnackbarResult.ActionPerformed -> {
                                                    viewModel.insertTodo(todo)
                                                    dismissState?.reset()
                                                }
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete"
                                        )
                                    }
                                }
                            })
                    }
                }
            }
        }
    }
}




