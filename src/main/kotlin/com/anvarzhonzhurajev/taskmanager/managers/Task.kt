package com.anvarzhonzhurajev.taskmanager.managers

import com.anvarzhonzhurajev.taskmanager.processes.Process

data class Task(
    val id: Int,
    val priority: Int,   // the bigger number is, the higher priority the task has
    val process: Process // task itself just has reference to process
)

/**
 * Delegates the kill request to process itself so it can self-clean
 */
fun Task.kill(): Boolean {
    return process.cleanup()
}