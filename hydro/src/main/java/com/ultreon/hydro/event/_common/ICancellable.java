package com.ultreon.hydro.event._common;

public interface ICancellable {
    void cancel();

    boolean isCancelled();
}
