package com.example.meditationapp2_0;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class SessionManager {

    private Stage primaryStage;
    private MediaManager mediaManager;
    private StreakManager streakManager;
    private MainApp mainApp;
    private VideoLibraryManager videoLibraryManager;
    private File selectedMediaFile;
    private Video selectedPreSelectedVideo;
    private int sessionDuration = 5; // default duration in minutes
    private String reminderMessage = "Time for your meditation!";
    private int intervalDuration = 30; // default interval in minutes

    public SessionManager(Stage primaryStage, MediaManager mediaManager, StreakManager streakManager, MainApp mainApp, VideoLibraryManager videoLibraryManager) {
        this.primaryStage = primaryStage;
        this.mediaManager = mediaManager;
        this.streakManager = streakManager;
        this.mainApp = mainApp;
        this.videoLibraryManager = videoLibraryManager;
    }

    public void startSession(String sessionType) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Button uploadButton = new Button("Upload Media");
        Button playButton = new Button("Play");
        Button stopButton = new Button("Stop");
        Button backButton = new Button("Back");
        Button selectPreSelectedButton = new Button("Select Pre-Selected Sources");
        Label sessionLabel = new Label("Session Type: " + sessionType);

        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Media Files", "*.mp4"));
            selectedMediaFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedMediaFile != null) {
                mediaManager.uploadMedia(selectedMediaFile);
            }
        });

        playButton.setOnAction(e -> {
            mediaManager.playMedia();
            startMeditationTimer();
        });

        stopButton.setOnAction(e -> mediaManager.stopMedia());
        backButton.setOnAction(e -> {
            mediaManager.stopMedia(); // Stop any playing media
            primaryStage.setScene(MainApp.getStartupScene());
        });
        selectPreSelectedButton.setOnAction(e -> showPreSelectedSources(sessionType));

        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(uploadButton, selectPreSelectedButton, playButton, stopButton, backButton);
        buttonLayout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        buttonLayout.setPadding(new Insets(10));

        layout.getChildren().addAll(sessionLabel, mediaManager.getMediaView(), buttonLayout);

        Scene sessionScene = new Scene(layout, 800, 500);
        primaryStage.setScene(sessionScene);
    }

    private void startMeditationTimer() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(sessionDuration), e -> {
            mediaManager.stopMedia();
            showReminderMessage();
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void showReminderMessage() {
        // Show the reminder message scene
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Label messageLabel = new Label(reminderMessage);
        Button continueButton = new Button("Continue");

        continueButton.setOnAction(e -> {
            // Return to startup scene after the interval timer
            startIntervalTimer();
        });

        layout.getChildren().addAll(messageLabel, continueButton);
        Scene reminderScene = new Scene(layout, 300, 200);
        primaryStage.setScene(reminderScene);
    }

    private void displayNotification(String message) {
        String osascriptCommand = "osascript -e 'display notification \"" + message + "\" with title \"Meditation Reminder\"'";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", osascriptCommand);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startIntervalTimer() {
        PauseTransition pause = new PauseTransition(Duration.minutes(intervalDuration));
        pause.setOnFinished(e -> {
            primaryStage.setScene(MainApp.getStartupScene());
            displayNotification(reminderMessage);
            PauseTransition notificationPause = new PauseTransition(Duration.seconds(20));
            notificationPause.setOnFinished(event -> displayNotification(reminderMessage));
            notificationPause.play();
        });
        pause.play();
    }

    private void showPreSelectedSources(String sessionType) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Label sessionLabel = new Label("Select Pre-Selected " + sessionType + " Videos");

        ListView<Video> preSelectedListView = new ListView<>();

        // Filter videos based on session type
        VideoTheme theme;
        if (sessionType.equals("Meditation")) {
            theme = VideoTheme.SLEEP;
        } else if (sessionType.equals("Eye Exercise")) {
            theme = VideoTheme.EYE_EXERCISE;
        } else {
            theme = VideoTheme.GENERAL;
        }
        preSelectedListView.getItems().addAll(videoLibraryManager.getVideosByTheme(theme));

        Button continueButton = new Button("Continue");
        continueButton.setDisable(true); // Initially disable the button

        preSelectedListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPreSelectedVideo = newSelection;
                continueButton.setDisable(false); // Enable the button when a video is selected
            } else {
                continueButton.setDisable(true); // Disable the button if no video is selected
            }
        });

        continueButton.setOnAction(e -> playSelectedPreSelectedVideo());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> startSession(sessionType)); // Go back to the session screen

        layout.getChildren().addAll(sessionLabel, preSelectedListView, continueButton, backButton);
        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
    }

    public void playSelectedPreSelectedVideo() {
        if (selectedPreSelectedVideo != null) {
            String filePathOrUrl = selectedPreSelectedVideo.getFilePathOrUrl();
            if (filePathOrUrl.startsWith("http") || filePathOrUrl.startsWith("https")) {
                mediaManager.uploadMedia(filePathOrUrl);
            } else {
                mediaManager.uploadMedia(new File(filePathOrUrl));
            }
            mediaManager.playMedia();
            startMeditationTimer(); // Start the timer
        }
    }

    public void setSelectedPreSelectedVideo(Video video) {
        this.selectedPreSelectedVideo = video;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public void setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }

    public int getIntervalDuration() {
        return intervalDuration;
    }

    public void setIntervalDuration(int intervalDuration) {
        this.intervalDuration = intervalDuration;
    }

}