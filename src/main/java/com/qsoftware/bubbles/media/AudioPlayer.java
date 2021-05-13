package com.qsoftware.bubbles.media;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class AudioPlayer {
    private int maximal;
    private final ArrayList<AudioSlot> slots;

    public AudioPlayer(int maximal) {
        this.maximal = maximal;
        slots = new ArrayList<>();
    }

    public void add(AudioSlot slot) {
        if (slots.size() >= maximal) return;

        slots.add(slot);

        if (!slot.isPlaying()) slot.play();
    }

    public void addIf(AudioSlot slot, Predicate<AudioSlot> predicate) {
        if (slots.size() >= maximal) return;

        boolean flag = false;
        for (AudioSlot slot1 : slots) {
            boolean flag1 = predicate.test(slot1);
            if (flag1) {
                flag = true;
            }
        }

        if (!flag) return;

        slots.add(slot);

        if (!slot.isPlaying()) slot.play();
    }

    public boolean removeIf(Predicate<AudioSlot> filter) {
        return slots.removeIf(filter);
    }

    public boolean removeStopped() {
        return removeIf(AudioSlot::isStopped);
    }

    public boolean removePlaying() {
        return removeIf(AudioSlot::isPlaying);
    }

    public boolean remove(AudioSlot slot) {
        return slots.remove(slot);
    }

    public AudioSlot remove(int index) {
        return slots.remove(index);
    }

    public AudioSlot get(int index) {
        return slots.get(index);
    }

    public void stopAll() {
        for (AudioSlot slot : slots) {
            slot.stop();
        }
    }

    public void playAll() {
        for (AudioSlot slot : slots) {
            slot.play();
        }
    }

    public boolean isSilent() {
        for (AudioSlot slot : slots) {
            if (slot.isPlaying()) return false;
        }
        return true;
    }

    public void clear() {
        for (AudioSlot slot : slots) {
            slot.stop();
        }
        slots.clear();
    }

    public int getMaximal() {
        return maximal;
    }

    public void setMaximal(int maximal) {
        this.maximal = maximal;
    }

    public List<AudioSlot> getSlots() {
        return slots;
    }
}
