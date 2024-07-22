package com.example.meditationapp2_0;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VideoLibraryScreen {

    private Stage primaryStage;
    private VideoLibraryManager videoLibraryManager;
    private SessionManager sessionManager;

    public VideoLibraryScreen(Stage primaryStage, VideoLibraryManager videoLibraryManager, SessionManager sessionManager) {
        this.primaryStage = primaryStage;
        this.videoLibraryManager = videoLibraryManager;
        this.sessionManager = sessionManager;
    }

    public void showVideoLibraryScreen() {
        VBox layout = new VBox(10);
        ListView<Video> videoListView = new ListView<>();
        videoListView.getItems().addAll(videoLibraryManager.getAllVideos());

        Button continueButton = new Button("Continue");
        continueButton.setDisable(true); // Initially disable the button

        videoListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                sessionManager.setSelectedPreSelectedVideo(newSelection);
                continueButton.setDisable(false); // Enable the button when a video is selected
            } else {
                continueButton.setDisable(true); // Disable the button if no video is selected
            }
        });

        continueButton.setOnAction(e -> sessionManager.playSelectedPreSelectedVideo());

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> primaryStage.setScene(MainApp.getStartupScene()));

        layout.getChildren().addAll(videoListView, continueButton, backButton);
        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
    }
}