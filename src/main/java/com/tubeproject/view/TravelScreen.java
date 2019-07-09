package com.tubeproject.view;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Station;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.Select;
import com.tubeproject.model.requests.GetAllStationsRequest;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
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
    private JFXButton facebookIcon;

    @FXML
    private JFXButton twitterIcon;

    @FXML
    private JFXButton instagramIcon;

    @FXML
    private JFXButton mailIcon;

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
    private void handleButtonActionHome(ActionEvent event) {

    }

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
        initializeIcons();
        //initializeMap();
        setClickoutEvent();
        initializeTextFieldEvent(txtStart, startBtn);
        initializeTextFieldEvent(txtEnd, endBtn);
        List<String> listStationName = loadStation();
        autocomplete(txtStart, listStationName);
        autocomplete(txtEnd, listStationName);
        checkTime();
        initializeBurger();
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
        try {
            VBox vbox = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.VBOX));
            drawer.setSidePane(vbox);
            for (Node node : vbox.getChildren()) {
                if (node.getId() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                        switch (node.getId()) {
                            case "btnHistory":
                                AnchorPane historyPage;
                                try {
                                    historyPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.HISTORY_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                Scene homeScene = new Scene(historyPage);
                                Stage homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeScene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
                                homeStage.show();
                                break;
                            case "btnAdministration":
                                AnchorPane administrationPage;
                                try {
                                    administrationPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.ADMINISTRATOR_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                homeScene = new Scene(administrationPage);
                                homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeScene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
                                homeStage.show();
                                break;
                            case "btnLogOut":
                                ContextMap.getContextMap().put("USER", null);
                                AnchorPane homePage;
                                try {
                                    homePage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.MAIN_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                homeScene = new Scene(homePage);
                                homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeStage.show();
                                break;
                            case "btnProfile":
                                AnchorPane profilPage;
                                try {
                                    profilPage = FXMLLoader.load(getClass().getResource(Resources.ViewFiles.PROFIL_SCREEN));

                                } catch (IOException ex) {
                                    System.out.println("Warning unandled exeption.");
                                    return;
                                }
                                homeScene = new Scene(profilPage);
                                homeStage = (Stage) anchorPane.getScene().getWindow();
                                homeStage.setScene(homeScene);
                                homeScene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
                                homeStage.show();
                                break;
                        }
                    });
                }
            }

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
        } catch (Exception e) {
            System.out.println(e);
        }

    }

}
