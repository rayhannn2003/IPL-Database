package com.example.ipldatabase.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClubDataLoader {

    private Map<String, String> clubPasswordList = new HashMap<>();

    public void loadClubData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String clubName = parts[0].strip();
                    String username = parts[1].strip();
                    String password = parts[2].strip();
                    clubPasswordList.put(username, password);
                    System.out.println("Loaded club: " + clubName + " with username: " + username);
                } else {
                    System.err.println("Invalid line format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getClubPasswordList() {
        return clubPasswordList;
    }

    public static void main(String[] args) {
        ClubDataLoader loader = new ClubDataLoader();
        loader.loadClubData("src/main/java/com/example/ipldatabase/Asset/Data/loginInfo.txt");
        System.out.println(loader.getClubPasswordList());
    }
}