package com.qtech.bubbleblaster.event;

public interface ICancellable {
    void setCancelled();

    boolean isCancelled();
}
