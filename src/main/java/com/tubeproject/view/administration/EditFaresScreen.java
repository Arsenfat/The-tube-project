package com.tubeproject.view.administration;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Fare;
import com.tubeproject.controller.User;
import com.tubeproject.controller.Zone;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.builder.FareBuilder;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.Update;
import com.tubeproject.model.requests.select.GetFaresRequest;
import com.tubeproject.model.requests.update.UpdateFareRequest;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.BurgerMenu;
import com.tubeproject.view.component.WebButton;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class EditFaresScreen extends Application implements Initializable, Injectable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXHamburger burger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private ToggleGroup ticketType;

    @FXML
    private JFXRadioButton rdAdult;

    @FXML
    private JFXRadioButton rdChild;

    @FXML
    private JFXRadioButton rdOysOffPeak;

    @FXML
    private JFXRadioButton rdOysPeak;

    @FXML
    private JFXComboBox<Zone> zoneFrom;

    @FXML
    private JFXComboBox<Zone> zoneTo;

    @FXML
    private JFXTextField txtPrice;

    @FXML
    private JFXButton btnSave;

    private BurgerMenu burgerPane;

    private Map<String, Object> contextMap;

    private List<Fare> fares;

    @FXML
    private Pane webButtonPane;

    @FXML
    private void handleButtonActionHomePage() {
        ContextMap.getContextMap().put("USER", null);
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
    }

    @FXML
    private void handleButtonActionGoBack() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.ADMINISTRATOR_SCREEN);
    }


    @Override
    public void injectMap(Map<String, Object> map) {
        contextMap = map;
        burgerPane.checkUserLoggedIn((User) contextMap.get("USER"));
    }

    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.EDIT_FARES_SCREEN);

        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().add(getClass().getResource(Resources.Stylesheets.MENU).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawer.setVisible(false);
        initializeImgView();
        initializeBackground();
        initializeBurger();
        fares = loadData();
        fillForm(fares);
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
    }

    private List<Fare> loadData() {
        List<Fare> data = new ArrayList<>();
        try {
            DatabaseConnection.DatabaseOpen();
            GetFaresRequest gFR = new GetFaresRequest();
            Select s = new Select(gFR);
            Optional<List<Fare>> value = (Optional<List<Fare>>) s.select();
            if (value.isEmpty()) {
                throw new SQLException("No values");
            }
            data = value.get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't connect to database");
            alert.setContentText("Error while processing request");
        }

        return data;
    }

    private void fillForm(List<Fare> fares) {
        List<Zone> departing = fares
                .stream()
                .map(Fare::getDepartingZone)
                .distinct()
                .collect(Collectors.toList());
        zoneFrom.setItems(FXCollections.observableArrayList(departing));
        zoneFrom.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<Zone> arriving = fares
                        .stream()
                        .filter(fare -> fare.getDepartingZone().equals(newValue))
                        .map(Fare::getArrivingZone)
                        .distinct()
                        .collect(Collectors.toList());
                zoneTo.setItems(FXCollections.observableArrayList(arriving));
                zoneTo.getSelectionModel().select(0);
            }
        }));
        zoneTo.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ticketType.selectToggle(rdAdult);
                txtPrice.setText(String.format("%.02f", fares.stream().filter(fare -> {
                    return fare.getDepartingZone().equals(zoneFrom.getValue())
                            && fare.getArrivingZone().equals(newValue)
                            && fare.getType().equals(Fare.Type.ADULT);
                }).findFirst().orElseGet(() -> new FareBuilder().createFare()).getPrice()));
                rdAdult.setDisable(false);
                rdChild.setDisable(false);
                rdOysOffPeak.setDisable(false);
                rdOysPeak.setDisable(false);
                txtPrice.setDisable(false);
                btnSave.setDisable(false);
            }
        }));

        ticketType.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue == rdAdult) {
                txtPrice.setText(String.format("%.02f", fares.stream().filter(fare -> {
                    return fare.getDepartingZone().equals(zoneFrom.getValue())
                            && fare.getArrivingZone().equals(zoneTo.getValue())
                            && fare.getType().equals(Fare.Type.ADULT);
                }).findFirst().get().getPrice()));
            } else if (newValue == rdChild) {
                txtPrice.setText(String.format("%.02f", fares.stream().filter(fare -> {
                    return fare.getDepartingZone().equals(zoneFrom.getValue())
                            && fare.getArrivingZone().equals(zoneTo.getValue())
                            && fare.getType().equals(Fare.Type.CHILD);
                }).findFirst().get().getPrice()));
            } else if (newValue == rdOysOffPeak) {
                txtPrice.setText(String.format("%.02f", fares.stream().filter(fare -> {
                    return fare.getDepartingZone().equals(zoneFrom.getValue())
                            && fare.getArrivingZone().equals(zoneTo.getValue())
                            && fare.getType().equals(Fare.Type.OYSTER_OFF_PEAK);
                }).findFirst().get().getPrice()));
            } else if (newValue == rdOysPeak) {
                txtPrice.setText(String.format("%.02f", fares.stream().filter(fare -> {
                    return fare.getDepartingZone().equals(zoneFrom.getValue())
                            && fare.getArrivingZone().equals(zoneTo.getValue())
                            && fare.getType().equals(Fare.Type.OYSTER_PEAK);
                }).findFirst().get().getPrice()));
            }
        }));
    }

    @FXML
    private void saveValue() {
        RadioButton selected = (RadioButton) ticketType.getSelectedToggle();
        Fare.Type type = Fare.Type.ADULT;
        if (selected == rdChild) {
            type = Fare.Type.CHILD;
        } else if (selected == rdOysOffPeak) {
            type = Fare.Type.OYSTER_OFF_PEAK;
        } else if (selected == rdOysPeak) {
            type = Fare.Type.OYSTER_PEAK;
        }
        Fare fare = null;
        try {
            fare = new FareBuilder()
                    .setDepartingZone(zoneFrom.getValue())
                    .setArrivingZone(zoneTo.getValue())
                    .setType(type).setPrice(Double.parseDouble(txtPrice.getText())).createFare();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("NOT A NUMBER");
            alert.setContentText("YOUR PRICE IS INVALID !");
            alert.showAndWait();
        }

        try {
            DatabaseConnection.DatabaseOpen();
            UpdateFareRequest uFR = new UpdateFareRequest(fare);
            Update u = new Update(uFR);
            u.update();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't connect to server");
            alert.setContentText("Can't update prices, error !");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Price updated");
        alert.setContentText("Price updated !");
        alert.showAndWait();
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
