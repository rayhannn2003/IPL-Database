package com.example.ipldatabase.DataModel;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.Serializable;
import java.util.Objects;


public class Player implements Serializable {
    private final String Name;
    private final String Country;
    private final int age;
    private final double Height;

    private String Club;
    private final String Position;
    private int number;
    private final int salary;
    private boolean isTransferListed;
    double transferFee;
    String imageSource;
    double estimatedValue;
    public Player(String Name,String Country,int age,double height,String Club,String Position,int number,int salary,boolean isTransferListed,double transferFee,String imageSource,double estimatedValue)
    {
        this.Name = Name;
        this.Country = Country;
        this.age = age;
        this.Height = height;
        this.Club = Club;
        this.Position = Position;
        this.number = number;
        this.salary = salary;
        this.isTransferListed = isTransferListed;
        this.transferFee = transferFee;
        this.imageSource = imageSource;
        this.estimatedValue = estimatedValue;
    }
    public Player(Player player)
    {
        this.Name = player.Name;
        this.Country = player.Country;
        this.age = player.age;
        this.Height = player.Height;
        this.Club = player.Club;
        this.Position = player.Position;
        this.number = player.number;
        this.salary = player.salary;
        this.isTransferListed = player.isTransferListed;
        this.transferFee = player.transferFee;
        this.imageSource = player.imageSource;
        this.estimatedValue = player.estimatedValue;
    }
    //setters and setters
    //getters and setters
    public String getName()
    {
        return Name;
    }
    public String getCountry()
    {
        return Country;
    }
    public int getAge()
    {
        return age;
    }
    public double getHeight()
    {
        return Height;
    }
    public String getClub()
    {
        return Club;
    }
    public String getPosition()
    {
        return Position;
    }
    public int getNumber()
    {
        return number;
    }
    public int getSalary()
    {
        return salary;
    }
    public boolean isTransferListed() {
        return isTransferListed;
    }

    public String getImageSource() {
        return imageSource;
    }

    //to String
    public String toString()
    {
        return "Name: "+Name+"\nCountry: "+Country+"\nAge: "+age+"\nHeight: "+Height+"\nClub: "+Club+"\nPosition: "+Position+"\nNumber: "+number+"\nWeekly Salary: "+salary +"\nTransfer Listed: "+isTransferListed+"\nTransfer Fee: "+transferFee +"\nImage Source: "+imageSource +"\nEstimated Value: "+estimatedValue;
    }

    public Node getImageView() {
        // Path to the image: Ensure the image is placed in the resources folder
        String name = getName();
        String path = name+".jpg"; // Relative path within the resources directory

        // Attempt to load the image using getClass().getResourceAsStream()
        Image image;
        try {
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
            System.err.println("Image not found at: " + path);
            e.printStackTrace();
            return null; // Return null if the image cannot be found
        }

        // Create ImageView and configure dimensions
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100); // Set desired height
        imageView.setFitWidth(100);  // Set desired width
        imageView.setPreserveRatio(true); // Maintain aspect ratio
        return imageView;
    }

    public void setClub(String name) {
        this.Club = name;
    }

    public void setTransferListed(boolean transferListed) {
        isTransferListed = transferListed;
    }

    public void setNumber(int jerseyNumber) {
        this.number = jerseyNumber;
    }

    public double getTransferFee() {

        return transferFee;
    }



    public synchronized int isTransferPossible(Club buyer) { //returns 0 on success, 1 on already bought, 2 on insufficient budget
        if (!isTransferListed) return 1;
        if (buyer.getTransferBudget() < transferFee) return 2;
        return 0;
    }
    public void showDetails() {
        System.out.println("Name: " + Name);
        System.out.println("Country: " + Country);
        System.out.println("Age: " + age);
        System.out.println("Height: " + Height);
        System.out.println("Club: " + Club);
        System.out.println("Position: " + Position);
        System.out.println("Number: " + number);
        System.out.println("Weekly Salary: " + salary);
        //System.out.println("Transfer Listed: " + isTransferListed);
        //System.out.println("Transfer Fee: " + transferFee);
    }

    public double getEstimatedValue() {

    return  estimatedValue;
    }

    public String getClubName() {
        return Club;
    }

    public void setTransferFee(double transferFee) {
        this.transferFee = transferFee;
    }

    public void setClubName(String name) {
        this.Club = name;
    }
}

