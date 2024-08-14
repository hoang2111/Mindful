package com.example.meditationapp2_0;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Credits {

    private final Stage primaryStage;

    public Credits(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showCreditsScreen() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);
        layout.setFillWidth(true);

        Text creditsText = new Text("""
                Hello user,

                First of all, me, the creator of Mindful, really appreciates you installing this app! This is my first application ever, and I’m ecstatic that it reaches you!

                My intention behind creating Mindful is to aid you all with concentration and vision problems. We live in a hyper-demanding society, with no shortage of deadlines and work. Since COVID, a major percentage of that work is on computers. Many people, including myself, stare into screens for hours, nonstop, without a single break! That is concerning for two reasons: Firstly, we humans lack the capability to remain focused for long time periods: the longer we work, the lower our attention spans, thus our work’s quality is diminished. Secondly, staring at digital screens for a long time can lead to vision problems such as eye strain, myopia (cận thị) or astigmatism (loạn thị).

                Mindful subscribes to the idea that a human’s attention span should be divided into chunks instead. By using Mindful, users will work for a customized amount of time; afterwards, the app will ask the user to meditate or do eye yoga for a customized amount of time to de-stress their eyes. Finally the app will notify users to drink water or look outside, reminding them to be aware of their health. The app runs internally, and users will be notified when their next session comes. Users can also use pre-selected background sounds by pressing the Customize Sound button in the startup of the app.\s

                Consider using Mindful like a challenge: try to dedicate time, even if just a few minutes, for your mindfulness. We are gifted with 24 hours a day, so our time management skills should be reconsidered if we can not spare a few minutes for ourselves.

                This is my first app, so any feedback is appreciated: https://forms.gle/yqQ9SvUkurdsW9YZA

                Again, really appreciate you, user! Hope you have productive and relaxing working hours on Mindful!

                Hoang

                My email: nguyenvinhhoang21@gmail.com.""");

        TextFlow textFlow = new TextFlow(creditsText);
        textFlow.setPadding(new Insets(10));

        // Bind the width of the TextFlow to the width of the stage
        textFlow.maxWidthProperty().bind(primaryStage.widthProperty().subtract(40)); // Subtract some padding

        Button backButton = new Button("Back");
        backButton.setOnAction(_ -> primaryStage.setScene(MainApp.getStartupScene()));

        VBox.setVgrow(textFlow, Priority.ALWAYS);
        layout.getChildren().addAll(textFlow, backButton);

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);

        // Adjust the textFlow dimensions to fit within the screen size
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> adjustTextFlowWidth(textFlow, (double) newVal));
    }

    private void adjustTextFlowWidth(TextFlow textFlow, double newWidth) {
        textFlow.setMaxWidth(newWidth - 40); // Adjust for padding
    }
}