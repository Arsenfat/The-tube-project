package com.tubeproject.view.connected.travel;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.algorithm.PathCalculator;
import com.tubeproject.controller.StationWLine;
import com.tubeproject.controller.User;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.BurgerMenu;
import com.tubeproject.view.component.WebButton;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.controlsfx.control.textfield.TextFields;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TravelScreen implements Initializable, Injectable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger burger;

    @FXML
    private JFXButton startBtn;

    @FXML
    private JFXButton endBtn;

    @FXML
    private JFXTextField txtStart;

    @FXML
    private JFXTextField txtEnd;

    @FXML
    private Pane timeInfo;

    @FXML
    private JFXButton nowBtn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private JFXButton arriveBtn;

    @FXML
    private JFXButton leaveBtn;

    @FXML
    private JFXTimePicker time;

    @FXML
    private JFXButton goBtn;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Pane webButtonPane;

    private Map<String, Object> contextMap;
    private BurgerMenu burgerPane;

    @FXML
    private void handleButtonActionArrive(ActionEvent event) {
        arriveBtn.setStyle("-fx-background-color:  #97bb91 ; -fx-text-fill: black");
        leaveBtn.setStyle(" -fx-text-fill: #151928");
        time.setVisible(true);
    }

    @FXML
    private void handleButtonActionLeave(ActionEvent event) {
        leaveBtn.setStyle("-fx-background-color:  #97bb91 ; -fx-text-fill: black");
        arriveBtn.setStyle(" -fx-text-fill: #151928");
        time.setVisible(true);
    }

    @FXML
    private void handleButtonActionNow(ActionEvent event) {
        nowBtn.setText("> Later");
        datePicker.setValue(LocalDate.now());
        time.setValue(LocalTime.now());
    }


    @FXML
    private void handleButtonActionHomePage(MouseEvent event) {
        System.out.println("yeah");
        ContextMap.getContextMap().put("USER", null);
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        webButtonPane.getChildren().add(new WebButton((HostServices) contextMap.get("HOSTED")));
        burgerPane.checkUserLoggedIn((User) contextMap.get("USER"));

    }

    @FXML
    private void goToJourney() {
        List<StationWLine> stationList = loadStation();
        if (startBtn.getText() != null && endBtn.getText() != null && !startBtn.getText().equalsIgnoreCase("") && !endBtn.getText().equalsIgnoreCase("")) {
            StationWLine start = stationList.stream().filter((station) -> startBtn.getText().equalsIgnoreCase(station.toString())).findFirst().orElse(null);
            StationWLine end = stationList.stream().filter((station) -> endBtn.getText().equalsIgnoreCase(station.toString())).findFirst().orElse(null);
            if (start != null && end != null) {
                contextMap.put("START_STATION", start);
                contextMap.put("END_STATION", end);
                StageManager.changeStage(anchorPane, Resources.ViewFiles.JOURNEY_SCREEN, Resources.Stylesheets.MENU);
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Error, stations do not exist");
                a.setHeaderText("Error, station does not exit !");
                a.setContentText(String.format("Station %s does not exist", (start == null) ? start : (end == null) ? end : "none"));
            }
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        initializeBackground();
        //initializeMap();
        setClickoutEvent();
        initializeTextFieldEvent(txtStart, startBtn);
        initializeTextFieldEvent(txtEnd, endBtn);
        List<StationWLine> listStationName = loadStation();
        autocomplete(txtStart, listStationName);
        autocomplete(txtEnd, listStationName);
        checkTime();
        initializeBurger();
        imgView.setOnMouseClicked(this::handleButtonActionHomePage);
    }

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent) node, nodes);
        }
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

    public void setClickoutEvent() {
        anchorPane.setOnMouseClicked((evt) -> {
            txtStart.setVisible(false);
            txtEnd.setVisible(false);
        });
        for (Node tmpNode : getAllNodes(anchorPane)) {
            tmpNode.setOnMouseClicked((evt) -> {
                Object source = evt.getSource();
                if (source instanceof TextField) {
                    TextField txt = (TextField) source;
                    if (txt.getId().equals(txtStart.getId())) {
                        txtEnd.setVisible(false);
                    } else if (txt.getId().equals(txtEnd.getId())) {
                        txtStart.setVisible(false);
                    }
                } else if (source instanceof Pane) {
                    Pane pane = (Pane) source;
                    if (pane.getId() != null && pane.getId().equals(timeInfo.getId())) {
                        txtStart.setVisible(false);
                        txtEnd.setVisible(false);
                    } else {
                        txtStart.setVisible(false);
                        txtEnd.setVisible(false);
                    }
                } else if (source instanceof Button) {
                    Button button = (Button) source;
                    if (button.getId().equals(startBtn.getId())) {
                        txtStart.setVisible(true);
                        txtEnd.setVisible(false);
                    } else if (button.getId().equals(endBtn.getId())) {
                        txtEnd.setVisible(true);
                        txtStart.setVisible(false);
                    } else if (button.getId().equals(nowBtn.getId())) {
                        txtStart.setVisible(false);
                        txtEnd.setVisible(false);
                        timeInfo.setVisible(true);
                    } else if (button.getId().equals(arriveBtn.getId())) {
                        //do nothing
                    } else if (button.getId().equals(leaveBtn.getId())) {
                        //do nothing
                    }
                } else if (source instanceof DatePicker) {
                    DatePicker datePicker = (DatePicker) source;
                    if (datePicker.getId().equals(datePicker.getId())) {
                        //do nothing
                    }
                } else if (source instanceof JFXTimePicker) {
                    JFXTimePicker label = (JFXTimePicker) source;
                    if (label.getId().equals(time.getId())) {
                        //do nothing
                    }
                } else {
                    txtEnd.setVisible(false);
                    txtStart.setVisible(false);
                }
            });
        }

    }

    public void autocomplete(JFXTextField txtField, List<StationWLine> listStationName) {
        TextFields.bindAutoCompletion(txtField, listStationName);
    }

    public void initializeTextFieldEvent(JFXTextField textField, JFXButton button) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            button.setText(newValue);
            button.setStyle("-fx-background-color:  #97bb91 ; -fx-text-fill: black");
        });
        textField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            if (!txtStart.getText().equals("") && !txtEnd.getText().equals("")) {
                goBtn.setStyle("-fx-background-color:  #97bb91; -fx-background-radius:   16.4, 15 ; -fx-text-fill: black");
            }
        }));
    }

    public void checkTime() {
        datePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0);
            }
        });
    }

    public List<StationWLine> loadStation() {
        return new ArrayList<>(PathCalculator.getTravelEdges().keySet());
    }

    public void initializeBurger() {
        burgerPane = new BurgerMenu();
        drawer.setSidePane(burgerPane);
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(burger);
        transition.setRate(-1);
        burger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();
            if (drawer.isShown()) {
                drawer.close();
                drawer.setVisible(false);
            } else {
                drawer.setVisible(true);
                drawer.open();
            }
        });

    }

}
