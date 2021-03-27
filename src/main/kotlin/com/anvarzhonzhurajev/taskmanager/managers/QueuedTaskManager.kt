package com.anvarzhonzhurajev.taskmanager.managers

import com.anvarzhonzhurajev.taskmanager.processes.Process

/**
 * @author Anvarzhon Zhurajev
 * @since 0.1
 */
class QueuedTaskManager(capacity: Int) : TaskManager(capacity) {

    /**
     * Adds new process with auto-generated id
     *
     * @param priority Higher numbers mean higher priority
     * @param process  Some implementation of Process interface
     * @return id of newly added task
     *
     * After TaskManager's capacity is reached, oldest process gets killed
     * to give space for adding a new one
     */
    @Synchronized
    override fun add(priority: Int, process: Process): Int {
        if (priority <= 0) return -1

        return if (taskList.size < capacity) {
            addProcess(priority, process)
        } else {
            if (killOldest() > 0) add(priority, process) else -1
        }
    }
}