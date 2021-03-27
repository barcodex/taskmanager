package com.anvarzhonzhurajev.taskmanager.managers

import com.anvarzhonzhurajev.taskmanager.processes.Process

enum class SortingOptions {
    OLDEST, NEWEST, PRIORITY_DESC, PRIORITY_ASC, NONE
}

/**
 * @author Anvarzhon Zhurajev
 * @since 1.0
 *
 * TaskManager class can be extended in regards of how new processes are added.
 * That's why its `add()` method is declared `open`
 */
open class TaskManager(val capacity: Int) {
    var taskList: MutableList<Task> = mutableListOf()
    var lastProcessId = 0

    /**
     * Adds new process with auto-generated id
     *
     * @param priority Higher numbers mean higher priority
     * @param process  Some implementation of Process interface
     * @return id of newly added task or -1 on failure:
     *
     * After TaskManager's capacity is reached, new add() calls should return -1
     *
     * Note "task" here stands for "process implementation with id and priority".
     * From business point of view, we can use "test" and "process" interchangeably
     */
    @Synchronized
    open fun add(priority: Int, process: Process): Int {
        if (priority <= 0) return -1

        return if (taskList.size < capacity) { // TODO handle concurrent add() calls
            addProcess(priority, process)
        } else {
            println("Could not add a new process because capacity is exhausted")
            -1
        }
    }

    /**
     * Lists all the tasks
     *
     * @param option Sorting option
     * @return sorted list of tasks (NONE option returns `taskList`as it is)
     *
     * It's left to responsibility of the caller of this method to format the results
     * Note: since processes receive incremental ids, NONE sorting equals OLDEST
     */
    fun list(option: SortingOptions = SortingOptions.NONE): List<Task> {
        return when (option) {
            SortingOptions.OLDEST -> taskList.sortedBy { it.id }
            SortingOptions.NEWEST -> taskList.sortedByDescending { it.id }
            SortingOptions.PRIORITY_ASC -> taskList.sortedBy { it.priority }
            SortingOptions.PRIORITY_DESC -> taskList.sortedByDescending { it.priority }
            SortingOptions.NONE -> taskList
        }
    }

    /**
     * Deletes process by given id
     *
     * @param id of the task we want to delete
     * @return id of the deleted process or -1 on failure:
     *
     * Failure can be caused by invalid id (no task with given id is listed),
     * or by an unsuccessful attempt of killing a task
     */
    fun killById(id: Int): Int {
        val task = taskList.find { it.id == id } ?: return -1
        return if (killAndRemoveTask(task)) task.id else -1
    }

    /**
     * Deletes oldest process
     *
     * @return id of the deleted process or -1 on failure:
     *
     * Failure can be caused by an unsuccessful attempt of killing a task
     */
    fun killOldest(): Int {
        val task = if (taskList.size > 0) taskList.first() else return -1
        return if (killAndRemoveTask(task)) task.id else -1
    }

    /**
     * Deletes all processes with given priority
     *
     * @param priority desired priority of the tasks to delete
     * @return list of ids of the deleted processes
     */
    fun killGroup(priority: Int): List<Int> {
        return killAndRemoveMultipleTasks(taskList.filter { it.priority == priority })
    }

    /**
     * Deletes all processes
     *
     * @return list of ids of the deleted processes
     */
    fun killAll(): List<Int> {
        return killAndRemoveMultipleTasks(taskList.filter { true })
    }

    /**
     * Adds process to the list with given priority
     *
     * @param priority task's priority, bigger number means higher priority
     * @param process Any object that implements Process interface
     *
     * Since this process mutates lastProcessId, it is synchronized
     */
    @Synchronized
    protected fun addProcess(priority: Int, process: Process): Int {
        val processId = lastProcessId + 1
        if (taskList.add(Task(processId, priority, process))) {
            lastProcessId = processId
        }
        return processId
    }

    protected fun killAndRemoveMultipleTasks(tasks: List<Task>): List<Int> {
        val deletedTaskIds = mutableListOf<Int>()
        tasks.forEach {
            if (killAndRemoveTask(it)) {
                deletedTaskIds.add(it.id)
            }
        }
        return deletedTaskIds
    }

    protected fun killAndRemoveTask(task: Task): Boolean {
        try {
            if (task.kill()) {
                taskList.remove(task)
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }
}
