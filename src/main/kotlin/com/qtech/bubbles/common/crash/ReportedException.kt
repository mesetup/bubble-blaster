package com.qtech.bubbles.common.crash

import com.qtech.bubbles.BubbleBlaster
import com.qtech.bubbles.core.utils.categories.StringUtils
import java.io.PrintStream
import java.io.PrintWriter

class ReportedException internal constructor(val crashReport: CrashReport) : RuntimeException() {
    override fun printStackTrace() {
        val crashString = crashReport.toString()
        val strings = StringUtils.splitIntoLines(crashString)
        for (string in strings) {
            System.err.println(string)
        }
        BubbleBlaster.instance.shutdown()
    }



    override fun printStackTrace(err: PrintWriter) {
        val crashString = crashReport.toString()
        val strings = StringUtils.splitIntoLines(crashString)
        for (string in strings) {
            err.println(string)
        }
        BubbleBlaster.instance.shutdown()
    }

    override fun printStackTrace(err: PrintStream) {
        val crashString = crashReport.toString()
        val strings = StringUtils.splitIntoLines(crashString)
        for (string in strings) {
            err.println(string)
        }
        BubbleBlaster.instance.shutdown()
    }
}