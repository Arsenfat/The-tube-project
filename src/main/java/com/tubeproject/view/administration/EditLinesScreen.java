package com.tubeproject.view.administration;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import com.tubeproject.controller.Line;
import com.tubeproject.controller.Station;
import com.tubeproject.controller.User;
import com.tubeproject.model.ContextMap;
import com.tubeproject.model.DatabaseConnection;
import com.tubeproject.model.builder.StationBuilder;
import com.tubeproject.model.interfaces.Injectable;
import com.tubeproject.model.requests.Select;
import com.tubeproject.model.requests.Update;
import com.tubeproject.model.requests.select.GetAllLinesWithStationsRequest;
import com.tubeproject.model.requests.update.UpdateStationRequest;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class EditLinesScreen extends Application implements Initializable, Injectable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane webButtonPane;

    @FXML
    private JFXHamburger burger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private Label naptanValue;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtLatitude;

    @FXML
    private JFXTextField txtLongitude;

    @FXML
    private JFXCheckBox chkWheelchair;

    @FXML
    private JFXComboBox<Line> cmbLine;

    @FXML
    private JFXComboBox<Station> cmbStation;

    private BurgerMenu burgerPane;

    private Map<String, Object> contextMap;

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
        anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.EDIT_LINES_SCREEN);

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
        initializeBurger();
        chkWheelchair.setAllowIndeterminate(false);
        List<Line> lineList = loadData();
        fillCombBox(lineList);
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
    }

    private List<Line> loadData() {

        try {
            DatabaseConnection.DatabaseOpen();
            GetAllLinesWithStationsRequest getAllLinesWithStationsRequest = new GetAllLinesWithStationsRequest();
            Select s = new Select(getAllLinesWithStationsRequest);
            Optional<List<Line>> optional = (Optional<List<Line>>) s.select();
            if (optional.isPresent())
                return optional.get();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Couldn't load data");
            alert.setHeaderText("Couldn't load data from the server ! Try reloading the page.");
            alert.showAndWait();
        }

        return new ArrayList<>();
    }

    private void fillCombBox(List<Line> lineData) {

        cmbLine.setItems(FXCollections.observableArrayList(lineData));
        cmbLine.setConverter(new StringConverter<>() {
            @Override
            public String toString(Line line) {
                return line.getName();
            }

            @Override
            public Line fromString(String string) {
                return lineData.stream().filter(line -> line.getName().equals(string)).findFirst().orElse(null);
            }
        });
        cmbStation.setConverter(new StringConverter<>() {
            @Override
            public String toString(Station station) {
                return station.getName();
            }

            @Override
            public Station fromString(String stationName) {
                return lineData
                        .stream()
                        .filter(line -> line.equals(cmbLine.getValue()))
                        .map(Line::getStations)
                        .flatMap(Collection::stream)
                        .filter((station) -> stationName.equals(station))
                        .findFirst()
                        .orElse(null);
            }
        });
        cmbLine.valueProperty().addListener(((observable, oldValue, newValue) -> {
            cmbStation.setItems(FXCollections.observableArrayList(newValue.getStations()));
            cmbStation.getSelectionModel().select(0);
        }));

        cmbStation.valueProperty().addListener(((observable, oldValue, newValue) -> fillForm(newValue)));

    }

    private void fillForm(Station station) {
        naptanValue.setText(station.getNaptan());
        txtName.setText(station.getName());
        txtLatitude.setText(String.format("%.02f", station.getLatitude()));
        txtLongitude.setText(String.format("%.02f", station.getLongitude()));
        chkWheelchair.setSelected(station.isWheelchair());
    }

    @FXML
    public void updateData() {
        String naptan = naptanValue.getText();
        String name = txtName.getText();
        double longitude = warnDoubleParsing(txtLongitude);
        double latitude = warnDoubleParsing(txtLatitude);
        if (longitude == Double.MIN_VALUE || latitude == Double.MIN_VALUE)
            return;
        boolean wheelchair = chkWheelchair.isSelected();
        Station station = new StationBuilder()
                .setNaptan(naptan)
                .setName(name)
                .setLatitude(latitude)
                .setLongitude(longitude)
                .setWheelchair(wheelchair)
                .createStation();
        try {
            DatabaseConnection.DatabaseOpen();
            UpdateStationRequest updateStationRequest = new UpdateStationRequest(station);
            Update update = new Update(updateStationRequest);
            update.update();
            DatabaseConnection.DatabaseClose();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while sending data.");
            alert.setHeaderText("Update failure");
            alert.setContentText("An error occurred while sending data to the server.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update success");
        alert.setHeaderText("Update success");
        alert.setContentText(String.format("%s has been updated with success.", name));
        alert.showAndWait();
    }

    public double warnDoubleParsing(JFXTextField textField) {
        try {
            return Double.parseDouble(textField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Number wrong format");
            alert.setHeaderText("Enter a valid floating point number !");
            alert.setContentText(String.format("%s is a wrong number.", textField.getPromptText()));
            alert.showAndWait();
            return Double.MIN_VALUE;
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
