package com.tubeproject.view.user;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.builder.UserBuilder;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.Update;
import com.tubeproject.model.requests.select.EmailExistsRequest;
import com.tubeproject.model.requests.update.UpdatePasswordRequest;
import com.tubeproject.utils.EmailUtils;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.WebButton;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePasswordScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane webButtonPane;

    @FXML
    private Label lblEmail;

    @FXML
    private Label lblPassword;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXPasswordField pwdPassword;

    @FXML
    private JFXPasswordField pwdConfirm;


    @FXML
    private void handleButtonActionHomePage() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.CHANGE_PASSWORD_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeImgView();
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
        initializeBackground();
    }

    private void initializeBackground() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage bgImg = ImageUtils.loadBackgroundImage(Resources.Images.BACKGROUND, backgroundSize);
        anchorPane.setBackground(new Background(bgImg));
    }


    private void initializeImgView() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.LOGO1);
        Image img = new Image(stream);
        this.imgView.setImage(img);
    }


    @FXML
    public void handlePasswordReset() {
        String black = "#151928";
        String red = "#ef5353";

        if (txtEmail.getText().isEmpty()) {
            setWarning(lblEmail, "E-Mail cannot be empty");
            changeNodeColor(txtEmail, red);
            return;
        }


        if (!EmailUtils.checkEmail(txtEmail.getText())) {
            setWarning(lblEmail, "Enter a valid E-Mail");
            changeNodeColor(txtEmail, red);
            return;
        }
        changeNodeColor(txtEmail, black);
        hideWarning(lblEmail);

        if (pwdConfirm.getText().isEmpty() || pwdPassword.getText().isEmpty()) {
            setWarning(lblPassword, "Passwords cannot be empty");
            setWarning(lblPassword, "Passwords cannot be empty");
            changeNodeColor(pwdConfirm, red);
            changeNodeColor(pwdPassword, red);
            return;
        }

        if (!pwdPassword.getText().equals(pwdConfirm.getText())) {
            setWarning(lblPassword, "Your passwords must match !");
            changeNodeColor(pwdPassword, red);
            changeNodeColor(pwdConfirm, red);
            return;
        }
        changeNodeColor(pwdPassword, black);
        changeNodeColor(pwdConfirm, black);
        hideWarning(lblPassword);


        User user = new UserBuilder().setEmail(txtEmail.getText()).setPassword(pwdPassword.getText()).createUser();
        user.crypt();
        System.out.println("user = " + user);
        boolean fail = false;
        try {
            DatabaseConnection.DatabaseOpen();
            EmailExistsRequest eER = new EmailExistsRequest(txtEmail.getText());
            Select s = new Select(eER);
            boolean exists = (Boolean) s.select().get();
            if (!exists) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("User not found");
                alert.setContentText("The e-mail is not in the database !");

                alert.showAndWait();
                return;
            }
            UpdatePasswordRequest uPR = new UpdatePasswordRequest(user);
            Update u = new Update(uPR);
            u.update();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
            fail = true;
        }
        if (!fail) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password changed");
            alert.setContentText("Your password has been changed successfully !");

            alert.showAndWait();
            StageManager.changeStage(anchorPane, Resources.ViewFiles.LOGIN_SCREEN);

        }


    }

    private void setWarning(Label warn, String message) {
        warn.setText(message);
        warn.setVisible(true);
    }

    private void hideWarning(Label warn) {
        warn.setVisible(false);
    }

    private void changeNodeColor(JFXPasswordField txtField, String color) {
        txtField.setFocusColor(Paint.valueOf(color));
        txtField.setUnFocusColor(Paint.valueOf(color));
        txtField.setStyle(String.format("-fx-text-inner-color : %s", color));
        txtField.setStyle(String.format("-fx-prompt-text-fill : %s", color));
        txtField.setStyle(String.format("-fx-text-inner-color: %s", color));
    }

    private void changeNodeColor(JFXTextField txtField, String color) {
        txtField.setFocusColor(Paint.valueOf(color));
        txtField.setUnFocusColor(Paint.valueOf(color));
        txtField.setStyle(String.format("-fx-text-inner-color : %s", color));
        txtField.setStyle(String.format("-fx-prompt-text-fill : %s", color));
        txtField.setStyle(String.format("-fx-text-inner-color: %s", color));
    }

}
