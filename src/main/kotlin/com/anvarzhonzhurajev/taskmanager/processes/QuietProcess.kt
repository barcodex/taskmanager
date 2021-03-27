package com.anvarzhonzhurajev.taskmanager.processes

/**
 * @author Anvarzhon Zhurajev
 * @since 1.0
 */
class QuietProcess : Process {
    override fun cleanup() : Boolean {
        return true // let's assume that kill() is always successful
    }
}