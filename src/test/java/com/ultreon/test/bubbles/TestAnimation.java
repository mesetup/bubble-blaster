package com.ultreon.test.bubbles;

import com.ultreon.hydro.render.ValueAnimator;

public class TestAnimation {
    public static void main(String[] args) {
        ValueAnimator valueAnimator = new ValueAnimator(100, 200, 30d);
        valueAnimator.start();

        //noinspection InfiniteLoopStatement
        while (true) {
            valueAnimator.animate();
            try {
                //noinspection BusyWait
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
