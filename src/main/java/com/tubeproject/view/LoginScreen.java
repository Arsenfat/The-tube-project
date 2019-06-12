package com.tubeproject.view;

import com.jfoenix.controls.JFXTextField;
import com.tubeproject.utils.FXMLUtils;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private ImageView imgViewUser;

    @FXML
    private ImageView imgViewPassword;

    @FXML
    private JFXTextField txtUsername;

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.LOGIN_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeImgView();
    }

    private void initializeImgView() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.LOGO1);
        Image img = new Image(stream);
        imgView.setImage(img);
        stream = getClass().getResourceAsStream(Resources.Images.PASSWORD);
        img = new Image(stream);
        imgViewPassword.setImage(img);
        stream = getClass().getResourceAsStream(Resources.Images.USERNAME);
        img = new Image(stream);
        imgViewUser.setImage(img);
    }
}
