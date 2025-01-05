package com.example.ipldatabase.util;

import com.example.ipldatabase.DataModel.Club;
import com.example.ipldatabase.DataModel.League;
import com.example.ipldatabase.DataModel.Player;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.example.ipldatabase.DataModel.Club.decodeSalaryString;



public class FileOperations {
    //


    public static List<Player> loadFromfile(String filename) throws IOException {
         List<Player> PlyaerList = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            System.out
                    .println("players.txt file not found. Please make sure the file exists in the correct directoryIn PlayerDataBase");
            return PlyaerList;
        }
        BufferedReader br = new BufferedReader(new FileReader(filename));

        try (Scanner scanner = new Scanner(br)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length < 12)
                    continue;

                String name = parts[0];
                String Country = parts[1];
                int age = parts[2].isEmpty() ? -1 : Integer.parseInt(parts[2]);
                double Height = parts[3].isBlank() ? 0.0 : Double.parseDouble(parts[3]);
                String Club = parts[4];
                String Position = parts[5];
                int number = parts[6].isEmpty() ? -1 : Integer.parseInt(parts[6]);
                int salary = parts[7].isEmpty() ? -1 : Integer.parseInt(parts[7]);
                boolean isTransferListed = parts[8].equalsIgnoreCase("true");
                double transferFee = parts[9].isEmpty() ? 0.0 : Double.parseDouble(parts[9]);
                double estimatedValue = parts[10].isEmpty() ? 0.0 : Double.parseDouble(parts[10]);
                String imageSource = parts[11];
                //name , country, age ,Height,CLub,Position,number,salary,isTransferListed,transferFee,imageSource,estimatedValue
                Player player = new Player(name, Country, age, Height, Club, Position, number, salary, isTransferListed,
                        transferFee, imageSource,estimatedValue);
                PlyaerList.add(player);
            }

        }
        br.close();
        return PlyaerList;
    }

    //
    /*
    public static List<Player> readPlayerDataFromFile(String FILE_NAME) throws Exception {
        List<Player> playerList = new ArrayList<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "windows-1252"));
        while (true) {
            String line = input.readLine();
            if (line == null)
                break;
            String[] tokens = line.split(";");
            try {
//                String name, String unAccentedName, String clubName, String country, int age, double estimatedValue, int number, String position, double height, double weight, String pFoot, double weeklySalary, String imageSource, int isTransferListed, double transferFee
                Player p = new Player(tokens[0], tokens[1], tokens[2], tokens[3], Integer.parseInt(tokens[4]), Club.decodeSalaryString(tokens[5]), Integer.parseInt(tokens[6]), tokens[7], Double.parseDouble(tokens[8]), Double.parseDouble(tokens[9]), tokens[10], Club.decodeSalaryString(tokens[11]), tokens[12], Integer.parseInt(tokens[13]), Club.decodeSalaryString(tokens[14]));
                playerList.add(p);
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(line);
            }
        }
        input.close();
        return playerList;
    }
*/
    public static HashMap<String, String> readCredentialsOfClubs(String FILE_NAME, League league) throws IOException {
        HashMap<String, String> clubPasswords = new HashMap<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "windows-1252"));
        while (true) {
            String line = input.readLine();
            if (line == null) break;
            try {
                String[] tokens = line.split(";");
                String username = tokens[0];
                String password = tokens[2];
                clubPasswords.put(username, password);
                var c = league.FindClub(username);
                if (c != null){
                    c.setPassword(password);
                }
            } catch (Exception ignored) {
            }
        }
        input.close();
        return clubPasswords;
    }

    public static ArrayList<Pair<String, String>> readFlagLinkOfCountries(String FILE_NAME) throws IOException {
        ArrayList<Pair<String, String>> countryFlagList = new ArrayList<>();
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME)));
        while (true) {
            String line = input.readLine();
            if (line == null) break;
            try {
                String[] tokens = line.split(";");
                String name = tokens[1];
                String flagLink = tokens[2];
                countryFlagList.add(new Pair<>(name, flagLink));
            } catch (Exception ignored) {
            }
        }
        input.close();
        return countryFlagList;
    }
    public static ArrayList<Pair<String, String>> readInformationOfClubs(
            String fileName,
            League league,
            HashMap<String, String> unaccentedToAccented
    ) throws IOException {
        ArrayList<Pair<String, String>> clubLogoList = new ArrayList<>();

        // Use try-with-resources to ensure the reader is closed automatically
        try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "windows-1252"))) {
            String line;

            while ((line = input.readLine()) != null) {
                try {
                    // Split and validate line tokens
                    String[] tokens = line.split(";");
                    if (tokens.length < 5) {
                        System.err.println("Invalid data format: " + line);
                        continue;
                    }

                    // Extract and process data
                    String name = tokens[0].trim();
                    String unAccentedName = tokens[1].trim();
                    double worth = decodeSalaryString(tokens[2].trim());
                    double budget = decodeSalaryString(tokens[3].trim());
                    String logoLink = tokens[4].trim();

                    // Add to club logo list
                    clubLogoList.add(new Pair<>(name, logoLink));

                    // Find and update club in the league
                    var club = league.FindClub(name);
                    if (club != null) {
                        club.setWorth(worth);
                        club.setTransferBudget(budget);
                        club.setLogoLink(logoLink);
                        club.setUnAccentedName(unAccentedName);
                        unaccentedToAccented.put(unAccentedName, club.getName());
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            throw e; // Re-throw exception for higher-level handling
        }

        return clubLogoList;
    }


//    public static ArrayList<Pair<String, String>> readInformationOfClubs(String FILE_NAME, League league, HashMap<String, String> unaccented_accented) throws IOException {
//        ArrayList<Pair<String, String>> clubLogoList = new ArrayList<>();
//        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "windows-1252"));
//        while (true) {
//            String line = input.readLine();
//            if (line == null) break;
//            try {
//                String[] tokens = line.split(";");
//                String name = tokens[0];
//                String unAccentedName = tokens[1];
//                double worth = decodeSalaryString(tokens[2]);
//                double budget = decodeSalaryString(tokens[3]);
//                String logoLink = tokens[4];
//                clubLogoList.add(new Pair<>(name, logoLink));
//                var club = league.FindClub(name);
//                if (club != null) {
//                    club.setWorth(worth);
//                    club.setTransferBudget(budget);
//                    club.setLogoLink(logoLink);
//                    club.setUnAccentedName(unAccentedName);
//                    unaccented_accented.put(unAccentedName, club.getName());
//                }
//            } catch (Exception e) {
//                System.out.println(line);
//                e.printStackTrace();
//            }
//        }
//        input.close();
//        return clubLogoList;
//    }

    public static void writePlayerDataToFile(String FILE_NAME, List<Player> playerList) throws Exception {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), "windows-1252"));
        for (Player p : playerList) {
//           Virat Kohli,India,35,1.75,Royal Challengers Bangalore,Batsman,18,17000000,false,0,20000000,https://static.cricbuzz.com/a/img/v1/152x152/i1/c591944/virat-kohli.jpg
            output.write(p.getName() + "," + p.getCountry() + "," + p.getAge() + "," + p.getHeight() + "," + p.getClub() + "," + p.getPosition() + "," + p.getNumber() + "," + Club.showCurrency(p.getSalary()) + "," + p.isTransferListed() + "," + Club.showCurrency(p.getTransferFee()) + "," + Club.showCurrency(p.getEstimatedValue()) + "," + p.getImageSource());
            output.write("\n");
        }
        output.close();
    }

    public static void writeClubPasswords(String FILE_NAME, List<Club> clubList) throws Exception {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), "windows-1252"));
        for (Club c : clubList) {
            output.write(c.getName() + ";" + c.getUnAccentedName() + ";" + c.getPassword());
            output.write("\n");
        }
        output.close();
    }

    public static void writeClubInformationToFile(String FILE_NAME, List<Club> clubList) throws Exception {
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), "windows-1252"));
        for (Club c : clubList) {
            String buffer = c.getName() + ";" + c.getUnAccentedName() + ";" + Club.showCurrency(c.getWorth()) + ";" + Club.showCurrency(c.getTransferBudget()) + ";" + c.getLogoLink();
            output.write(buffer);
            output.write("\n");
        }
        output.close();
    }
}