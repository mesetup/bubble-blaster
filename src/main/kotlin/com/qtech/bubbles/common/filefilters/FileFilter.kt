package com.qtech.bubbles.common.filefilters

import java.io.File
import java.io.FileFilter

class FileFilter : FileFilter {
    override fun accept(pathname: File): Boolean {
        return pathname.isFile
    }
}