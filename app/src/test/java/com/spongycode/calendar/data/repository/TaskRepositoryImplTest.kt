package com.spongycode.calendar.data.repository

import com.spongycode.calendar.data.local.TaskDao
import com.spongycode.calendar.data.local.TaskEntity
import com.spongycode.calendar.data.model.TaskDetailDto
import com.spongycode.calendar.data.model.TaskDto
import com.spongycode.calendar.data.model.TaskListDto
import com.spongycode.calendar.data.model.TaskResponseDto
import com.spongycode.calendar.data.remote.TaskApi
import com.spongycode.calendar.domain.model.Task
import com.spongycode.calendar.util.MockitoHelper
import com.spongycode.calendar.utils.Resource
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class TaskRepositoryImplTest {

    private lateinit var api: TaskApi
    private lateinit var dao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    @Before
    fun setUp() {
        api = mock(TaskApi::class.java)
        dao = mock(TaskDao::class.java)
        repository = TaskRepositoryImpl(api, dao)
    }

    @Test
    fun testGetAllTasksFromCloud() = runBlocking {
        val response = mock(TaskListDto::class.java)
        `when`(api.getTaskList(MockitoHelper.anyObject())).thenReturn(response)
        val result = repository.getAllTasksFromCloud().toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun testStoreTaskOnCloud() = runBlocking {
        val response = mock(TaskResponseDto::class.java)
        `when`(api.storeTask(MockitoHelper.anyObject())).thenReturn(response)
        val task = Task(
            title = "test title",
            description = "test description",
            year = 2024,
            month = 7,
            day = 12,
            timestamp = System.currentTimeMillis().toString()
        )
        `when`(api.storeTask(MockitoHelper.anyObject())).thenReturn(response)
        `when`(response.status).thenReturn("success")
        val result = repository.storeTaskOnCloud(task).toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result[1] is Resource.Success)
    }

    @Test
    fun testDeleteTaskFromCloud() = runBlocking {
        val taskList = mock(TaskListDto::class.java)
        val taskDto = mock(TaskDto::class.java)
        val timestamp = System.currentTimeMillis().toString()
        `when`(taskDto.title).thenReturn("test title$2024:7:12:$timestamp")
        `when`(taskDto.description).thenReturn("test description")
        val taskEntity = TaskEntity(
            title = "test title",
            description = "test description",
            year = 2024,
            month = 7,
            day = 12,
            timestamp = timestamp
        )
        assertNotNull(taskEntity)
        val tasks = listOf(TaskDetailDto(task_id = 0, task_detail = taskDto))
        `when`(taskList.tasks).thenReturn(tasks)
        `when`(api.getTaskList(MockitoHelper.anyObject())).thenReturn(taskList)
        val response = mock(TaskResponseDto::class.java)
        `when`(api.deleteTask(MockitoHelper.anyObject())).thenReturn(response)
        `when`(response.status).thenReturn("success")
        val result = repository.deleteTaskFromCloud(taskEntity.timestamp).toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result[1] is Resource.Success)
    }


    @Test
    fun testGetAllTasksFromLocal() = runBlocking {
        val taskEntities = listOf(mock(TaskEntity::class.java))
        `when`(dao.getAllTasks()).thenReturn(taskEntities)
        val result = repository.getAllTasksFromLocal().toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result[1] is Resource.Success)
    }


    @Test
    fun testStoreTaskOnLocal() = runBlocking {
        val task = Task(
            title = "test title",
            description = "test description",
            year = 2024,
            month = 7,
            day = 12,
            timestamp = System.currentTimeMillis().toString()
        )
        val result = repository.storeTaskOnLocal(task).toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result[1] is Resource.Success)
    }

    @Test
    fun testDeleteTaskFromLocal() = runBlocking {
        val timestamp = "test-timestamp"
        val result = repository.deleteTaskFromLocal(timestamp).toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result[1] is Resource.Success)
    }


    @Test
    fun testGetTasksByMonthFromLocal() = runBlocking {
        val year = 2024
        val month = 7
        val taskEntities = listOf(mock(TaskEntity::class.java))
        `when`(dao.getTasksByMonth(year, month)).thenReturn(taskEntities)
        val result = repository.getTasksByMonthFromLocal(year, month).toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result[1] is Resource.Success)
    }


    @Test
    fun testGetTasksByDayFromLocal() = runBlocking {
        val year = 2024
        val month = 7
        val day = 12
        val taskEntities = listOf(mock(TaskEntity::class.java))
        `when`(dao.getTasksByDay(year, month, day)).thenReturn(taskEntities)
        val result = repository.getTasksByDayFromLocal(year, month, day).toList()
        assertNotNull(result)
        assertTrue(result.isNotEmpty())
        assertTrue(result[1] is Resource.Success)
    }
}
