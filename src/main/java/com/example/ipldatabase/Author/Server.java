package com.example.ipldatabase.Author;

import com.example.ipldatabase.DataModel.League;
import com.example.ipldatabase.DataModel.Player;
import com.example.ipldatabase.util.ClubDataLoader;
import com.example.ipldatabase.util.FileOperations;
import com.example.ipldatabase.util.NetworkUtil;

import javafx.util.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    private ServerSocket serverSocket;
    public static League indianPremierLeage;
    private HashMap<String, String> clubPasswordList;
    private HashMap<String, NetworkUtil> clubNetworkUtilMap;
    private ArrayList<Player> transferListedPlayers;
    private ArrayList<Pair<String, String>> countryFlagList;
    private ArrayList<Pair<String, String>> clubLogoList;
    private HashMap<String, String> unaccented_accented;

    public Server(int port) throws Exception {
        indianPremierLeage = new League();
        clubNetworkUtilMap = new HashMap<>();
        transferListedPlayers = new ArrayList<>();
        serverSocket = new ServerSocket(port);
        unaccented_accented = new HashMap<>();
        // reading the unaccented and accented characters from the file and storing it in a hashmap for future use
        String filename = "E:\\JavaFX Project\\IPL Database\\src\\main\\java\\com\\example\\ipldatabase\\Asset\\Data\\players.txt";
        var loaded = FileOperations.loadFromfile(filename); 
        for (var p : loaded) {
            indianPremierLeage.addPlayerToLeague(p);
            if (p.isTransferListed())
                transferListedPlayers.add(p);
        }
        System.out.println("Loaded data of " + indianPremierLeage.CentralPlayerDatabase.size() + " players and "
                + indianPremierLeage.getClubList().size() + " clubs");
        // Reading all the club passwords
        //function for all players data
        String fileofClubPasswords = "E:\\JavaFX Project\\IPL Database\\src\\main\\java\\com\\example\\ipldatabase\\Asset\\Data\\loginInfo.txt";
        ClubDataLoader loader = new ClubDataLoader();
        loader.loadClubData(fileofClubPasswords);
        clubPasswordList = new HashMap<>(loader.getClubPasswordList());
        System.out.println("Loaded login credentials of " + clubPasswordList.size() + " clubs");

        clubPasswordList = FileOperations.readCredentialsOfClubs(fileofClubPasswords, indianPremierLeage);
        System.out.println("Loaded login credentials of " + clubPasswordList.size() + " clubs");
        // Reading Country Data
        String countryFlagFIle = "E:\\JavaFX Project\\IPL Database\\src\\main\\java\\com\\example\\ipldatabase\\Asset\\Data\\flagImage.txt";
        countryFlagList = FileOperations.readFlagLinkOfCountries(countryFlagFIle);
        System.out.println("Loaded flags of " + countryFlagList.size() + " countries");
        // Reading Club Data
        String fileofClubDatabase = "E:\\JavaFX Project\\IPL Database\\src\\main\\java\\com\\example\\ipldatabase\\Asset\\Data\\ClubDatabase.txt";
        clubLogoList = FileOperations.readInformationOfClubs(fileofClubDatabase, indianPremierLeage,
                unaccented_accented);
    
        System.out.println("Server On and running");// Server is running
    }

    public void serve(Socket clientSocket) throws IOException {// Server is serving
        System.out.println("Server accepts a new socket");
        var networkUtil = new NetworkUtil(clientSocket);
        new ReadThreadServer(networkUtil, indianPremierLeage, transferListedPlayers, clubPasswordList,
                clubNetworkUtilMap, countryFlagList, clubLogoList, unaccented_accented);
    }

    public static void main(String[] args) throws Exception {
        int port = 44444;
        Server server = new Server(port);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String next;
            // Saving the data to the file
            //player data and club data
            String filename = "E:\\JavaFX Project\\IPL Database\\src\\main\\java\\com\\example\\ipldatabase\\Asset\\Data\\players.txt";
            String filofClubLogin = "E:\\JavaFX Project\\IPL Database\\src\\main\\java\\com\\example\\ipldatabase\\Asset\\Data\\ClubDatabase.txt";
            while (true) {
                next = scanner.nextLine();
                if (next.strip().equalsIgnoreCase("Stop")) {
                    try {
                        FileOperations.writePlayerDataToFile(filename, indianPremierLeage.CentralPlayerDatabase);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        FileOperations.writeClubInformationToFile(filofClubLogin, indianPremierLeage.getClubList());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("Good Bye");
                    try {
                        server.serverSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        }).start();
        while (true) {
            var cs = server.serverSocket.accept();
            server.serve(cs);
        }
    }
}