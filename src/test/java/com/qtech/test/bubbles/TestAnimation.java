package com.qtech.test.bubbles;

import com.qtech.bubbles.graphics.Animation;

public class TestAnimation {
    public static void main(String[] args) {
        Animation animation = new Animation(100, 200, 30d);
        animation.start();

        //noinspection InfiniteLoopStatement
        while (true) {
            animation.animate();
            try {
                //noinspection BusyWait
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
