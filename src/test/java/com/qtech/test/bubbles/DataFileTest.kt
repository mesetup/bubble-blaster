package com.qtech.test.bubbles

import com.qtech.bubbles.common.ResourceLocation
import java.io.*

object DataFileTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val file = File("test.qdat")
        val fout: FileOutputStream
        fout = try {
            FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            System.exit(1)
            return
        }
        val out = DataOutputStream(fout)
        val oos: ObjectOutputStream
        oos = try {
            ObjectOutputStream(out)
        } catch (e: IOException) {
            e.printStackTrace()
            System.exit(1)
            return
        }
        try {
            val key = ResourceLocation("bubbleblaster", "hello")
            println(key)
            oos.writeObject(key)
            try {
                oos.close()
                out.close()
                fout.close()
            } catch (ignored: IOException) {
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val fin: FileInputStream
        fin = try {
            FileInputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            System.exit(1)
            return
        }
        val `in` = DataInputStream(fin)
        val ois: ObjectInputStream
        ois = try {
            ObjectInputStream(`in`)
        } catch (e: IOException) {
            e.printStackTrace()
            System.exit(1)
            return
        }
        try {
            val key = ois.readObject() as ResourceLocation
            println(key)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}