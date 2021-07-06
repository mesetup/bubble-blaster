package qtech.bubbles.common

class LoggableProgress : Progress {
    private val infoTransporter: InfoTransporter

    constructor(infoTransporter: InfoTransporter, progress: Int, max: Int) : super(progress, max) {
        this.infoTransporter = infoTransporter
    }

    constructor(infoTransporter: InfoTransporter, max: Int) : super(max) {
        this.infoTransporter = infoTransporter
    }

    fun log(text: String) {
        infoTransporter.log(text)
    }
}