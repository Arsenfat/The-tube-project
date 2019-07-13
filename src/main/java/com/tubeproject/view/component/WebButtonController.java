package com.tubeproject.view.component;

import com.tubeproject.view.Resources;
import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class WebButtonController implements Initializable {


    @FXML
    private javafx.scene.image.ImageView facebookIcon;

    @FXML
    private javafx.scene.image.ImageView twitterIcon;

    @FXML
    private ImageView instagramIcon;


    private HostServices hostServices;


    @FXML
    private void handleActionPane() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeIcons();

    }

    @FXML
    public void setUrlFacebook() {
        hostServices.showDocument("https://www.facebook.com/transportforlondon/");
    }

    public void setUrlTwitter() {
        hostServices.showDocument("https://twitter.com/TfL?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor");
        ;
    }

    public void setUrlInstagram() {
        hostServices.showDocument("https://www.instagram.com/transportforlondon/?hl=en");
    }

    private void initializeIcons() {
        InputStream stream = getClass().getResourceAsStream(Resources.Images.FACEBOOK);
        Image img = new Image(stream);
        this.facebookIcon.setImage(img);

        InputStream stream2 = getClass().getResourceAsStream(Resources.Images.TWITTER);
        Image img2 = new Image(stream2);
        this.twitterIcon.setImage(img2);

        InputStream stream3 = getClass().getResourceAsStream(Resources.Images.INSTAGRAM);
        Image img3 = new Image(stream3);
        this.instagramIcon.setImage(img3);

    }

    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
