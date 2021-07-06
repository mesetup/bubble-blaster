package qtech.bubbles.common.filefilters

import java.io.File
import java.io.FileFilter

class VisibilityFileFilter(private val filterVisible: Boolean) : FileFilter {
    override fun accept(pathname: File): Boolean {
        return if (filterVisible) {
            !pathname.isHidden
        } else {
            pathname.isHidden
        }
    }
}