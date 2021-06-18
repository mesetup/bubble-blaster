package com.qtech.bubbles.common.crash

import com.qtech.bubbles.addon.loader.AddonContainer
import com.qtech.bubbles.addon.loader.AddonManager
import com.qtech.bubbles.common.References
import org.apache.commons.lang.SystemUtils
import oshi.SystemInfo
import java.io.*
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import javax.annotation.ParametersAreNonnullByDefault

@ParametersAreNonnullByDefault
class CrashReport : CrashCategory {
    private val categories: MutableList<CrashCategory> = ArrayList()

    constructor(details: String?, report: CrashReport?, t: Throwable?) : this(details, t) {
        if (t is ReportedException) this.throwable = addReportedException(t).throwable
        report?.let { addCrashReport(it) }
    }

    constructor(details: String?, report: CrashReport) : super(details) {
        this.throwable = addCrashReport(report).throwable
    }

    constructor(details: String?, t: Throwable?) : super(details, t) {
        if (t is ReportedException) {
            this.throwable = addReportedException(t).throwable
        } else {
            this.throwable = t
        }
    }

    private fun addCrashReport(report: CrashReport): CrashReport {
        val cat = CrashCategory(report.details, report.throwable)
        cat.entries.clear()
        cat.entries.addAll(report.entries)
        addCategory(cat)
        for (category in report.getCategories()) {
            addCategory(category)
        }
        return report
    }

    private fun addReportedException(exception: ReportedException): CrashReport {
        val crashReport = exception.crashReport
        val crashReport1 = CrashReport(crashReport.details, crashReport.throwable)
        crashReport1.categories.addAll(crashReport.categories.subList(0, crashReport.categories.size - 1))
        crashReport1.entries.addAll(crashReport.entries)
        return addCrashReport(crashReport1)
    }

    override var throwable: Throwable?
        set(throwable) {
            super.throwable = throwable
            field = throwable
        }

    fun addCategory(crashCategory: CrashCategory) {
        categories.add(crashCategory)
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getCategories(): List<CrashCategory> {
        return Collections.unmodifiableList(categories)
    }

    private val finalForm: CrashReport
        get() {
            val crashReport = CrashReport(details, throwable)
            crashReport.categories.addAll(categories)
            crashReport.entries.addAll(entries)
            val runtime = Runtime.getRuntime()
            val systemInfo = SystemInfo()
            val hardwareAbstractionLayer = systemInfo.hardware
            val category = CrashCategory("System Details")
            category.add("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"))
            category.add("CPU", hardwareAbstractionLayer.processor.processorIdentifier.name)
            category.add("GPU", hardwareAbstractionLayer.graphicsCards[0].name)
            category.add("Memory", (runtime.totalMemory() - runtime.freeMemory()).toString() + "/" + runtime.totalMemory())
            category.add(
                "Loaded Addons", AddonManager.instance.containers.stream()
                    .map(AddonContainer::namespace)
                    .collect(Collectors.joining(", "))
            )
            category.add("Constructed Addons", AddonManager.instance.containers.stream()
                .filter { container -> container.instance != null }
                .map(AddonContainer::namespace)
                .collect(Collectors.joining(", ")))
            crashReport.addCategory(category)
            return crashReport
        }
    val reportedException: ReportedException
        get() = ReportedException(finalForm)

    override fun toString(): String {
        val s1 = "// $details\r\n"
        val cs = StringBuilder()
        val sb = StringBuilder()

        if (entries.size > 0) {
            sb.append("Details:").append(System.lineSeparator())
            for ((key, value) in entries) {
                sb.append("  ").append(key)
                sb.append(": ")
                sb.append(value)
                sb.append("\r\n")
            }
        }
        for (category in categories) {
            cs.append(System.lineSeparator()).append("=------------------------------------------------------------------=")
            cs.append(SystemUtils.LINE_SEPARATOR).append(category.toString())
        }
        cs.append("=------------------------------------------------------------------=")
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        throwable?.printStackTrace(pw)
        return ">>> C R A S H   R E P O R T <<<\r\n$s1\r\n$sw$cs\r\n$sb"
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