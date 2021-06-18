package com.qtech.bubbles.graphics

enum class Anchor(val x: Int, val y: Int) {
    TOP_LEFT(-1, -1), TOP(0, -1), TOP_RIGHT(+1, -1), LEFT(-1, 0), CENTER(0, 0), RIGHT(+1, 0), BOTTOM_LEFT(-1, 1), BOTTOM(0, +1), BOTTOM_RIGHT(+1, +1);

    val rotationDeg: Int
        get() {
            when (x) {
                -1 -> {
                    run {
                        when (y) {
                            -1 -> return -45
                            0 -> return 0
                            +1 -> return 45
                            else -> {}
                        }
                    }
                    run {
                        when (y) {
                            -1 -> return -90
                            0 -> return Float.NaN.toInt()
                            +1 -> return 90
                            else -> {}
                        }
                    }
                    run {
                        when (y) {
                            -1 -> return -135
                            0 -> return 180
                            +1 -> return 135
                            else -> {}
                        }
                    }
                }
                0 -> {
                    run {
                        when (y) {
                            -1 -> return -90
                            0 -> return Float.NaN.toInt()
                            +1 -> return 90
                            else -> {}
                        }
                    }
                    run {
                        when (y) {
                            -1 -> return -135
                            0 -> return 180
                            +1 -> return 135
                            else -> {}
                        }
                    }
                }
                +1 -> {
                    when (y) {
                        -1 -> return -135
                        0 -> return 180
                        +1 -> return 135
                    }
                }
            }
            if (x < -1) {
                throw IndexOutOfBoundsException("x < -1")
            }
            if (x > 1) {
                throw IndexOutOfBoundsException("x < 1")
            }
            if (y < -1) {
                throw IndexOutOfBoundsException("y < -1")
            }
            throw IndexOutOfBoundsException("y > 1")
        }
    val rotationRad: Double
        get() = Math.toRadians(rotationDeg.toDouble())

    fun mirror(): Anchor {
        when (x) {
            -1 -> {
                run {
                    when (y) {
                        -1 -> return BOTTOM_RIGHT
                        0 -> return BOTTOM
                        +1 -> return BOTTOM_LEFT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return RIGHT
                        0 -> return CENTER
                        +1 -> return LEFT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return TOP_RIGHT
                        0 -> return TOP
                        +1 -> return TOP_LEFT
                        else -> {}
                    }
                }
            }
            0 -> {
                run {
                    when (y) {
                        -1 -> return RIGHT
                        0 -> return CENTER
                        +1 -> return LEFT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return TOP_RIGHT
                        0 -> return TOP
                        +1 -> return TOP_LEFT
                        else -> {}
                    }
                }
            }
            +1 -> {
                when (y) {
                    -1 -> return TOP_RIGHT
                    0 -> return TOP
                    +1 -> return TOP_LEFT
                }
            }
        }
        if (x < -1) {
            throw IndexOutOfBoundsException("x < -1")
        }
        if (x > 1) {
            throw IndexOutOfBoundsException("x < 1")
        }
        if (y < -1) {
            throw IndexOutOfBoundsException("y < -1")
        }
        throw IndexOutOfBoundsException("y > 1")
    }

    fun rotateClockwise(): Anchor {
        when (x) {
            -1 -> {
                run {
                    when (y) {
                        -1 -> return TOP
                        0 -> return TOP_RIGHT
                        +1 -> return RIGHT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return TOP_LEFT
                        0 -> return CENTER
                        +1 -> return BOTTOM_RIGHT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return LEFT
                        0 -> return BOTTOM_LEFT
                        +1 -> return BOTTOM
                        else -> {}
                    }
                }
            }
            0 -> {
                run {
                    when (y) {
                        -1 -> return TOP_LEFT
                        0 -> return CENTER
                        +1 -> return BOTTOM_RIGHT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return LEFT
                        0 -> return BOTTOM_LEFT
                        +1 -> return BOTTOM
                        else -> {}
                    }
                }
            }
            +1 -> {
                when (y) {
                    -1 -> return LEFT
                    0 -> return BOTTOM_LEFT
                    +1 -> return BOTTOM
                }
            }
        }
        if (x < -1) {
            throw IndexOutOfBoundsException("x < -1")
        }
        if (x > 1) {
            throw IndexOutOfBoundsException("x < 1")
        }
        if (y < -1) {
            throw IndexOutOfBoundsException("y < -1")
        }
        throw IndexOutOfBoundsException("y > 1")
    }

    fun rotateBitClockwise(): Anchor {
        when (x) {
            -1 -> {
                run {
                    when (y) {
                        -1 -> return LEFT
                        0 -> return TOP_LEFT
                        +1 -> return TOP
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return BOTTOM_LEFT
                        0 -> return CENTER
                        +1 -> return TOP_RIGHT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return BOTTOM
                        0 -> return BOTTOM_RIGHT
                        +1 -> return RIGHT
                        else -> {}
                    }
                }
            }
            0 -> {
                run {
                    when (y) {
                        -1 -> return BOTTOM_LEFT
                        0 -> return CENTER
                        +1 -> return TOP_RIGHT
                        else -> {}
                    }
                }
                run {
                    when (y) {
                        -1 -> return BOTTOM
                        0 -> return BOTTOM_RIGHT
                        +1 -> return RIGHT
                        else -> {}
                    }
                }
            }
            +1 -> {
                when (y) {
                    -1 -> return BOTTOM
                    0 -> return BOTTOM_RIGHT
                    +1 -> return RIGHT
                }
            }
        }
        if (x < -1) {
            throw IndexOutOfBoundsException("x < -1")
        }
        if (x > 1) {
            throw IndexOutOfBoundsException("x < 1")
        }
        if (y < -1) {
            throw IndexOutOfBoundsException("y < -1")
        }
        throw IndexOutOfBoundsException("y > 1")
    }

    companion object {
        fun of(x: Int, y: Int): Anchor {
            when (x) {
                -1 -> {
                    run {
                        when (y) {
                            -1 -> return TOP_LEFT
                            0 -> return TOP
                            +1 -> return TOP_RIGHT
                            else -> {}
                        }
                    }
                    run {
                        when (y) {
                            -1 -> return LEFT
                            0 -> return CENTER
                            +1 -> return RIGHT
                            else -> {}
                        }
                    }
                    run {
                        when (y) {
                            -1 -> return BOTTOM_LEFT
                            0 -> return BOTTOM
                            +1 -> return BOTTOM_RIGHT
                            else -> {}
                        }
                    }
                }
                0 -> {
                    run {
                        when (y) {
                            -1 -> return LEFT
                            0 -> return CENTER
                            +1 -> return RIGHT
                            else -> {}
                        }
                    }
                    run {
                        when (y) {
                            -1 -> return BOTTOM_LEFT
                            0 -> return BOTTOM
                            +1 -> return BOTTOM_RIGHT
                            else -> {}
                        }
                    }
                }
                +1 -> {
                    when (y) {
                        -1 -> return BOTTOM_LEFT
                        0 -> return BOTTOM
                        +1 -> return BOTTOM_RIGHT
                    }
                }
            }
            if (x < -1) {
                throw IndexOutOfBoundsException("x < -1")
            }
            if (x > 1) {
                throw IndexOutOfBoundsException("x < 1")
            }
            if (y < -1) {
                throw IndexOutOfBoundsException("y < -1")
            }
            throw IndexOutOfBoundsException("y > 1")
        }

        fun of(degrees: Int): Anchor {
            val i = degrees % 360
            return when (i) {
                0 -> TOP
                45 -> TOP_RIGHT
                90 -> RIGHT
                135 -> BOTTOM_RIGHT
                180 -> BOTTOM
                225 -> BOTTOM_LEFT
                270 -> LEFT
                315 -> TOP_LEFT
                else -> throw IllegalArgumentException("degrees is not a multiply of 45")
            }
        }

        fun of(theta: Double): Anchor {
            val i = theta % 1
            if (i == .0) {
                return TOP
            } else if (i == .125) {
                return TOP_RIGHT
            } else if (i == .25) {
                return RIGHT
            } else if (i == .375) {
                return BOTTOM_RIGHT
            } else if (i == .5) {
                return BOTTOM
            } else if (i == .625) {
                return BOTTOM_LEFT
            } else if (i == .75) {
                return LEFT
            } else if (i == .875) {
                return TOP_LEFT
            }
            throw IllegalArgumentException("theta is not a multiply of 0.125 (theta÷8)")
        }
    }
}