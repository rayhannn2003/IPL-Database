package com.example.ipldatabase.Controllers;

import com.example.ipldatabase.SecretData.BuyRequest;
import com.example.ipldatabase.DataModel.Club;
import com.example.ipldatabase.Author.Main;
import com.example.ipldatabase.DataModel.Player;
//import package for jfoenix button
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;

public class MinimalPlayerDetailController {

    @FXML
   private ImageView ImageView;
    @FXML
    private ImageView flag;
    private PlayerListViewController.PageType pageType;
    private Player player;
    private Main main;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private Label playerName;

    @FXML
    private Label clubName;

    @FXML
    private Label countryName;

    @FXML
    private Label age;

    @FXML
    private Label position;

    @FXML
    private Label salary;

    @FXML
    private Label fee;

    @FXML
    public VBox playerPane;

    @FXML
    private ImageView transferLabel;

    @FXML
    private JFXButton transferButton;

    public Label getFee() {
        return fee;
    }

    public void setFee(Label fee) {
        this.fee = fee;
    }

    public ImageView getTransferLabel() {
        return transferLabel;
    }

    public JFXButton getTransferButton() {
        return transferButton;
    }

    public VBox initiate(Player p, PlayerListViewController.PageType pageType) {
        this.pageType = pageType;
        this.player = p;
        playerName.setText(p.getName());
        clubName.setText(p.getClubName());
        countryName.setText(p.getCountry());
        age.setText(String.valueOf(p.getAge()));
        position.setText(p.getPosition());
        salary.setText(Club.showCurrency(p.getSalary()));

        try {
            Image pImage = new Image(player.getImageSource(), true);
            ImageView.setImage(pImage);
        } catch (Exception e) {
            var loaded = Main.class.getResourceAsStream("/com/example/ipldatabase/pic/anonym.jpg");
            assert loaded != null;
            ImageView.setImage(new Image(loaded));
        }
        Image flagImage = null;
        var what = main.countryFlagMap.get(player.getCountry());
        if (what != null) flagImage = new Image(what, true);
        else flagImage = new Image("https://www.pngfind.com/pngs/m/295-2959995_anonymous-png-transparent-png.png", true);
        flag.setImage(flagImage);


        if (pageType == PlayerListViewController.PageType.SimpleList){
            transferButton.setText("Sell Player");
            if (player.isTransferListed()) transferButton.setDisable(true);
        }
        else if (pageType == PlayerListViewController.PageType.TransferList) {
            transferButton.setText("Buy Player");
            if (player.getClubName().equalsIgnoreCase(main.myClub.getName())) transferButton.setDisable(true);
        }
        if (p.isTransferListed()){
            fee.setText(Club.showCurrency(p.getTransferFee()));
            transferLabel.setImage(main.seal);
        }
        else {
            fee.setText("N/A");
            transferLabel.setImage(null);
        }
        return playerPane;
    }

    @FXML
    void DoTransferAction(ActionEvent event) throws IOException {
        if (pageType == PlayerListViewController.PageType.SimpleList) {
            main.AskForTransferFee(this);
        } else {
            main.myNetworkUtil.write(new BuyRequest(player.getName(), player.getClubName(), main.myClub.getName()));
        }
    }

    @FXML
    void showPlayerDetails(ActionEvent event) throws IOException {
        main.showPlayerDetail(new Stage(), player);
    }
}
