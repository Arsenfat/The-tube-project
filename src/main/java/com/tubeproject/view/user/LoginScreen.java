package com.tubeproject.view.user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.tubeproject.controller.User;
import com.tubeproject.core.Login;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.select.LoginRequest;
import com.tubeproject.utils.EmailUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.WebButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable, Injectable {

    @FXML
    private ImageView imgView;

    @FXML
    private ImageView imgViewUser;

    @FXML
    private ImageView imgViewPassword;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane webButtonPane;

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Label txtLabel;

    @FXML
    private Label lbConnectToServer;

    private Map<String, Object> contextMap;

    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        webButtonPane.getChildren().add(new WebButton((HostServices) contextMap.get("HOSTED")));
    }

    @FXML
    private void handleButtonActionHomePage() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @FXML
    private void handleButtonActionForgotPassword(ActionEvent event) {
        ContextMap.getContextMap().put("USER", null);
        StageManager.changeStage(((Node) event.getSource()), Resources.ViewFiles.CHANGE_PASSWORD_SCREEN);
    }


    @FXML
    private void handleButtonActionSignUp(ActionEvent event) {
        StageManager.changeStage((Node) event.getSource(), Resources.ViewFiles.SIGN_UP_SCREEN);
    }


    @FXML
    private void handleButtonActionTryLogin(ActionEvent event) {
        checkFields();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeImgView();
        initializeBackground();
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

    private void initializeBackground() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage bgImg = ImageUtils.loadBackgroundImage(Resources.Images.BACKGROUND, backgroundSize);
        anchorPane.setBackground(new Background(bgImg));
    }


    private void checkFields() {
        String red = "#ef5353";
        String black = "#151928";

        if (txtUsername.getText().isEmpty()
                || !EmailUtils.checkEmail(txtUsername.getText())
                || txtPassword.getText().isEmpty()) {
            changeNodeColor(txtUsername, red);
            changeNodeColor(txtPassword, red);
            changeLabelVisibility(true);
            return;
        }
        User u;

        try {
            DatabaseConnection.DatabaseOpen();

            LoginRequest lR = new LoginRequest(txtUsername.getText());
            Select select = new Select(lR);
            Optional<?> opt = select.select();
            boolean connected;
            if (opt.isPresent()) {
                u = (User) (opt.get());
                connected = Login.checkPassword(txtPassword.getText(), u.getPassword(), u.getSalt());
                if (connected) {
                    u.setSalt("");
                    u.setPassword("");
                    ContextMap.getContextMap().put("USER", u);
                    changeLabelVisibility(false);

                    changeNodeColor(txtPassword, black);
                    changeNodeColor(txtUsername, black);
                    StageManager.changeStage(anchorPane, Resources.ViewFiles.TRAVEL_SCREEN, Resources.Stylesheets.MENU);
                }
            } else
                connected = false;

            if (!connected) {
                changeNodeColor(txtUsername, red);
                changeNodeColor(txtPassword, red);
                changeLabelVisibility(true);
            }


            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            connectionFailed();
            System.out.println("Error: Connection to the server failed.");
        }

    }


    //red #ef5353
    //black #151928
    private void changeNodeColor(JFXTextField txtField, String color) {
        txtField.setFocusColor(Paint.valueOf(color));
        txtField.setUnFocusColor(Paint.valueOf(color));
        txtField.setStyle(String.format("-fx-text-inner-color : %s", color));
        txtField.setStyle(String.format("-fx-prompt-text-fill : %s", color));
        txtField.setStyle(String.format("-fx-text-inner-color: %s", color));
    }

    private void changeNodeColor(JFXPasswordField txtField, String color) {
        txtField.setFocusColor(Paint.valueOf(color));
        txtField.setUnFocusColor(Paint.valueOf(color));
        txtField.setStyle(String.format("-fx-text-inner-color : %s", color));
        txtField.setStyle(String.format("-fx-prompt-text-fill : %s", color));
        txtField.setStyle(String.format("-fx-text-inner-color: %s", color));
    }


    private void changeLabelVisibility(boolean value) {
        txtLabel.setVisible(value);

    }

    private void connectionFailed() {
        lbConnectToServer.setVisible(true);
        TranslateTransition translate = new TranslateTransition(Duration.seconds(.4), lbConnectToServer);
        translate.setFromX(0.0);
        translate.setFromY(0);
        translate.setToX(0);
        translate.setToY(-50);
        translate.play();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(5), lbConnectToServer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.play();
    }
}
