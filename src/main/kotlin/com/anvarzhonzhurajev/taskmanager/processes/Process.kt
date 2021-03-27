package com.anvarzhonzhurajev.taskmanager.processes

/**
 * @author Anvarzhon Zhurajev
 * @since 1.0
 */
interface Process {
    fun cleanup() : Boolean // process itself is responsible for cleaning up
}