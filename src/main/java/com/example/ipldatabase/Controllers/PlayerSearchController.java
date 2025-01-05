package com.example.ipldatabase.Controllers;

import com.example.ipldatabase.Author.Main;
import com.example.ipldatabase.DataModel.Club;
import com.example.ipldatabase.DataModel.Player;
import com.example.ipldatabase.SecretData.BuyRequest;
import com.jfoenix.controls.JFXButton;
import com.example.ipldatabase.util.MyAlert;
import com.example.ipldatabase.util.CurrentScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerSearchController {
    private Main main;
    private Club club;
    private boolean willShowTheResult;

    public void setMain(Main main) {
        this.main = main;
    }

//    public void setBatsman(ActionEvent actionEvent) {
//    }
//
//    public void setBowler(ActionEvent actionEvent) {
//    }
//
//    public void setAllrounder(ActionEvent actionEvent) {
//    }
//
//    public void setWicketkeeper(ActionEvent actionEvent) {
//    }

    public enum Type{
        NormalStuff, MaxSal, MaxHeight, MaxAge, TotalSalary;
    }

    @FXML
    private TextField PlayerName;

    @FXML
    private TextField CountryName;

    @FXML
    private MenuButton PositionChoice;

    @FXML
    private TextField MinSalary;

    @FXML
    private TextField MaxSalary;

    @FXML
    private Label clubName;

    @FXML
    private ImageView clubLogo;

    public static List<Player> getIntersectionOfLists(List<Player>list1, List<Player>list2){
        List<Player>answer = new ArrayList<>();
        for (var p : list1){
            if (list2.contains(p)) answer.add(p);
        }
        return answer;
    }

    public List<Player> doTheSearch() {
        main.latestSearchType = Type.NormalStuff;
        double minSalary = -1, maxSalary = -1;
        List<Player> answer = new ArrayList<>();
        try {
            if (!MinSalary.getText().isEmpty()) minSalary = Double.parseDouble(MinSalary.getText());
            if (!MaxSalary.getText().isEmpty()) maxSalary = Double.parseDouble(MaxSalary.getText());
        } catch (Exception e) {
            new MyAlert(Alert.AlertType.ERROR, MyAlert.MessageType.InvalidSalaryInput).show();
            willShowTheResult = false;
            return answer;
        }
        String pName = PlayerName.getText(), countryName = CountryName.getText(), clubName = club.getName(), pChoice = PositionChoice.getText();
        if (pName.isEmpty() && countryName.isEmpty() && clubName.isEmpty() && minSalary == -1 && maxSalary == -1 && pChoice.equalsIgnoreCase("Position")){
            new MyAlert(Alert.AlertType.ERROR, MyAlert.MessageType.NoInput).show();
            willShowTheResult = false;
            return answer;
        }
        if (!pName.isEmpty()) answer = club.SearchByUnaccentedNameInClub(pName);
        else {
            if (countryName.isEmpty()) countryName = "any";
            answer = club.SearchPlayerByCountryInClub(countryName);
            if (!pChoice.isEmpty() && !pChoice.equalsIgnoreCase("Position") && !pChoice.equalsIgnoreCase("Any"))
                answer = getIntersectionOfLists(answer, club.SearchPlayerByPositionInClub(pChoice));
            if (minSalary != -1 || maxSalary != -1){
                answer = getIntersectionOfLists(answer, club.SearchPlayerBySalaryInClub(minSalary, maxSalary));
            }
        }
        willShowTheResult = true;
        return answer;
    }

    @FXML
    void resetInput() {
        PlayerName.setText("");
        CountryName.setText("");
        PositionChoice.setText("Position");
        MinSalary.setText("");
        MaxSalary.setText("");
    }

    @FXML
    void showSearchResults(ActionEvent event) throws IOException {
        var wantedList = doTheSearch();
        if (!willShowTheResult) return;
        if (wantedList != null && !wantedList.isEmpty()) {
            main.currentPageType = CurrentScene.Type.ShowSearchedPlayers;
            main.latestSearchedPlayers = wantedList;
//            main.displayList(wantedList, PlayerListViewController.PageType.SimpleList);
            main.showSearchedPlayers(wantedList, new Stage());
        }
        else{
            new MyAlert(Alert.AlertType.INFORMATION, MyAlert.MessageType.NoPlayerFound).show();
        }
    }

    @FXML
    void showCountByCountry() throws IOException {
        main.showCountryWiseCount(new Stage(), club);
    }

    @FXML
    public void showMaximumAgePlayers() throws IOException {
        main.latestSearchType = Type.MaxAge;
        var wantedList = club.SearchMaximumAge();
//        main.displayList(wantedList, PlayerListViewController.PageType.SimpleList);
        main.showSearchedPlayers(wantedList, new Stage());
    }

    @FXML
    public void showMaximumHeightPlayers() throws IOException {
        main.latestSearchType = Type.MaxHeight;
        var wantedList = club.SearchMaximumHeight();
//        main.displayList(wantedList, PlayerListViewController.PageType.SimpleList);
        main.showSearchedPlayers(wantedList, new Stage());
    }

    @FXML
    public void showMaximumSalaryPlayers() throws IOException {
        main.latestSearchType = Type.MaxSal;
        var playerlistofMaxSal = club.SearchMaximumSalary();
        main.showSearchedPlayers(playerlistofMaxSal, new Stage());
    }

    @FXML
    public void showTotalAnnualSalary() {
        main.latestSearchType = Type.TotalSalary;
        var alert = new MyAlert(Alert.AlertType.INFORMATION, MyAlert.MessageType.TotalAnnualSalary);
        alert.setHeaderText("Total Annual Salary of " + club.getName());
        alert.setContentText("Total Annual Salary is " + Club.showCurrency(club.TotalYearlySalary()));
        alert.show();
        main.latestAlert = alert;
    }

    @FXML
    public void setAnyPosition(){ PositionChoice.setText("Any");}
    @FXML
    public void setBatsman(){ PositionChoice.setText("Batsman");}
    @FXML
    public void setBowler(){ PositionChoice.setText("Bowler");}
    @FXML
    public void setAllrounder(){ PositionChoice.setText("All-rounder");}
    @FXML
    public void setWicketkeeper(){ PositionChoice.setText("Wicketkeeper");}

    public void initiate(Club c) {
        club = c;
        clubName.setText(c.getName());
        clubLogo.setImage(main.cLogo);
    }
}