package com.example.meditationapp2_0;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class ThemeCustomizationManager {

    private Stage primaryStage;
    private Scene startupScene;
    private File selectedBackgroundFile;

    public ThemeCustomizationManager(Stage primaryStage, Scene startupScene) {
        this.primaryStage = primaryStage;
        this.startupScene = startupScene;
    }

    public void showThemeCustomizationScreen() {
        VBox layout = new VBox(10);
        Button chooseBackgroundButton = new Button("Choose Background");
        Label chosenBackgroundLabel = new Label("No background selected");
        Button backButton = new Button("Back");

        chooseBackgroundButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            selectedBackgroundFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedBackgroundFile != null) {
                chosenBackgroundLabel.setText("Selected Background: " + selectedBackgroundFile.getName());
                setBackgroundImage(selectedBackgroundFile);
            }
        });

        backButton.setOnAction(e -> primaryStage.setScene(startupScene));

        layout.getChildren().addAll(chooseBackgroundButton, chosenBackgroundLabel, backButton);

        Scene themeCustomizationScene = new Scene(layout, 800, 500);
        primaryStage.setScene(themeCustomizationScene);
    }

    private void setBackgroundImage(File backgroundFile) {
        try {
            BackgroundImage backgroundImage = new BackgroundImage(new javafx.scene.image.Image(backgroundFile.toURI().toString()),
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
            VBox root = (VBox) startupScene.getRoot();
            root.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.out.println("Error setting background image: " + backgroundFile.getName());
        }
    }
}