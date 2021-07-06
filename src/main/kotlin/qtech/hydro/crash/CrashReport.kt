package qtech.hydro.crash

import oshi.SystemInfo
import qtech.bubbles.common.References
import qtech.bubbles.mods.loader.ModContainer
import qtech.bubbles.mods.loader.ModManager
import qtech.utilities.common.FileSize
import java.io.*
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import javax.annotation.ParametersAreNonnullByDefault
import kotlin.random.Random

@ParametersAreNonnullByDefault
class CrashReport {
    private val messages: List<String> = listOf(
        "Oops, game crashed!",
        "Hmm, something probably went wrong.",
        "Did you do something wrong?",
        "What did you do!?",
        "Uh, I guess it's time for me to CRASH.",
        "Haha, that tickles!",
        "Error",
        "It's good that your computer didn't crash. (Hopefully)",
        "Bleep bleep. Game crashed!",
        "I didn't understand that code.",
        "Did I do something?",
        "Wow, you found the hidden close button."
    )

    private val millis: Long = System.currentTimeMillis()

    private val message: String
        get() {
            return messages.random(Random(millis))
        }
    private val throwables: MutableList<Throwable> = ArrayList()
    private val categories: MutableList<CrashCategory> = ArrayList()

    var throwable: Throwable?
        private set

    constructor(report: CrashReport?, t: Throwable?) {
        this.throwable = null
        if (t is ReportedException) this.throwable = addReportedException(t).throwable
        report?.let { addCrashReport(it) }
    }

    constructor(report: CrashReport) {
        this.throwable = addCrashReport(report).throwable
    }

    constructor(t: Throwable?) {
        if (t is ReportedException) {
            this.throwable = addReportedException(t).throwable
        } else {
            this.throwable = t
        }
    }

    private fun addCrashReport(report: CrashReport): CrashReport {
        for (category in report.getCategories()) {
            addCategory(category)
        }
        return report
    }

    private fun addReportedException(exception: ReportedException): CrashReport {
        val crashReport = exception.crashReport
        val crashReport1 = CrashReport(crashReport.throwable)
        crashReport1.categories.addAll(crashReport.categories.subList(0, crashReport.categories.size))
        return addCrashReport(crashReport1)
    }

    fun addThrowable(throwable: Throwable) {
        this.throwables.add(throwable)
    }

    fun addCategory(crashCategory: CrashCategory) {
        categories.add(crashCategory)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getCategories(): List<CrashCategory> {
        return Collections.unmodifiableList(categories)
    }

    internal val finalForm: CrashReport
        get() {
            return this
        }

    val reportedException: ReportedException
        get() = ReportedException(this)

    override fun toString(): String {
        val stackTraceWrite = StringWriter()
        val stackTracePrint = PrintWriter(stackTraceWrite)
        throwable?.printStackTrace(stackTracePrint)

        val detailsString = StringBuilder()
        val s = StringBuilder()

        s.append("                 .\n")
        s.append("                / \\\n")
        s.append("               /   \\\n")
        s.append("              /     \\\n")
        s.append("             /  ###  \\\n")
        s.append("            /  #   #  \\\n")
        s.append("           /   #   #   \\\n")
        s.append("          /    #   #    \\\n")
        s.append("         /      # #      \\\n")
        s.append("        /       # #       \\\n")
        s.append("       /         #         \\\n")
        s.append("      /                     \\\n")
        s.append("     /           #           \\\n")
        s.append("    /           # #           \\\n")
        s.append("   /             #             \\\n")
        s.append("  /_____________________________\\\n")
        
        s.append(">---- C R A S H   R E P O R T ----<\n")
        s.append("// $message\n\n${stackTraceWrite.toString().replace("\r\n", "\n").replace("\r", "\n")}\n")

        val categoriesString = StringBuilder()
        for (category in categories) {
            categoriesString.append(category.toString())
        }
        s.append(categoriesString.toString())

        val crashReport = CrashReport(throwable)
        crashReport.categories.addAll(categories)
        val runtime = Runtime.getRuntime()
        val systemInfo = SystemInfo()
        val hardwareAbstractionLayer = systemInfo.hardware
        val category = CrashCategory("System Details")

        category.add("OS") {
            val osName = System.getProperty("os.name")
            val osVersion = System.getProperty("os.version")
            "$osName ($osVersion)"
        }
        category.add("CPU") { hardwareAbstractionLayer.processor.processorIdentifier.name }
        category.add("GPU") { hardwareAbstractionLayer.graphicsCards[0].name }
        category.add("Memory") {
            val usedMem = (runtime.totalMemory() - runtime.freeMemory()).toString()
            val totalMem = runtime.totalMemory().toString()
            "$usedMem / $totalMem"
        }
        category.add("Memory B") {
            val usedMem = FileSize.toString(runtime.totalMemory() - runtime.freeMemory())
            val totalMem = FileSize.toString(runtime.totalMemory())
            "$usedMem / $totalMem"
        }
        category.add("Loaded mods") {
            ModManager.instance.containers.stream()
                .map(ModContainer::namespace).collect(Collectors.joining(", ")) }
        category.add("Constructed mods") {
            ModManager.instance.containers.stream()
                .filter { container -> container.instance != null }
                .map(ModContainer::namespace).collect(Collectors.joining(", "))
        }
        s.append(category.toString())

        return s.toString().replace("\n", System.lineSeparator())
    }

    fun printCrashReport(): String {
        val stackTraceWrite = StringWriter()
        val stackTracePrint = PrintWriter(stackTraceWrite)
        throwable?.printStackTrace(stackTracePrint)

        val detailsString = StringBuilder()
        val s = StringBuilder()

        print("                 .\n")
        print("                / \\\n")
        print("               /   \\\n")
        print("              /     \\\n")
        print("             /  ###  \\\n")
        print("            /  #   #  \\\n")
        print("           /   #   #   \\\n")
        print("          /    #   #    \\\n")
        print("         /      # #      \\\n")
        print("        /       # #       \\\n")
        print("       /         #         \\\n")
        print("      /                     \\\n")
        print("     /           #           \\\n")
        print("    /           # #           \\\n")
        print("   /             #             \\\n")
        print("  /_____________________________\\\n")

        print(">---- C R A S H   R E P O R T ----<\n")
        print("// $message\n\n${stackTraceWrite.toString().replace("\r\n", "\n").replace("\r", "\n")}\n")

        val categoriesString = StringBuilder()
        for (category in categories) {
            categoriesString.append(category.toString())
        }
        print(categoriesString.toString())

        val crashReport = CrashReport(throwable)
        crashReport.categories.addAll(categories)
        val runtime = Runtime.getRuntime()
        val systemInfo = SystemInfo()
        val hardwareAbstractionLayer = systemInfo.hardware
        val category = CrashCategory("System Details")

        category.add("OS") {
            val osName = System.getProperty("os.name")
            val osVersion = System.getProperty("os.version")
            "$osName ($osVersion)"
        }
        category.add("CPU") { hardwareAbstractionLayer.processor.processorIdentifier.name }
        category.add("GPU") { hardwareAbstractionLayer.graphicsCards[0].name }
        category.add("Memory") {
            val usedMem = (runtime.totalMemory() - runtime.freeMemory()).toString()
            val totalMem = runtime.totalMemory().toString()
            "$usedMem / $totalMem"
        }
        category.add("Memory B") {
            val usedMem = FileSize.toString(runtime.totalMemory() - runtime.freeMemory())
            val totalMem = FileSize.toString(runtime.totalMemory())
            "$usedMem / $totalMem"
        }
        category.add("Loaded mods") {
            ModManager.instance.containers.stream()
                .map(ModContainer::namespace).collect(Collectors.joining(", ")) }
        category.add("Constructed mods") {
            ModManager.instance.containers.stream()
                .filter { container -> container.instance != null }
                .map(ModContainer::namespace).collect(Collectors.joining(", "))
        }
        print(category.toString())

        return s.toString().replace("\n", System.lineSeparator())
    }

    @Throws(IOException::class)
    fun writeToFile() {
        val now = LocalDateTime.now()
        val fileName = "CrashReport_" + now.format(DateTimeFormatter.ofPattern("MM.dd.yyyy-HH.mm.ss")) + ".txt"
        if (!References.CRASH_REPORTS.exists()) {
            References.CRASH_REPORTS.mkdirs()
        }
        val fileOutputStream = FileOutputStream(File(References.CRASH_REPORTS, fileName))
        fileOutputStream.write(toString().toByteArray(StandardCharsets.UTF_8))
        fileOutputStream.flush()
        fileOutputStream.close()
    }
}