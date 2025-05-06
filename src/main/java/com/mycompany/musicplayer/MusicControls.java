package com.mycompany.musicplayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicControls {
    private Clip clip;
    private AudioInputStream audioStream;
    private boolean isPlaying;
    private boolean isPaused;
    private float currentVolume;
    private long clipPosition;
    private FloatControl gainControl;
    private boolean isRepeating;
    private List<Runnable> songEndListeners = new ArrayList<>();

    public MusicControls() {
        this.isPlaying = false;
        this.isPaused = false;
        this.currentVolume = 0.5f;
        this.isRepeating = false;
    }

    public void loadSong(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (clip != null) {
            clip.close();
        }
        audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        
        gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        setVolume(currentVolume);
        
        // Add line listener for song end detection
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP && isPlaying && !isPaused) {
                for (Runnable listener : songEndListeners) {
                    listener.run();
                }
            }
        });
    }

    public synchronized void play() {
        if (clip != null && !isPlaying) {
            if (isPaused) {
                clip.setMicrosecondPosition(clipPosition);
            }
            clip.start();
            isPlaying = true;
            isPaused = false;
        }
    }

    public synchronized void pause() {
        if (clip != null && isPlaying) {
            clipPosition = clip.getMicrosecondPosition();
            clip.stop();
            isPlaying = false;
            isPaused = true;
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.setMicrosecondPosition(0);
            isPlaying = false;
            isPaused = false;
        }
    }

    public void setVolume(float volume) {
        if (clip != null && gainControl != null) {
            currentVolume = volume;
            float min = gainControl.getMinimum();
            float max = gainControl.getMaximum();
            float range = max - min;
            float gain = min + (range * volume);
            gainControl.setValue(gain);
        }
    }

    public void seek(long position) {
        if (clip != null) {
            clip.setMicrosecondPosition(position);
        }
    }

    public long getCurrentPosition() {
        return clip != null ? clip.getMicrosecondPosition() : 0;
    }

    public long getTotalDuration() {
        return clip != null ? clip.getMicrosecondLength() : 0;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public float getCurrentVolume() {
        return currentVolume;
    }

    public void cleanup() {
        if (clip != null) {
            clip.close();
        }
        if (audioStream != null) {
            try {
                audioStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRepeating(boolean repeating) {
        this.isRepeating = repeating;
    }

    public boolean isRepeating() {
        return isRepeating;
    }

    public void addSongEndListener(Runnable listener) {
        songEndListeners.add(listener);
    }
}