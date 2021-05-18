package com.qtech.bubbles.media;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class AudioSlot {
    private final AudioClip clip;
    private final String name;
    private boolean stopped = false;

    public AudioSlot(File file) {
        this(file, "");
    }

    public AudioSlot(File file, String name) {
        clip = new AudioClip(file.toURI().toString());
        this.name = name;
    }

    public AudioSlot(URI uri) {
        this(uri, "");
    }

    public AudioSlot(URI uri, String name) {
        clip = new AudioClip(uri.toString());
        this.name = name;
    }

    public AudioSlot(URL url) throws URISyntaxException {
        this(url, "");
    }

    public AudioSlot(URL url, String name) throws URISyntaxException {
        clip = new AudioClip(url.toURI().toString());
        this.name = name;
    }

    public AudioClip getClip() {
        return clip;
    }

    public void play() {
        clip.play();
        stopped = false;
    }

    public void stop() {
        clip.stop();
        stopped = true;
    }

    public double getBalance() {
        return clip.getBalance();
    }

    public double getRate() {
        return clip.getRate();
    }

    public void setVolume(double v) {
        clip.setVolume(v);
    }

    public void setMute(double v) {
        clip.setBalance(v);
    }

    public double getVolume() {
        return clip.getVolume();
    }

    public String getSource() {
        return clip.getSource();
    }

    public boolean isStopped() {
        return stopped;
    }

    public boolean isPlaying() {
        return !stopped;
    }

    public String getName() {
        return name;
    }
}
