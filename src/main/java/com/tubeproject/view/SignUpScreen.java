package com.tubeproject.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.tubeproject.controller.User;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Insert;
import com.tubeproject.model.Select;
import com.tubeproject.model.builder.UserBuilder;
import com.tubeproject.model.requests.DuplicateMailRequest;
import com.tubeproject.model.requests.InsertUserRequest;
import com.tubeproject.utils.EmailUtils;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXButton facebookIcon;

    @FXML
    private JFXButton twitterIcon;

    @FXML
    private JFXButton instagramIcon;

    @FXML
    private JFXButton mailIcon;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtMail;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private Label lbConnectToServer;

    @FXML
    private Label txtLabel;

    @FXML
    private DatePicker dpDate;

    @FXML
    private void handleButtonActionHomePage() {
        System.out.println("you've clicked");
        AnchorPane homePage;
        try {
            homePage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.MAIN_SCREEN));

        } catch (IOException e) {
            System.out.println("Warning unandled exeption.");
            return;
        }
        Scene homeScene = new Scene(homePage);
        Stage homeStage = (Stage) anchorPane.getScene().getWindow();
        homeStage.setScene(homeScene);
        homeStage.show();
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.SIGN_UP_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeImgView();
        initializeBackground();
        initializeIcons();
    }

    @FXML
    private void handleButtonActionTrySignUp(ActionEvent event) {
        boolean valid = checkFields();
        if (valid) {
            String black = "#151928";
            changeNodeColor(txtPassword, black);
            changeNodeColor(txtFirstName, black);
            changeNodeColor(txtLastName, black);
            changeNodeColor(txtMail, black);
            insertUser();
        }
    }

    private void initializeImgView() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.LOGO1);
        Image img = new Image(stream);
        this.imgView.setImage(img);
    }

    private void initializeBackground() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, false);
        BackgroundImage bgImg = ImageUtils.loadBackgroundImage(Resources.Images.BACKGROUND, backgroundSize);
        anchorPane.setBackground(new Background(bgImg));
    }

    private void initializeIcons() {
        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        BackgroundImage bgImg = ImageUtils.loadBackgroundImage(Resources.Images.FACEBOOK, backgroundSize);
        facebookIcon.setBackground(new Background(bgImg));

        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        bgImg = ImageUtils.loadBackgroundImage(Resources.Images.TWITTER, backgroundSize);
        twitterIcon.setBackground(new Background(bgImg));


        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        bgImg = ImageUtils.loadBackgroundImage(Resources.Images.INSTAGRAM, backgroundSize);
        instagramIcon.setBackground(new Background(bgImg));

        backgroundSize = new BackgroundSize(100, 100, true, true, false, true);
        bgImg = ImageUtils.loadBackgroundImage(Resources.Images.MAIL, backgroundSize);
        mailIcon.setBackground(new Background(bgImg));


    }

    private boolean checkFields() {
        String red = "#ef5353";
        boolean succeed = true;
        if (txtPassword.getText().isEmpty()) {
            changeNodeColor(txtPassword, red);
            changeLabelVisibility(true);
            succeed = false;
        }
        if (txtFirstName.getText().isEmpty()) {
            changeNodeColor(txtFirstName, red);
            changeLabelVisibility(true);
            succeed = false;
        }
        if (txtLastName.getText().isEmpty()) {
            changeNodeColor(txtLastName, red);
            changeLabelVisibility(true);
            succeed = false;
        }
        if (txtMail.getText().isEmpty() || !EmailUtils.checkEmail(txtMail.getText())) {
            changeNodeColor(txtMail, red);
            changeLabelVisibility(true);
            succeed = false;
        }

        return succeed;
    }

    private void insertUser() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String password = txtPassword.getText();
        String email = txtMail.getText();
        Date dateOfBirth = Date.valueOf(dpDate.getValue());

        User userToInsert = new UserBuilder().setFirstName(firstName)
                .setLastName(lastName)
                .setDateOfBirth(dateOfBirth)
                .setEmail(email)
                .setRole(1)
                .setPassword(password)
                .createUser();
        userToInsert.crypt();
        try {
            DatabaseConnection.DatabaseOpen();
            DuplicateMailRequest dMR = new DuplicateMailRequest(email);
            Select s = new Select(dMR);
            boolean duplicate = (Boolean) s.select().get();
            if (duplicate) {
                InsertUserRequest uR = new InsertUserRequest(userToInsert);
                Insert insert = new Insert(uR);
                insert.insert();
            } else {
                System.out.println("DUPLICATE");
            }
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
            connectionFailed();
        }
    }

    private void changeLabelVisibility(boolean value) {
        txtLabel.setVisible(value);

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
        fadeOut.setOnFinished((ActionEvent event) -> lbConnectToServer.setVisible(false));
        lbConnectToServer.setOnMouseClicked((MouseEvent event) -> {
                    lbConnectToServer.setVisible(false);
                }
        );
    }
}
