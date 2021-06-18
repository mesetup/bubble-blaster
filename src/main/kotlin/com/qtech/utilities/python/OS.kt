@file:Suppress("unused")

package com.qtech.utilities.python

import com.qtech.utilities.system.User
import java.io.IOException

object OS {
    val sep: String
        get() = System.getProperty("file.separator")
    val login: String
        get() = System.getProperty("user.name")
    val user: User
        get() = User()

    fun kill(pid: Long): Boolean {
        val process = ProcessHandle.of(pid).orElseThrow()
        return process.destroy()
    }

    val pID: Long
        get() {
            val process = ProcessHandle.current()
            return process.pid()
        }

    fun system(cmd: String?): Int {
        val process: Process = try {
            Runtime.getRuntime().exec(cmd)
        } catch (e: IOException) {
            e.printStackTrace()
            return 1
        }
        return try {
            process.waitFor()
        } catch (ignored: InterruptedException) {
            -1
        }
    }

    val tid: Long
        get() = Thread.currentThread().id

    fun getTid(thread: Thread): Long {
        return thread.id
    }

    fun killThread(tid: Long): Boolean {
        for (t in Thread.getAllStackTraces().keys) {
            if (t.id == tid) {
                t.interrupt()
                return t.isInterrupted
            }
        }
        return false
    }

    @JvmOverloads
    fun killThread(thread: Thread = Thread.currentThread()): Boolean {
        thread.interrupt()
        return thread.isInterrupted
    }
}