package qtech.bubbles.common.command.tabcomplete

import qtech.bubbles.common.ResourceLocation

class TabCompleter private constructor() {
    companion object {
        fun getStrings(arg: String, vararg strings: String): List<String> {
            val list = ArrayList<String>()
            for (str in strings) {
                addIfStartsWith(list, "$arg ", str)
            }
            return list
        }

        fun getInts(arg: String): List<String> {
            val list = ArrayList<String>()
            for (i in 0..9) {
                list.add(arg + i)
            }
            return list
        }

        fun getDecimals(arg: String): List<String> {
            val list = ArrayList<String>()
            list.add("$arg.")
            for (i in 0..9) {
                list.add(arg + i)
            }
            return list
        }

        private fun addIfStartsWith(list: MutableList<String>, arg: String, startWith: String): List<String> {
            if (arg.startsWith(startWith)) {
                list.add(arg)
            }
            return list
        }

        private fun addIfStartsWith(list: MutableList<String>, arg: ResourceLocation, startWith: String): List<String> {
            if (arg.path.startsWith(startWith)) {
                list.add(arg.toString())
            } else if (arg.toString().startsWith(startWith)) {
                list.add(arg.toString())
            }
            return list
        }
    }

    init {
        throw UnsupportedOperationException("Not allowed to initialize TabCompleter.")
    }
}