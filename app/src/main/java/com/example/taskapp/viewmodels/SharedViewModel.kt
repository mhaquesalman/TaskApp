package com.example.taskapp.viewmodels

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.models.Priority
import com.example.taskapp.models.Task
import com.example.taskapp.repositories.DataStoreRepository
import com.example.taskapp.repositories.TaskRepository
import com.example.taskapp.utils.Action
import com.example.taskapp.utils.Constants
import com.example.taskapp.utils.RequestState
import com.example.taskapp.utils.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Tasks = List<Task>

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    val searchAppBarState: MutableState<SearchAppBarState> = mutableStateOf(SearchAppBarState.CLOSED)
    val searchTextState: MutableState<String> = mutableStateOf("")

    val action: MutableState<Action> = mutableStateOf(Action.NO_ACTION)

    val id: MutableIntState = mutableIntStateOf(0)
    val title: MutableState<String> = mutableStateOf("")
    val description: MutableState<String> = mutableStateOf("")
    val priority: MutableState<Priority> = mutableStateOf(Priority.LOW)

    private val _allTasks = MutableStateFlow<RequestState<Tasks>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<Tasks>> = _allTasks
    private val _searchedTasks = MutableStateFlow<RequestState<Tasks>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<Tasks>> = _searchedTasks
    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask
    private val _lowPriorityTasks = MutableStateFlow<RequestState<Tasks>>(RequestState.Idle)
    val lowPriorityTasks: StateFlow<RequestState<Tasks>> = _lowPriorityTasks
    private val _highPriorityTasks = MutableStateFlow<RequestState<Tasks>>(RequestState.Idle)
    val highPriorityTasks: StateFlow<RequestState<Tasks>> = _highPriorityTasks


    private val _sortState = MutableStateFlow<Priority>(Priority.NONE)
    val sortState: StateFlow<Priority> = _sortState

    private val _themeState = MutableStateFlow(false)
    val themeState: StateFlow<Boolean> = _themeState

    fun getAllTasks() {
        _allTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                taskRepository.getAllTasks.collect {
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (err: Exception) {
            _allTasks.value = RequestState.Error(err)
        }
    }

    fun sortByLowPriorityTasks() {
        _lowPriorityTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                taskRepository.sortByLowPriority.collect {
                    _lowPriorityTasks.value = RequestState.Success(it)
                }
            }
        } catch (err: Exception) {
            _lowPriorityTasks.value = RequestState.Error(err)
        }
    }

    fun sortByHighPriorityTasks() {
        _highPriorityTasks.value = RequestState.Loading
        try {
            viewModelScope.launch {
                taskRepository.sortByHighPriority.collect {
                    _highPriorityTasks.value = RequestState.Success(it)
                }
            }
        } catch (err: Exception) {
            _highPriorityTasks.value = RequestState.Error(err)
        }
    }

    fun searchDatabase() {
        _searchedTasks.value = RequestState.Loading
//        Log.d("DebugTaskApp", "searchDatabase: ${searchTextState.value}")
        try {
            viewModelScope.launch {
                taskRepository.searchTasks("%${searchTextState.value}%").collect {
                    _searchedTasks.value = RequestState.Success(it)
                }
            }
        } catch (err: Exception) {
            _searchedTasks.value = RequestState.Error(err)
        }
        searchAppBarState.value = SearchAppBarState.TRIGGRED
    }

    fun getSelectedTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.getTaskById(taskId).collect{ task ->
                _selectedTask.value = task
            }
        }
    }

    private fun insertTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            taskRepository.insertTask(task)
        }
       if (searchAppBarState.value == SearchAppBarState.TRIGGRED)
           searchAppBarState.value = SearchAppBarState.CLOSED
    }

    private fun updateTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(
                id = id.intValue,
                title = title.value,
                description = description.value,
                priority = priority.value,
            )
            taskRepository.updateTask(task)
        }
    }

    fun updateCheckedTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.updateTask(task)
        }
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            val task = Task(
                id = id.intValue,
                title = title.value,
                description = description.value,
                priority = priority.value
            )
            taskRepository.deleteTask(task)
        }
    }

    private fun deleteAllTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.deleteAllTasks()
        }
    }

    fun readSortState() {
        try {
            viewModelScope.launch {
                dataStoreRepository.readSortState
                    .map {
                        Priority.valueOf(it)
                    }
                    .collect {
                        _sortState.value = it
                    }
            }
        } catch (err: Exception) {
            _sortState.value = Priority.NONE
        }
    }

    fun createSortState(priority: Priority) {
        viewModelScope.launch {
            dataStoreRepository.storeSortState(priority)
        }
    }

    fun readThemeState() {
        try {
            viewModelScope.launch {
                dataStoreRepository.readThemeState.collect {
                    _themeState.value = it
                }
            }
        } catch (err: Exception) {
            _themeState.value = false
        }
    }

    fun createThemeState(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreRepository.storeThemeState(isDark)
        }
    }

    fun updateTaskFields(selectedTask: Task?) {
        if (selectedTask != null) {
            id.intValue = selectedTask.id
            title.value = selectedTask.title
            description.value = selectedTask.description
            priority.value = selectedTask.priority
        } else {
            id.intValue = 0
            title.value = ""
            description.value = ""
            priority.value = Priority.LOW
        }
    }

    fun updateTaskTitle(newTitle: String) {
        if (newTitle.length < Constants.MAX_TITLE_LENGTH)
            title.value = newTitle

    }

    fun validateFields() : Boolean {
        return title.value.isNotEmpty() && description.value.isNotEmpty()
    }

    fun handleDbOps(action: Action) {
        when (action) {
            Action.ADD -> insertTask()
            Action.UPDATE -> updateTask()
            Action.DELETE ->  deleteTask()
            Action.DELETE_ALL ->  deleteAllTasks()
            Action.UNDO ->  insertTask()
            else -> {}
        }
    }

}


/*
val lowPriorityTasks: StateFlow<List<Task>> =
    taskRepository.sortByLowPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
val highPriorityTasks: StateFlow<List<Task>> =
    taskRepository.sortByHighPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )*/
