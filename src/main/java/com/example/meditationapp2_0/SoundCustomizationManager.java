package com.example.meditationapp2_0;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class SoundCustomizationManager {
    private Stage primaryStage;
    private BackgroundMusicManager backgroundMusicManager;
    private File selectedSoundFile;

    // Pre-selected sounds
    private ObservableList<String> preSelectedSounds = FXCollections.observableArrayList(
            "White Noise", "Birds Chirping", "Rainfall", "Soft Piano", "Forest Ambience"
    );

    public SoundCustomizationManager(Stage primaryStage, BackgroundMusicManager backgroundMusicManager) {
        this.primaryStage = primaryStage;
        this.backgroundMusicManager = backgroundMusicManager;
    }

    public void showSoundCustomizationScreen() {
        VBox layout = new VBox(10);
        Button chooseSoundButton = new Button("Choose Sound");
        Label chosenSoundLabel = new Label("No sound selected");
        Button preSelectedSoundButton = new Button("Choose Pre-Selected Sound");
        ChoiceBox<String> preSelectedSoundChoiceBox = new ChoiceBox<>(preSelectedSounds);
        preSelectedSoundChoiceBox.setValue("Select Pre-Selected Sound");
        Button backButton = new Button("Back");

        chooseSoundButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
            selectedSoundFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedSoundFile != null) {
                chosenSoundLabel.setText("Selected Sound: " + selectedSoundFile.getName());
                backgroundMusicManager.setBackgroundMusic(selectedSoundFile.toURI().toString());
                backgroundMusicManager.playBackgroundMusic();
            }
        });

        preSelectedSoundButton.setOnAction(e -> {
            String selectedSound = preSelectedSoundChoiceBox.getValue();
            if (selectedSound != null && !selectedSound.equals("Select Pre-Selected Sound")) {
                chosenSoundLabel.setText("Selected Sound: " + selectedSound);
                String soundUrl = getPreSelectedSoundUrl(selectedSound);
                if (soundUrl != null) {
                    backgroundMusicManager.setBackgroundMusic(soundUrl);
                    backgroundMusicManager.playBackgroundMusic();
                }
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getStartupScene()));

        layout.getChildren().addAll(chooseSoundButton, chosenSoundLabel, preSelectedSoundChoiceBox, preSelectedSoundButton, backButton);

        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
    }

    private String getPreSelectedSoundUrl(String soundName) {
        switch (soundName) {
            case "White Noise":
                return "https://ia801909.us.archive.org/26/items/y-2mate.com-white-noise-black-screen-no-ads-1-hour/y2mate.com%20-%20White%20Noise%20%20Black%20Screen%20%20No%20ads%20%201%20hour.mp3";
            case "Birds Chirping":
                return "https://ia800600.us.archive.org/25/items/y-2mate.com-bird-sounds-spectacular-morning-bird-sound/y2mate.com%20-%20Bird%20Sounds%20Spectacular%20%20Morning%20Bird%20Sound.mp3";
            case "Rainfall":
                return "https://ia801907.us.archive.org/8/items/y-2mate.com-nature-sounds-rain-sounds-one-hour-for-sleeping-sleep-aid-for-everybody/y2mate.com%20-%20Nature%20Sounds%20Rain%20Sounds%20One%20Hour%20for%20Sleeping%20Sleep%20Aid%20for%20Everybody.mp3";
            case "Soft Piano":
                return "https://ia600601.us.archive.org/2/items/y-2mate.com-classical-music-for-studying-working-focusing-concentrating-1-hour/y2mate.com%20-%20Classical%20Music%20for%20Studying%20%20Working%20%C2%A0Focusing%20%20Concentrating%20%C2%A01%20Hour.mp3";
            case "Forest Ambience":
                return "https://ia904506.us.archive.org/19/items/y-2mate.com-3-d-asmr-amazing-forest-sounds-binaural-asmr-1-hour-relaxing-studying/y2mate.com%20-%203D%20ASMR%20Amazing%20Forest%20Sounds%20Binaural%20ASMR%20%201%20Hour%20%20%20Relaxing%20%20Studying.mp3";
            default:
                return null;
        }
    }
}