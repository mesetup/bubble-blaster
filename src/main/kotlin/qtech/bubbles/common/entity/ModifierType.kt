package qtech.bubbles.common.entity

class ModifierType(name: String) {
    val name: String

    companion object {
        val types = HashMap<String, ModifierType>()
    }

    init {
        require(!types.containsKey(name)) { "Duplicate modifier detected!" }
        types[name] = this
        this.name = name
    }
}