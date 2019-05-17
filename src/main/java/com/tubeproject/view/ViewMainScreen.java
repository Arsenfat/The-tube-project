package com.tubeproject.view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewMainScreen extends Application implements EventHandler<ActionEvent> {
    private Button button;
    private Button btn;
    private Label myLabel;
    private Label myLabel2;

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Tube project");

        button = new Button("Sign Up or Login");
        button.setOnAction(this);
        btn = new Button();
        btn.setText("Sign Up or Login");
        btn.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        final Stage dialog = new Stage();
                        dialog.initModality(Modality.APPLICATION_MODAL);
                        dialog.initOwner(stage);
                        VBox dialogVbox = new VBox(20);
                        dialogVbox.getChildren().add(new Text("Sign up and Log in with e-mail"));
                        Scene dialogScene = new Scene(dialogVbox, 300, 200);
                        dialog.setScene(dialogScene);
                        dialog.show();
                    }
                });
        myLabel = new Label();
        myLabel2 = new Label("Second Label");
        myLabel.setText("Welcome to Tube ! You want to go to Baker Street ?");

        FlowPane flowPaneRoot = new FlowPane(10, 10);
        flowPaneRoot.setAlignment(Pos.CENTER);

        //StackPane layout = new StackPane();
        flowPaneRoot.getChildren().add(button);
        flowPaneRoot.getChildren().add(btn);
        flowPaneRoot.getChildren().add(myLabel);
        flowPaneRoot.getChildren().add(myLabel2);
        Scene scene = new Scene(flowPaneRoot, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    //whenever an user click the button
    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == button) {

        }
    }
}
