package com.anvarzhonzhurajev.taskmanager.managers

import com.anvarzhonzhurajev.taskmanager.processes.LoudProcess
import com.anvarzhonzhurajev.taskmanager.processes.QuietProcess
import junit.framework.TestCase

class PriorityTaskManagerTest : TestCase() {
    fun testAdd() {
        // Fill TaskManager until capacity is reached
        val taskManager = PriorityTaskManager(5)
        val firstId = taskManager.add(2, LoudProcess())
        assertEquals(1, firstId)
        assertEquals(1, taskManager.taskList.size)
        val secondId = taskManager.add(1, LoudProcess())
        assertEquals(2, secondId)
        assertEquals(2, taskManager.taskList.size)
        val thirdId = taskManager.add(3, LoudProcess())
        assertEquals(3, thirdId)
        assertEquals(3, taskManager.taskList.size)
        val fourthId = taskManager.add(1, LoudProcess())
        assertEquals(4, fourthId)
        assertEquals(4, taskManager.taskList.size)
        val fifthId = taskManager.add(3, LoudProcess())
        assertEquals(5, fifthId)
        assertEquals(5, taskManager.taskList.size)

        // Add a task with priority 3 - oldest task with priority 1 should be removed
        val sixthId = taskManager.add(3, QuietProcess())
        assertEquals(6, sixthId)
        assertEquals(5, taskManager.taskList.size)
        assertEquals("1,3,4,5,6", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("2,3,1,3,3", taskManager.taskList.map { it.priority }.joinToString(","))

        // Add a task with priority 2 - oldest task with priority 1 should be removed
        val seventhId = taskManager.add(2, QuietProcess())
        assertEquals(7, seventhId)
        assertEquals(5, taskManager.taskList.size)
        assertEquals("1,3,5,6,7", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("2,3,3,3,2", taskManager.taskList.map { it.priority }.joinToString(","))

        // Try to add a task with priority 2 - should be skipped because there's no tasks with priority 1
        val failedId = taskManager.add(2, QuietProcess())
        assertEquals(-1, failedId)
        assertEquals(5, taskManager.taskList.size)
        assertEquals("1,3,5,6,7", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("2,3,3,3,2", taskManager.taskList.map { it.priority }.joinToString(","))

        // Add a task with priority 3 - oldest task with priority 2 should be removed
        val eigthId = taskManager.add(3, QuietProcess())
        assertEquals(8, eigthId)
        assertEquals(5, taskManager.taskList.size)
        assertEquals("3,5,6,7,8", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("3,3,3,2,3", taskManager.taskList.map { it.priority }.joinToString(","))

    }
}