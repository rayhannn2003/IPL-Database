package com.example.ipldatabase.Author;

import com.example.ipldatabase.SecretData.*;
import com.example.ipldatabase.DataModel.Club;
import com.example.ipldatabase.DataModel.League;
import com.example.ipldatabase.DataModel.Player;

import javafx.util.Pair;
import com.example.ipldatabase.util.FileOperations;
import com.example.ipldatabase.util.NetworkUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ReadThreadServer implements Runnable{
    private Thread t;
    private NetworkUtil networkUtil;
    private League league;
    private Club club;
    private HashMap<String, String> clubPasswordList;
    private HashMap<String, NetworkUtil> clubNetworkUtilMap;
    private ArrayList<Player> transferListedPlayers;
    private ArrayList<Pair<String, String>> countryFlagList;
    private ArrayList<Pair<String, String>> clubLogoList;
    private HashMap<String, String> unaccented_accented;
    private boolean isThreadOn = true;
    private boolean hasAddedToMap = false;

    public ReadThreadServer(NetworkUtil networkUtil, League league, ArrayList<Player> transferListedPlayers, HashMap<String, String> clubPasswordList, HashMap<String, NetworkUtil> clubNetworkUtilMap, ArrayList<Pair<String, String>> countryFlagList, ArrayList<Pair<String, String>> clubLogoList, HashMap<String, String> unaccented_accented) {
        this.networkUtil = networkUtil;
        this.league = league;
        this.transferListedPlayers = transferListedPlayers;
        this.clubPasswordList = clubPasswordList;
        this.clubNetworkUtilMap = clubNetworkUtilMap;
        this.clubLogoList = clubLogoList;
        this.countryFlagList = countryFlagList;
        this.unaccented_accented = unaccented_accented;
        t = new Thread(this, "Read Thread Server");
        t.start();
    }

    @Override
    public void run() {
        try {
            networkUtil.write(new ClubCountryImageData(countryFlagList, clubLogoList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Club object and country list has been sent");
        while (isThreadOn){
            Object next = null;
            while (isThreadOn){
                try {
                    next = networkUtil.read();
                    break;
                } catch (IOException | ClassNotFoundException ignored){}
            }
            if (next instanceof ClubLoginInformation){
                String username = ((ClubLoginInformation) next).getUsername().strip();
                String password = ((ClubLoginInformation) next).getPassword();
                System.out.println("New login request:\nUsername: " + username + " Password: " + password);
                // Check if the user is already logged in to the network or not and if the user is already registered or not    
                if (clubNetworkUtilMap != null && clubNetworkUtilMap.containsKey(username)){
                    try {
                        networkUtil.write(new RequestResponse(RequestResponse.Type.AlreadyLoggedIn));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("This user is already logged in to the network");
                }
                else if (clubPasswordList.containsKey(username)){
                    if (clubPasswordList.get(username).equals(password)) {
                        try {
                            networkUtil.write(new RequestResponse(RequestResponse.Type.LoginSuccessful));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        clubNetworkUtilMap.put(username, networkUtil);
                        hasAddedToMap = true;
                        club = league.FindClub(username);
                        if (club == null) {
                            System.err.println("Club not found: " );
                            // Handle the error, e.g., throw an exception or return a default value.
                        }
                        club.setPassword(password);
                        try {
                            networkUtil.write(club);
//                       
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(" Club Login successful.");
                        try {
                            var newTransferList = new UpdatedTransferList(transferListedPlayers, club.getName());
                            networkUtil.write(newTransferList);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Updated transfer list has been sent.");
                    }
                    else{
                        try {
                            networkUtil.write(new RequestResponse(RequestResponse.Type.IncorrectPassword));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Incorrect password");
                    }
                }
                else{
                    try {
                        networkUtil.write(new RequestResponse(RequestResponse.Type.UsernameNotRegistered));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Username not registered on server");
                }
            }
            else if (next instanceof PasswordChange){
                PasswordChange pc = (PasswordChange) next;
                var c = league.FindClub(pc.getClubName());
                System.out.println("Received a request from " + c.getName() + " to change password to " + pc.getNewPassword());
                c.setPassword(pc.getNewPassword());//loading the new password
                //loading the club information to the file
                try { String fileName = "E:\\JavaFX Project\\IPL Database\\src\\main\\java\\com\\example\\ipldatabase\\Asset\\Data\\loginInfo.txt";
                    FileOperations.writeClubPasswords(fileName, league.getClubList());
                    clubPasswordList.put(pc.getClubName(), pc.getNewPassword());
                    networkUtil.write(new RequestResponse(RequestResponse.Type.PasswordChangeSuccessful));
                    System.out.println("Password change has been successful");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (next instanceof BuyRequest){
                System.out.println(((BuyRequest) next).getPotentialBuyerClub() + " wants to buy " + ((BuyRequest) next).getPlayerName() + " from " + ((BuyRequest) next).getPlayerCurrentClub());
                var p = league.FindPlayer(((BuyRequest) next).getPlayerName());
                var buyer = league.FindClub(((BuyRequest) next).getPotentialBuyerClub());
                var seller = league.FindClub(((BuyRequest) next).getPlayerCurrentClub());
                int checkStatus = p.isTransferPossible(buyer);
                if (checkStatus == 0){ //possible sale and sufficient budget
                    System.out.println(((BuyRequest) next).getPlayerName() + " is currently indeed up for sale");
                    System.out.println("Sufficient budget. Processing buying request....");
                    league.transferPlayerToNewClub(p, seller, buyer);
                    transferListedPlayers.remove(p);
                    System.out.println("Player purchase successful. All budget update done");
                    for (var c : clubNetworkUtilMap.entrySet()){
                        if (buyer.getName().equalsIgnoreCase(c.getKey()) || seller.getName().equalsIgnoreCase(c.getKey())) continue;
                        try {
                            c.getValue().write(new UpdatedTransferList(transferListedPlayers, "all"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Updated list has been delivered to all active clubs");
                    try {
                        var newInfo = new NewPlayerPurchased(p, buyer.getName(), seller.getName());
                        networkUtil.write(newInfo);
                        var sellerNetworkUtil = clubNetworkUtilMap.get(seller.getName());
                        if (sellerNetworkUtil != null) sellerNetworkUtil.write(newInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (checkStatus == 1){ //already bought
                    System.out.println(p.getName() + " has already been bought by " + p.getClubName());
                    try {
                        networkUtil.write(new RequestResponse(RequestResponse.Type.AlreadyBought));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{ //inadequate transfer fee
                    System.out.println(((BuyRequest) next).getPlayerName() + " is currently indeed up for sale");
                    System.out.println("Insufficient transfer fee");
                    try {
                        networkUtil.write(new RequestResponse(RequestResponse.Type.InsufficientTransferBudget));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (next instanceof SellRequest){
                var that = (SellRequest)next;
                var p = league.FindPlayer(that.getPlayerName());
                System.out.println("New sell request from " + p.getClubName() + " for " + that.getTransferFee());
                p.setTransferListed(true);
                p.setTransferFee(that.getTransferFee());
                transferListedPlayers.add(p);
                var newList = new UpdatedTransferList(transferListedPlayers, "all");
                System.out.println("Updated transfer list is being sent to all active clubs:");
                for (var activeClubs : clubNetworkUtilMap.entrySet()){
                    if (activeClubs.getKey().equalsIgnoreCase(club.getName())) continue;
                    try {
                        activeClubs.getValue().write(newList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if (next instanceof Request){
                if (((Request) next).requestType == Request.Type.UpdatedListQuery) {
                    System.out.println("Received a request from " + ((Request) next).getFrom() + " to avail the updated transfer list");
                    var newList = new UpdatedTransferList(transferListedPlayers, ((Request) next).getFrom());
                    for (var p : newList.getPlayerList()) p.showDetails();
                    try {
                        networkUtil.write(newList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Updated list has been delivered on request to " + ((Request) next).getFrom());
                }
                else if (((Request) next).requestType == Request.Type.LogOut){
                    clubNetworkUtilMap.remove(((Request) next).getFrom());
                    System.out.println(((Request) next).getFrom() + " has been successfully logged out from the network");
                    isThreadOn = false;
                }
                else if (((Request) next).requestType == Request.Type.IAmOut){
                    if (hasAddedToMap){
                        hasAddedToMap = false;
                        clubNetworkUtilMap.remove(((Request) next).getFrom());
                    }
                    isThreadOn = false;
                }
            }
        }
        try {
            networkUtil.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("Quitting from this read thread server: ");
        if (club != null) System.out.print(" " + club.getName());
        System.out.println();
    }
}
