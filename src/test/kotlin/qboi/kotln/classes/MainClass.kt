package qboi.kotln.classes

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

class MainClass {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val kClass: KClass<VictimClass> = VictimClass::class
            val cns: VictimClass = kClass.primaryConstructor!!.call()

            for (member in kClass.members) {
//                println(member::class.qualifiedName)
                if (member is KFunction) {
//                    println(member.name)
                    if (member.name.startsWith("print")) {
                        member.call(cns, "Qboi")
                    }
                }
            }
        }
    }
}