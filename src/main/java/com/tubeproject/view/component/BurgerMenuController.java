package com.tubeproject.view.component;

import com.tubeproject.model.ContextMap;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class BurgerMenuController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
