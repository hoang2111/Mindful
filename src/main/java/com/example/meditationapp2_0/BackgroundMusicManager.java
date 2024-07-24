package com.example.meditationapp2_0;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BackgroundMusicManager {
    private MediaPlayer mediaPlayer;

    public void setBackgroundMusic(String musicFileUri) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(musicFileUri);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setVolume(0.3); // Set volume to 50%
    }

    public void playBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}