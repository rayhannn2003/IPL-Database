package com.example.ipldatabase.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import com.example.ipldatabase.Author.Main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

public class ClubLoginController {
    public Button signIn;
    private Main main;

    @FXML
    public Label appName;

    @FXML
    public Label playerCount;

    @FXML
    public Label clubCount;

    @FXML
    public Label countryCount;

    @FXML
    public Label l1;

    @FXML
    public Label l2;

    @FXML
    public Label l3;

    @FXML
    private PasswordField password;

    @FXML
    private MenuButton clubName;

    public void initiate(Main main){//INTIIALIZE THE CLUB LOGIN PAGE WITH THE MAIN OBJECT
        if (!main.isFirstTimeTransition){
            appName.setText("VIVO IPL 2025");
            playerCount.setText("221");
            l1.setText("Players");
            clubCount.setText("10");
            l2.setText("Clubs");
            countryCount.setText("12");
            l3.setText("Countries");
        }
        this.main = main;//SET THE MAIN OBJECT
        List<String> all = new ArrayList<>();
        for (var club_c : main.clubLogoMap.entrySet()){
            all.add(club_c.getKey());
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
    void DoSignIn(ActionEvent event) throws IOException {
        SignIn();
    }

    public void setClubClient(Main main) {
        this.main = main;
    }
}