package ch.epfl.rigel.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {

    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);

        Button closeButton = new Button("Stay with Rigel");
        Button proButton = new Button("more about Pro");
        Button upgradeButton = new Button("Upgrade now!");
        upgradeButton.setVisible(false);

        closeButton.setOnAction(e -> window.close());

        proButton.setOnAction(e -> {
            label.setText("Enjoy surfing the night sky with full access to 3D " + '\n' +
                            "visualizations of celestial bodies, extended info, etc.");
            proButton.setVisible(false);
            closeButton.setVisible(false);
            upgradeButton.setVisible(true);
        });

        upgradeButton.setOnAction(e -> {
            label.setText("Upgrade unavailable because of technical limitations.");
            upgradeButton.setVisible(false);
            closeButton.setVisible(true);
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton, proButton, upgradeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
