package com.tubeproject.view.user;

import com.tubeproject.utils.FXMLUtils;
import com.tubeproject.utils.ImageUtils;
import com.tubeproject.view.Resources;
import com.tubeproject.view.StageManager;
import com.tubeproject.view.component.WebButton;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewMainScreen extends Application implements Initializable {

    @FXML
    private ImageView imgView;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane webButtonPane;

    @FXML
    private javafx.scene.control.Label lblAnimation;

    private int number;


    @FXML
    private void handleButtonActionFacebook(ActionEvent event) {
        Hyperlink myHyperlink = new Hyperlink();
        myHyperlink.setText("My Link Text");

        myHyperlink.setOnAction(e -> {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.facebook.com/Tube-Project-600314397160600/"));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleButtonActionTwitter(ActionEvent event) {

    }

    @FXML
    private void HandleButtonActionEmail(ActionEvent event) {

    }

    @FXML
    private void handleButtonActionLogin(ActionEvent event) {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.LOGIN_SCREEN);
    }

    @FXML
    private void handleButtonActionSignUp(ActionEvent event) {
        StageManager.changeStage(anchorPane, Resources.ViewFiles.SIGN_UP_SCREEN);
    }



    public static void startWindow() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        AnchorPane anchorPane = FXMLUtils.loadFXML(Resources.ViewFiles.MAIN_SCREEN);

        Scene scene = new Scene(anchorPane);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> {
            System.out.println("Application shutdown");
            System.exit(0);
        });
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        number = 0;
        initializeImgView();
        initializeBackground();
        webButtonPane.getChildren().add(new WebButton(this.getHostServices()));
        animationLabel();

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

    public void animateText(Label lbl, List<String> words, int index) {
        if (index < 0)
            return;
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(4000));
            }

            protected void interpolate(double frac) {

                String content = words.get(index);
                final int length = content.length();
                final int n = Math.round(length * (float) frac);
                lbl.setText(content.substring(0, n));
            }
        };
        animation.setOnFinished(event -> animateTextReversed(lbl, words, index));
        animation.play();
    }

    public void animateTextReversed(Label lbl, List<String> words, int index) {
        if (index < 0)
            return;
        final Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(4000));
            }

            protected void interpolate(double frac) {

                String content = words.get(index);
                final int length = content.length();
                final int n = Math.round(length * (float) (1 - frac));
                lbl.setText(content.substring(0, n));
            }
        };

        animation.setOnFinished(event -> animateText(lbl, words, index - 1));
        if (index - 1 < 0)
            animation.setOnFinished(event -> animateText(lbl, words, words.size() - 1));
        animation.play();
    }

    public void animationLabel() {
        animateText(lblAnimation, List.of(" Dollis hill ?", " Old Street ?", " Oxford Circus ?", " Paddington ?", " Willesden Green ?", " Liverpool Street ?", " Marble Arch ?", " Camden Town ?", " Baker Street ?"), 8);

    }

}
