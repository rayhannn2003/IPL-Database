package com.example.ipldatabase.Controllers;


import com.example.ipldatabase.DataModel.Club;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import com.example.ipldatabase.Author.Main;
import com.example.ipldatabase.SecretData.Request;

import java.io.IOException;
import java.net.URISyntaxException;

public class ClubDashboardController {
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private Label clubName;

    @FXML
    private ImageView clubLogo;

    @FXML
    public JFXButton myPlayerButton;

    @FXML
    public JFXButton searchPlayerButton;

    @FXML
    public JFXButton marketplaceButton;

    @FXML
    public JFXButton logoutButton;

    @FXML
    public Label budget;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label clubWorth;

    public void initiate(Club club){
        main.mainPane = anchorPane;
        main.dashboardController = this;
        clubName.setText(club.getName());
        budget.setText(Club.showCurrency(club.getTransferBudget()));
        clubWorth.setText(Club.showCurrency(club.getWorth()));
        clubLogo.setImage(main.cLogo);
    }

    @FXML
    void LogOut(ActionEvent event) throws IOException, URISyntaxException {
        main.myNetworkUtil.write(new Request(main.myClub.getName(), Request.Type.LogOut));
        if (main.tempStage != null && main.tempStage.isShowing()) main.tempStage.close();
        main.showLoginPage();
    }

    @FXML
    void ShowBuyablePlayers(ActionEvent event) throws IOException {
        main.showBuyablePlayers();
    }

    @FXML
    void ShowMyPlayers(ActionEvent event) throws IOException {
        main.showMyPlayers();
    }

    @FXML
    void ShowSearchOptions(ActionEvent event) throws IOException {//TODO search options
        main.showSearchPage();
    }

    @FXML
    void changePassword() throws IOException{//TODO change password
        main.showPasswordChangingWindow();
    }

}