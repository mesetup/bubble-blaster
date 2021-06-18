package com.qtech.bubbles.common.command

interface TabHandler {
    fun tabComplete(args: Array<String>): List<String>?
}