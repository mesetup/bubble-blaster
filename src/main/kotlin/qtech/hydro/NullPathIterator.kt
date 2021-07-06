package qtech.hydro


import java.awt.geom.PathIterator

class NullPathIterator : PathIterator {
    override fun getWindingRule(): Int {
        return 0
    }

    override fun isDone(): Boolean {
        return true
    }

    override fun next() {}
    override fun currentSegment(floats: FloatArray): Int {
        return PathIterator.SEG_CLOSE
    }

    override fun currentSegment(doubles: DoubleArray): Int {
        return PathIterator.SEG_CLOSE
    }
}