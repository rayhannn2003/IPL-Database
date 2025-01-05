package com.example.ipldatabase.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.ipldatabase.Author.Main;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.sort;

public class LoginViewController {
  //  public Button SignIn;
    public Button LoginforCLub;
    public Label teamcount;
    public Label level2;
    public Label level3;
    public Label level1;
    public Button adminlogin;

    private Main main;

    @FXML
    public Label appName;

    @FXML
    public Label playerCount;
    @FXML
    public MediaView mediaplayer;
    private static final String MEDIA_URL = "http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv";


    @FXML
    public Label countryCount;
    @FXML
    private PasswordField password;
    @FXML
    private MenuButton clubName;

    public void initiate(Main main) throws URISyntaxException {
        if (!main.isFirstTimeTransition){
            appName.setText("VIVO IPL 2025");
            playerCount.setText("221");
            level1.setText("Players");
            teamcount.setText("10");
            level2.setText("Clubs");
            countryCount.setText("12");
            level3.setText("Countries");
        }
        Media media = new Media(Objects.requireNonNull(Main.class.getResource( "/com/example/ipldatabase/pic/virat.mp4")).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
        MediaView video;
        mediaplayer.setMediaPlayer(mediaPlayer);
        //mediaPlayer.play();
        this.main = main;
        List<String> all = new ArrayList<>();
        for (var c : main.clubLogoMap.entrySet()){
            all.add(c.getKey());
        }
        sort(all);
        for (var any : all){
            var item = new MenuItem(any);
            item.setOnAction(event -> clubName.setText(any));
            clubName.getItems().add(item);
        }
    }

    public void SignIn() throws IOException {
        String name = clubName.getText();
        String pass = password.getText();
        main.ConnectToServer(name, pass);
    }

    @FXML
    void DoLogIn(ActionEvent event) throws IOException {
        SignIn();
    }

    public void setClubClient(Main main) {
        this.main = main;
    }

    public void logInAsAdmin(ActionEvent actionEvent) {
        main.adminLogin();
    }
}