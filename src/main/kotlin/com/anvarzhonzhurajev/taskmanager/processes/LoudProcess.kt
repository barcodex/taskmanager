package com.anvarzhonzhurajev.taskmanager.processes

/**
 * @author Anvarzhon Zhurajev
 * @since 1.0
 */
class LoudProcess : Process {
    override fun cleanup() : Boolean {
        println("LoudProcess is killed!") // LoudProcess screams when it gets killed
        return true // let's assume that kill() is always successful
    }
}