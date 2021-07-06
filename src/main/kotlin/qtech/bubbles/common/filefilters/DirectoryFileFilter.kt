package qtech.bubbles.common.filefilters

import java.io.File
import java.io.FileFilter

class DirectoryFileFilter : FileFilter {
    override fun accept(pathname: File): Boolean {
        return pathname.isDirectory
    }
}