package com.qtech.bubbles

import com.qtech.bubbles.common.crash.CrashReport
import com.qtech.bubbles.common.crash.ReportedException
import com.qtech.bubbles.core.utils.categories.StringUtils

class BBExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        println("Error")

        try {

            if (throwable is ReportedException) {
                val crashString = throwable.crashReport.toString()
                val strings = StringUtils.splitIntoLines(crashString)
                for (string in strings) {
                    System.err.println(string)
                }
                try {
                    BubbleBlaster.instance.close()
                } catch (t: Throwable) {
                    BubbleBlaster.instance.shutdown()
                }
                return
            }

            val crashReport = CrashReport("Uncaught exception", throwable)
            val crashString = crashReport.toString()
            val strings = StringUtils.splitIntoLines(crashString)
            for (string in strings) {
                System.err.println(string)
            }
            try {
                BubbleBlaster.instance.close()
            } catch (t: Throwable) {
                BubbleBlaster.instance.shutdown()
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}