package qtech.bubbles.core.utils.categories

import java.io.File
import java.io.PrintWriter

object FileUtils {
    @JvmStatic
    var cwd: File
        get() {
            return File(System.getProperty("user.dir"))
        }
        set(value) {
            var result = false // Boolean indicating whether directory was set

            // Desired current working directory
            val directory = value.absoluteFile
            if (directory.exists() || directory.mkdirs()) {
                System.setProperty("user.dir", directory.absolutePath)
            }
        }

    @JvmStatic
    fun setCwdRet(directory_name: File): Boolean {
        var result = false // Boolean indicating whether directory was set

        // Desired current working directory
        val directory = directory_name.absoluteFile
        if (directory.exists() || directory.mkdirs()) {
            result = System.setProperty("user.dir", directory.absolutePath) != null
        }
        return result
    }

    fun openOutputFile(file_name: String?): PrintWriter? {
        var output: PrintWriter? = null // File to open for writing
        try {
            output = PrintWriter(File(file_name).absoluteFile)
        } catch (ignored: Exception) {
        }
        return output
    }
}