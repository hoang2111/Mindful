package com.example.meditationapp2_0;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomizationManager {
    private final SessionManager sessionManager;
    private final Stage primaryStage;

    public CustomizationManager(SessionManager sessionManager, Stage primaryStage) {
        this.sessionManager = sessionManager;
        this.primaryStage = primaryStage;
    }

    public void showCustomizationScreen() {
        VBox customizationLayout = new VBox(20);
        Label durationLabel = new Label("Session Duration (minutes):");
        TextField durationField = new TextField(String.valueOf(sessionManager.getSessionDuration()));
        Label reminderLabel = new Label("Reminder Message:");
        TextField reminderField = new TextField(sessionManager.getReminderMessage());
        Label intervalLabel = new Label("Interval Duration (minutes):");
        TextField intervalField = new TextField(String.valueOf(sessionManager.getIntervalDuration()));
        Button saveButton = new Button("Save");

        saveButton.setOnAction(e -> {
            sessionManager.setSessionDuration(Integer.parseInt(durationField.getText()));
            sessionManager.setReminderMessage(reminderField.getText());
            sessionManager.setIntervalDuration(Integer.parseInt(intervalField.getText()));
            primaryStage.setScene(MainApp.getStartupScene());
        });

        customizationLayout.getChildren().addAll(durationLabel, durationField, reminderLabel, reminderField, intervalLabel, intervalField, saveButton);
        Scene customizationScene = new Scene(customizationLayout, 800, 500);
        primaryStage.setScene(customizationScene);
    }
}
