package qtech.hydro.crash

import qtech.bubbles.BubbleBlaster
import qtech.hydro.Game
import kotlin.system.exitProcess

class CrashExceptionHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        println("Error")

        try {
            if (throwable is ReportedException) {
                BubbleBlaster.instance.close()
                throwable.crashReport.finalForm.printCrashReport()

                exitProcess(1)
            }

            Game.instance.close()
            val crashReport = CrashReport(throwable)
            crashReport.finalForm.printCrashReport()

            exitProcess(1)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }
}