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
import javafx.scene.control.Slider;
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
import java.io.FileWriter;
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
    private String reminderMessage = "Go outside and drink water. You need it man.";
    private int intervalDuration = 30; // default interval in minutes
    private HBox buttonLayout;

    private Button uploadButton;
    private Button playButton;
    private Button stopButton;
    private Button backButton;
    private Button selectPreSelectedButton;

    public SessionManager(Stage primaryStage, MediaManager mediaManager, StreakManager streakManager, MainApp mainApp, VideoLibraryManager videoLibraryManager) {
        this.primaryStage = primaryStage;
        this.mediaManager = mediaManager;
        this.streakManager = streakManager;
        this.mainApp = mainApp;
        this.videoLibraryManager = videoLibraryManager;
        initializeButtons();
    }

    private void initializeButtons() {
        uploadButton = new Button("Upload Media");
        playButton = new Button("Play");
        stopButton = new Button("Stop");
        backButton = new Button("Back");
        selectPreSelectedButton = new Button("Select Pre-Selected Sources");
    }

    public void startSession(String sessionType) {
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Label sessionLabel = new Label("Session Type: " + sessionType);

        uploadButton.setOnAction(e -> {
            File selectedFile = mediaManager.uploadMediaWithFileChooser();
            if (selectedFile != null) {
                String videoTitle = selectedFile.getName(); // Or prompt for title if desired
                VideoTheme videoTheme = sessionType.equalsIgnoreCase("Meditation") ? VideoTheme.SLEEP : VideoTheme.EYE_EXERCISE;
                Video newVideo = new Video(videoTitle, selectedFile.getAbsolutePath(), videoTheme);
                videoLibraryManager.addUserUploadedVideo(newVideo);
                mediaManager.playMedia();
                startMeditationTimer();
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

        buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(uploadButton, selectPreSelectedButton, playButton, stopButton, backButton);
        buttonLayout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        buttonLayout.setPadding(new Insets(10));

        layout.getChildren().addAll(sessionLabel, mediaManager.getMediaView(), customizeSlider(mediaManager.getSlider()), buttonLayout);

        Scene sessionScene = new Scene(layout, 800, 600);
        primaryStage.setScene(sessionScene);
    }

    private Slider customizeSlider(Slider slider) {
        slider.setStyle("-fx-control-inner-background: #454545;"); // Set your desired color here
        return slider;
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
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        Label messageLabel = new Label(reminderMessage);
        Button continueButton = new Button("Continue");

        continueButton.setOnAction(e -> {
            // Disable all buttons except stop and customize sound
            mainApp.disableAllButtonsExceptSoundAndStop();
            // Return to startup scene and start interval timer
            startIntervalTimer();
            primaryStage.setScene(MainApp.getStartupScene());
        });

        layout.getChildren().addAll(messageLabel, continueButton);
        Scene reminderScene = new Scene(layout, 300, 200);
        primaryStage.setScene(reminderScene);
    }

    private void disableAllButtonsExceptStop() {
        for (var node : buttonLayout.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                if (!button.getText().equalsIgnoreCase("Stop")) {
                    button.setDisable(true);
                }
            }
        }
    }

    private void enableAllButtons() {
        for (var node : buttonLayout.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setDisable(false);
            }
        }
    }

    private void displayNotification(String message) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac")) {
            displayMacNotification(message);
        } else if (os.contains("win")) {
            displayWindowsNotification(message);
        } else {
            System.out.println("Notifications are not supported on this OS.");
        }
    }

    private void displayMacNotification(String message) {
        String osascriptCommand = "osascript -e 'display notification \"" + message + "\" with title \"Meditation Reminder\"'";
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", osascriptCommand);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayWindowsNotification(String message) {
        String vbsScript =
                "Dim MsgBox\n" +
                        "Set MsgBox = CreateObject(\"WScript.Shell\")\n" +
                        "MsgBox.Popup \"" + message + "\", 10, \"Meditation Reminder\", 64";
        try {
            File tempScript = File.createTempFile("notify", ".vbs");
            try (FileWriter writer = new FileWriter(tempScript)) {
                writer.write(vbsScript);
            }
            ProcessBuilder processBuilder = new ProcessBuilder("cscript", tempScript.getAbsolutePath());
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startIntervalTimer() {
        PauseTransition pause = new PauseTransition(Duration.minutes(intervalDuration));
        pause.setOnFinished(e -> {
            primaryStage.setScene(MainApp.getStartupScene());
            mainApp.enableAllButtonsExceptSoundAndStop();
            displayNotification(reminderMessage);
            // Enable all buttons when the next session comes
            enableAllButtons();
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

    public void setSelectedMediaFile(File mediaFile) {
        this.selectedMediaFile = mediaFile;
    }

    public void setSessionDuration(int duration) {
        this.sessionDuration = duration;
    }

    public void setReminderMessage(String message) {
        this.reminderMessage = message;
    }

    public void setIntervalDuration(int duration) {
        this.intervalDuration = duration;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public int getIntervalDuration() {
        return intervalDuration;
    }
}