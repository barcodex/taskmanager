package com.anvarzhonzhurajev.taskmanager.managers

import com.anvarzhonzhurajev.taskmanager.processes.LoudProcess
import junit.framework.TestCase

/**
 * @author Anvarzhon Zhurajev
 * @since 0.1
 */
class QueuedTaskManagerTest : TestCase() {

    fun testAdd() {
        // Fill TaskManager until capacity is reached
        val taskManager = QueuedTaskManager(3)
        val firstId = taskManager.add(1, LoudProcess())
        assertEquals(1, firstId)
        assertEquals(1, taskManager.taskList.size)
        val secondId = taskManager.add(2, LoudProcess())
        assertEquals(2, secondId)
        assertEquals(2, taskManager.taskList.size)
        val thirdId = taskManager.add(1, LoudProcess())
        assertEquals(3, thirdId)
        assertEquals(3, taskManager.taskList.size)

        // after capacity is reached, delete oldest process before adding
        val fourthId = taskManager.add(1, LoudProcess())
        assertEquals(4, fourthId)
        assertEquals(3, taskManager.taskList.size)
        assertEquals("2,3,4", taskManager.taskList.map { it.id }.joinToString(","))
        assertEquals("2,1,1", taskManager.taskList.map { it.priority }.joinToString(","))
    }
}