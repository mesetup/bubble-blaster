package com.qtech.bubbles.common.streams

import java.io.OutputStream
import java.io.PrintStream

class LoggingPrintStream(out: OutputStream) : PrintStream(out)