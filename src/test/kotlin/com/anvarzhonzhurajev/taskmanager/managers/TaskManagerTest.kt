package com.anvarzhonzhurajev.taskmanager.managers

import com.anvarzhonzhurajev.taskmanager.processes.LoudProcess
import com.anvarzhonzhurajev.taskmanager.processes.QuietProcess
import junit.framework.TestCase

/**
 * @author Anvarzhon Zhurajev
 * @since 0.1
 */
class TaskManagerTest : TestCase() {

    fun testCapacity() {
        val taskManager = TaskManager(12)
        assertEquals(12, taskManager.capacity)
    }

    fun testAdd() {
        // Fill TaskManager until capacity is reached
        val taskManager = TaskManager(3)
        val firstId = taskManager.add(1, LoudProcess())
        assertEquals(1, taskManager.taskList.size)
        assertEquals(1, firstId)
        val secondId = taskManager.add(2, LoudProcess())
        assertEquals(2, taskManager.taskList.size)
        assertEquals(2, secondId)
        val thirdId = taskManager.add(1, LoudProcess())
        assertEquals(3, taskManager.taskList.size)
        assertEquals(3, thirdId)

        // Normal behaviour of vanilla TaskManager: ignore additions after capacity is reached
        taskManager.add(1, LoudProcess())
        assertEquals(3, taskManager.taskList.size)
        assertEquals("1,2,3", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("1,2,1", taskManager.taskList.map { it.priority }.joinToString(","))
    }

    fun testKillById() {
        val taskManager = TaskManager(3)
        val firstId = taskManager.add(1, LoudProcess())
        val secondId = taskManager.add(2, LoudProcess())
        assertEquals(2, taskManager.taskList.size)
        val deletedFirstId = taskManager.killById(firstId)
        assertEquals(firstId, deletedFirstId)
        assertEquals(1, taskManager.taskList.size)
        assertEquals(2, taskManager.taskList[0].id)
        val deletedSecondId = taskManager.killById(2)
        assertEquals(secondId, deletedSecondId)
        assertEquals(0, taskManager.taskList.size)
    }

    fun testKillGroup() {
        // Fill TaskManager with some tasks within capacity
        val taskManager = TaskManager(4)
        taskManager.add(1, LoudProcess())
        taskManager.add(2, LoudProcess())
        taskManager.add(1, QuietProcess())
        taskManager.add(2, QuietProcess())
        assertEquals("1,2,3,4", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("1,2,1,2", taskManager.taskList.map { it.priority }.joinToString(","))

        taskManager.killGroup(1);
        assertEquals("2,4", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("2,2", taskManager.taskList.map { it.priority }.joinToString(","))
        taskManager.killGroup(2);
        assertEquals(0, taskManager.taskList.size)
    }

    fun testKillAll() {
        // Fill TaskManager with some tasks within capacity
        val taskManager = TaskManager(4)
        var newTaskId = taskManager.add(1, LoudProcess())
        newTaskId = taskManager.add(2, LoudProcess())
        newTaskId = taskManager.add(1, QuietProcess())
        newTaskId = taskManager.add(2, QuietProcess())
        assertEquals("1,2,3,4", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("1,2,1,2", taskManager.taskList.map { it.priority }.joinToString(","))

        taskManager.killAll()
        assertEquals(0, taskManager.taskList.size)
    }
}