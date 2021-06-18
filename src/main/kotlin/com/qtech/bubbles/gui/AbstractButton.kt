package com.qtech.bubbles.gui

abstract class AbstractButton : Widget() {
    abstract var command: () -> Unit
}