package com.tubeproject.view.connected.travel;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Station;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.select.GetAllStationsRequest;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.WebButton;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.controlsfx.control.textfield.TextFields;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TravelScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger burger;

    @FXML
    private ImageView imgMap;

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
        datePicker.setValue(LocalDate.now());
        time.setValue(LocalTime.now());
    }


    @FXML
    private void handleButtonActionHomePage() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.TRAVEL_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.show();
        scene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());

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
        List<String> listStationName = loadStation();
        autocomplete(txtStart, listStationName);
        autocomplete(txtEnd, listStationName);
        checkTime();
        initializeBurger();
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
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

    private void initializeMap() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.MAP);
        Image img = new Image(stream);
        this.imgMap.setImage(img);
    }

    public static boolean inHierarchy(Node node, Node potentialHierarchyElement) {
        if (potentialHierarchyElement == null) {
            return true;
        }
        while (node != null) {
            if (node == potentialHierarchyElement) {
                return true;
            }
            node = node.getParent();
        }
        return false;
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

    public void autocomplete(JFXTextField txtField, List<String> listStationName) {
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

    public List<String> loadStation() {
        List<String> stationList = new ArrayList<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetAllStationsRequest stations = new GetAllStationsRequest();
            Select s = new Select(stations);

            List<Station> tmpList = (List<Station>) s.select().get();

            stationList = tmpList.stream().map(Station::getName).collect(Collectors.toList());
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
        }
        return stationList;
    }

    public void initializeBurger() {
        //drawer.setSidePane(new BurgerMenu());
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
