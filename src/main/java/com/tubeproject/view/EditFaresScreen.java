package com.tubeproject.view;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Fare;
import com.tubeproject.controller.Zone;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.builder.FareBuilder;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.Update;
import com.tubeproject.model.requests.select.GetFaresRequest;
import com.tubeproject.model.requests.update.UpdateFareRequest;
import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.component.BurgerMenu;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EditFaresScreen extends Application implements Initializable {

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

    private List<Fare> fares;


    @FXML
    private void handleButtonActionHomePage() {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.MAIN_SCREEN);
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
        initializeIcons();
        initializeBurger();
        fares = loadData();
        fillForm(fares);
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

    public void initializeBurger() {
        drawer.setSidePane(new BurgerMenu());

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
