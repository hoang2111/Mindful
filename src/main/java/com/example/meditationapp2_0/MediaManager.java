package com.example.meditationapp2_0;

import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MediaManager {
    private MediaPlayer mediaPlayer;
    private final MediaView mediaView;
    private final Stage primaryStage;
    private final Slider slider;

    public MediaManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mediaView = new MediaView();
        mediaView.setPreserveRatio(true);
        mediaView.fitWidthProperty().bind(primaryStage.widthProperty());
        mediaView.fitHeightProperty().bind(primaryStage.heightProperty().subtract(140)); // Subtract height to avoid overshadowing buttons

        slider = new Slider();
        slider.setDisable(true);
        slider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (slider.isValueChanging() && mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(newValue.doubleValue() / 100));
            }
        });
    }

    public MediaView getMediaView() {
        return mediaView;
    }

    public Slider getSlider() {
        return slider;
    }

    public void uploadMedia(File mediaFile) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        Media media = new Media(mediaFile.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        initializeMediaPlayer();
    }

    public void uploadMedia(String mediaUrl) {
        if (mediaPlayer != null) {
            mediaPlayer.dispose();
        }
        Media media = new Media(mediaUrl);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        initializeMediaPlayer();
    }

    public void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void restartMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
        }
    }

    public void setBackgroundMusic(String musicFileUri) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        Media media = new Media(musicFileUri);
        mediaPlayer = new MediaPlayer(media);
    }

    public void playBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public File uploadMediaWithFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp4", "*.mp3"));
        File mediaFile = fileChooser.showOpenDialog(primaryStage);
        if (mediaFile != null) {
            uploadMedia(mediaFile);
            return mediaFile;
        }
        return null;
    }

    private void initializeMediaPlayer() {
        slider.setDisable(false);
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            if (!slider.isValueChanging()) {
                slider.setValue(newTime.toMillis() / mediaPlayer.getTotalDuration().toMillis() * 100);
            }
        });
        mediaPlayer.setOnReady(() -> slider.setValue(0));
        mediaPlayer.setOnEndOfMedia(() -> slider.setValue(100));
    }
}