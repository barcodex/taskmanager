package com.anvarzhonzhurajev.taskmanager.managers

import com.anvarzhonzhurajev.taskmanager.processes.Process

/**
 * @author Anvarzhon Zhurajev
 * @since 0.1
 */
class PriorityTaskManager(capacity: Int) : TaskManager(capacity) {

    /**
     * Adds new process with auto-generated id
     *
     * @param priority Higher numbers mean higher priority
     * @param process  Some implementation of Process interface
     * @return id of newly added task
     *
     * After TaskManager's capacity is reached, oldest one from lower priority tasks
     * gets killed to give space for adding a new one
     */
    @Synchronized
    override fun add(priority: Int, process: Process): Int {
        if (priority <= 0) return -1

        return if (taskList.size < capacity) {
            addProcess(priority, process)
        } else {
            if (killOldestLowerPriority(priority) > 0) add(priority, process) else -1
        }
    }

    /**
     * Deletes process with the lowest priority
     *
     * @param priority priority of more important task
     * @return id of the deleted process or -1 on failure:
     *
     * Failure can be caused by absence of tasks with lower priority
     * or by an unsuccessful attempt of killing a task
     */
    private fun killOldestLowerPriority(priority: Int): Int {
        // group tasks by priority
        val tasksByPriority = taskList
            .filter { it.priority < priority } // only take lower priority tasks
            .groupBy { it.priority }
            .toSortedMap() // groups are now sorted by priority (ascending)
        if (tasksByPriority.size == 0) {
            return -1;
        }
        // now, search for oldest task from those with lowest priority
        val task = tasksByPriority[tasksByPriority.firstKey()]?.minByOrNull { it.id } ?: return -1
        return if (killAndRemoveTask(task)) task.id else -1
    }


}