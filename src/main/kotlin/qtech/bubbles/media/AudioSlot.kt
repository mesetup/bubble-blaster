package qtech.bubbles.media

import javafx.scene.media.AudioClip
import java.io.File
import java.net.URI
import java.net.URL

class AudioSlot {
    val clip: AudioClip
    val name: String
    var isStopped = false
        private set

    constructor(file: File, name: String = "") {
        clip = AudioClip(file.toURI().toString())
        this.name = name
    }

    constructor(uri: URI, name: String = "") {
        clip = AudioClip(uri.toString())
        this.name = name
    }

    constructor(url: URL, name: String = "") {
        clip = AudioClip(url.toURI().toString())
        this.name = name
    }

    fun play() {
        clip.play()
        isStopped = false
    }

    fun stop() {
        clip.stop()
        isStopped = true
    }

    val balance: Double
        get() = clip.balance
    val rate: Double
        get() = clip.rate

    fun setMute(v: Double) {
        clip.balance = v
    }

    var volume: Double
        get() = clip.volume
        set(v) {
            clip.volume = v
        }
    val source: String
        get() = clip.source
    val isPlaying: Boolean
        get() = !isStopped
}