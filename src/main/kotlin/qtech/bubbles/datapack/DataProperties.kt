package qtech.bubbles.datapack

class DataProperties private constructor() {
    var id: String? = null
        private set

    class Builder {
        private var id: String? = null
        fun id(id: String?): Builder {
            this.id = id
            return this
        }

        fun build(): DataProperties {
            val properties = DataProperties()
            properties.id = id
            return properties
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}