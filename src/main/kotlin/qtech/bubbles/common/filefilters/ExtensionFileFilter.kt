package qtech.bubbles.common.filefilters

import java.io.File
import java.io.FileFilter

class ExtensionFileFilter(extension: String) : FileFilter {
    var extension: String
    override fun accept(pathname: File): Boolean {
        return if (pathname.isFile) {
            pathname.name.endsWith(extension)
        } else false
    }

    init {
        require(extension.startsWith(".")) { "Extension don't starts with a dot." }
        this.extension = extension
    }
}