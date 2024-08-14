package com.example.meditationapp2_0;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApp extends Application {

    private SessionManager sessionManager;
    private CustomizationManager customizationManager;
    private ThemeCustomizationManager themeCustomizationManager;
    private SoundCustomizationManager soundCustomizationManager;
    private VideoLibraryScreen videoLibraryScreen;
    private static Scene startupScene;
    private StreakManager streakManager;
    private Credits credits;

    private Button meditateButton;
    private Button eyeExerciseButton;
    private Button customizeButton;
    private Button lightExerciseButton;
    private Button soundCustomizeButton;
    private Button videoLibraryButton;
    private Button creditsButton;
    private Button stopButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mindful");

        streakManager = new StreakManager();
        streakManager.login(); // Log the user in when the app starts

        MediaManager mediaManager = new MediaManager(primaryStage);
        BackgroundMusicManager backgroundMusicManager = new BackgroundMusicManager();
        VideoLibraryManager videoLibraryManager = new VideoLibraryManager();

        sessionManager = new SessionManager(primaryStage, mediaManager, streakManager, this, videoLibraryManager);
        customizationManager = new CustomizationManager(sessionManager, primaryStage);
        themeCustomizationManager = new ThemeCustomizationManager(primaryStage, createStartupScene(primaryStage));
        soundCustomizationManager = new SoundCustomizationManager(primaryStage, backgroundMusicManager);
        videoLibraryScreen = new VideoLibraryScreen(primaryStage, videoLibraryManager, sessionManager);
        credits = new Credits(primaryStage);

        startupScene = createStartupScene(primaryStage);
        primaryStage.setScene(startupScene);
        primaryStage.show();
    }

    private Scene createStartupScene(Stage primaryStage) {
        VBox startupLayout = new VBox(10);
        startupLayout.setAlignment(Pos.CENTER);

        meditateButton = new Button("Meditate");
        eyeExerciseButton = new Button("Eye Exercise");
        customizeButton = new Button("Settings");
        lightExerciseButton = new Button("Light Exercise");
        soundCustomizeButton = new Button("Customize Sound");
        videoLibraryButton = new Button("Video Library");
        creditsButton = new Button("Credits");
        stopButton = new Button("Stop");

        meditateButton.setOnAction(_ -> sessionManager.startSession("Meditation"));
        lightExerciseButton.setOnAction(_ -> sessionManager.startSession("Light Exercise"));
        eyeExerciseButton.setOnAction(_ -> sessionManager.startSession("Eye Exercise"));
        customizeButton.setOnAction(_ -> customizationManager.showCustomizationScreen());
        soundCustomizeButton.setOnAction(_ -> soundCustomizationManager.showSoundCustomizationScreen());
        videoLibraryButton.setOnAction(e -> videoLibraryScreen.showVideoLibraryScreen());
        creditsButton.setOnAction(_ -> credits.showCreditsScreen());
        stopButton.setOnAction(_ -> Platform.exit()); // Properly exit the application

        HBox buttonLayout = new HBox(10);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(meditateButton, eyeExerciseButton, lightExerciseButton, customizeButton, soundCustomizeButton, videoLibraryButton, creditsButton, stopButton);
        buttonLayout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        buttonLayout.setPadding(new Insets(10));
        startupLayout.getChildren().add(buttonLayout);

        // Display current streak
        VBox streakLayout = new VBox(10);
        streakLayout.setAlignment(Pos.CENTER);
        streakLayout.setPadding(new Insets(10));
        streakLayout.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))));
        streakLayout.getChildren().add(new Label("Current Streak: " + streakManager.getCurrentStreak() + " days"));

        startupLayout.getChildren().add(streakLayout);

        return new Scene(startupLayout, 800, 500);
    }

    public static Scene getStartupScene() {
        return startupScene;
    }

    public void disableAllButtonsExceptSoundAndStop() {
        meditateButton.setDisable(true);
        eyeExerciseButton.setDisable(true);
        customizeButton.setDisable(true);
        lightExerciseButton.setDisable(true);
        videoLibraryButton.setDisable(true);
        creditsButton.setDisable(true);
    }
    public void enableAllButtonsExceptSoundAndStop() {
        meditateButton.setDisable(false);
        eyeExerciseButton.setDisable(false);
        customizeButton.setDisable(false);
        lightExerciseButton.setDisable(false);
        videoLibraryButton.setDisable(false);
        creditsButton.setDisable(false);
    }
}