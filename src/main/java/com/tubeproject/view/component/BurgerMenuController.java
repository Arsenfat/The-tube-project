package com.tubeproject.view.component;

import com.tubeproject.controller.User;
import com.tubeproject.model.ContextMap;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class BurgerMenuController implements Initializable {

    private User user;

    @FXML
    private Label userInfo;

    @FXML
    private Button btnAdministration;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }


    public void kickNotLoggedIn() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not connected");
        alert.setHeaderText("You must be logged in to access this page");
        alert.showAndWait();

        StageManager.changeStage(userInfo, Resources.ViewFiles.LOGIN_SCREEN);
    }

    public void checkUserLoggedIn(User user) {
        this.user = user;
        if (user == null) {
            kickNotLoggedIn();
            return;
        }
        setUserInfo(user);
    }

    private void setUserInfo(User user) {
        userInfo.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
        if (user.getRole() < 3) {
            btnAdministration.setDisable(true);
            btnAdministration.setVisible(false);
        }
    }

    @FXML
    public void travel(ActionEvent e) {
        StageManager.changeStage(((Node) e.getSource()), Resources.ViewFiles.TRAVEL_SCREEN, Resources.Stylesheets.MENU);
    }

    @FXML
    public void profile(ActionEvent e) {
        StageManager.changeStage(((Node) e.getSource()), Resources.ViewFiles.PROFIL_SCREEN, Resources.Stylesheets.MENU);
    }

    @FXML
    public void history(ActionEvent e) {
        StageManager.changeStage(((Node) e.getSource()), Resources.ViewFiles.HISTORY_SCREEN, Resources.Stylesheets.MENU);
    }


    @FXML
    public void logOut(ActionEvent e) {
        ContextMap.getContextMap().put("USER", null);
        StageManager.changeStage(((Node) e.getSource()), Resources.ViewFiles.MAIN_SCREEN);
    }

    @FXML
    public void administration(ActionEvent e) {
        StageManager.changeStage(((Node) e.getSource()), Resources.ViewFiles.ADMINISTRATOR_SCREEN, Resources.Stylesheets.MENU);
    }

}
