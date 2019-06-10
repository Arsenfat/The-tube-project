package com.tubeproject.view;

import com.tubeproject.utils.FXMLUtils;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ViewMainScreen extends Application implements EventHandler<ActionEvent> {
    private Button buttonTopRight;
    private Button buttonCenter;
    private Label topLeftCorner;
    private Label topRightCorner;
    private Label center;
    private Stage stage;


    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.MAIN_SCREEN);
        //scene : what is display
        Scene scene = new Scene(anchorPane);
        //stage : application
        stage.setScene(scene);
        stage.setTitle("Tube");
        stage.show();
    }

    //whenever an user click the button
    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttonCenter) {

        }
    }

    public void onClickLogin(ActionEvent event) throws Exception {
        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.LOGIN_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
    }

    public void mangerDuPain(ActionEvent ae) {
        System.out.println("wallah");
    }
}
