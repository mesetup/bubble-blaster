package com.qtech.bubbles.common.interfaces

interface Listener {
    //    boolean eventsActive = false;
    fun bindEvents()
    fun unbindEvents()
    fun eventsAreBound(): Boolean
}