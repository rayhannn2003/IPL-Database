package com.example.ipldatabase.Controllers;

import com.example.ipldatabase.Author.Main;
import com.example.ipldatabase.DataModel.Player;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class PlayerListViewController {
    public GridPane playerGrid;
    private Main main;

    public enum PageType {
        SimpleList, TransferList
    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private ListView<VBox> listView;

    public void initiate(List<Player> players, PageType pageType) throws IOException {
        System.out.println(players);
        for (var p : players) {
            var newLoader = new FXMLLoader();// loader.setLocation(getClass().getResource("));
            newLoader.setLocation(getClass().getResource("/com/example/ipldatabase/FXML/MinimalPlayerDetailView.fxml"));
            newLoader.load();
            MinimalPlayerDetailController pDetail = newLoader.getController();
            pDetail.setMain(main);
            listView.getItems().add(pDetail.initiate(p, pageType));
        }
    }

}