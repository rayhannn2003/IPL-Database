package com.example.ipldatabase.DataModel;


import java.util.ArrayList;
import java.util.List;

public class League {
    public List<Player> CentralPlayerDatabase;
    private final List<Club> ClubList;
    private final List<Country> CountryList;
    private final List<Position> PositionList;

    public League() {
        CentralPlayerDatabase = new ArrayList<>();
        ClubList = new ArrayList<>();
        CountryList = new ArrayList<>();
        PositionList = new ArrayList<>();
    }

    public List<Club> getClubList() {
        return ClubList;
    }
//
//    public static String FormatName(String name) {
//        return name;
//    }

    public Player FindPlayer(String PlayerName) { //will return null if club is not registered yet
        Player wanted = null;
        String FormattedPlayerName = PlayerName;//FormatName(PlayerName)
        for (var p : CentralPlayerDatabase) {
            if (p.getName().equalsIgnoreCase(FormattedPlayerName)) {
                wanted = p;
                break;
            }
        }
        return wanted;
    }

    public Club FindClub(String ClubName) { //will return null if club is not registered yet
        Club findClub = null;
        //FormatName(ClubName)
        for (var c : ClubList) {
            if (c.getName().equalsIgnoreCase(ClubName)) {
                findClub = c;
                break;
            }
        }
        return findClub;
    }


    public Country FindCountry(String CountryName) { //will return null if club is not registered yet
        Country wanted = null;
        // FormatName(CountryName);
        for (var c : CountryList) {
            if (c.getName().equalsIgnoreCase(CountryName)) {
                wanted = c;
                break;
            }
        }
        return wanted;
    }

    public Position FindPosition(String PositionName) { //will return null if club is not registered yet
        Position wanted = null;
        //FormatName(PositionName)
        for (var c : PositionList) {
            if (c.getName().equalsIgnoreCase(PositionName)) {
                wanted = c;
                break;
            }
        }
        return wanted;
    }

    public void addPlayerToLeague(Player p) {
        //size check and player existence check is done in main
        var c = FindClub(p.getClub());
        if (c == null) {
            c = new Club(p.getClub());
            ClubList.add(c);
        }
        String CountryName = p.getCountry();//FormatName(p.getCountry());
        var country = FindCountry(CountryName);
        if (country == null) {
            country = new Country(CountryName);
            CountryList.add(country);
        }
        String PositionName = p.getPosition();//FormatName(p.getPosition());
        var position = FindPosition(PositionName);
        if (position == null) {
            position = new Position(PositionName);
            PositionList.add(position);
        }
        CentralPlayerDatabase.add(p);
        c.addPlayerToClub(p);
     //   c.addPlayerToClub(p);
        country.incrementCount();
        position.incrementCount();
    }
    public void removePlayerFromCurrentClub(Player p) {
        var c = FindClub(p.getClub());
        if (c != null) {
            c.getPlayerList().remove(p);
        } else {
            System.err.println("Club not found: " + p.getClub());
        }
    }

    public void transferPlayerToNewClub(Player p, Club before, Club after){
        removePlayerFromCurrentClub(p);
        p.setClub(after.getName());
        p.setTransferListed(false);
        var jerseyNumber = p.getNumber();
        while (after.NumberTaken.contains(jerseyNumber)) jerseyNumber++;
        p.setNumber(jerseyNumber);
        after.addPlayerToClub(p);
        before.increaseTransferBudget(p.getTransferFee());
        after.decreaseTransferBudget(p.getTransferFee());
    }

    public List<Country> getCountryList() {
        return CountryList;
    }
}