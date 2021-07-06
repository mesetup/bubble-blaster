package qtech.bubbles

class BBFlags(private val set: Set<String>) {
    constructor() : this(HashSet())

    fun get(name: String): Boolean {
        return set.contains(name)
    }

    companion object {
        fun parse(code: String): BBFlags {
            return BBFlags(HashSet(code.split(Regex(";"))))
        }
    }
}
