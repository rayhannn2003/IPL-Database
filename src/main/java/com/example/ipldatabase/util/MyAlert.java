package com.example.ipldatabase.util;

import javafx.scene.control.Alert;

public class MyAlert extends Alert {
    public enum MessageType {
        InvalidSalaryInput, NoPlayerFound, NoInput, TotalAnnualSalary, InvalidTransferFee
    }

    MessageType mType;

    public MyAlert(AlertType alertType, String HeaderText, String ContentText) {
        super(alertType);
        this.setHeaderText(HeaderText);
        this.setContentText(ContentText);
    }

    public MyAlert(AlertType alertType, MessageType mType) {
        super(alertType);
        this.mType = mType;

        switch (mType) {
            case InvalidSalaryInput:
                this.setHeaderText("Invalid salary input");
                this.setContentText("Please input some decimal value for salary limits");
                break;
            case NoPlayerFound:
                this.setHeaderText("No match");
                this.setContentText("No such player found!");
                break;
            case NoInput:
                this.setHeaderText("No input");
                this.setContentText("Please input some choices");
                break;
            case TotalAnnualSalary:
                this.setHeaderText("Total Annual Salary");
            case InvalidTransferFee:
                this.setHeaderText("Invalid Transfer Fee Input");
                this.setContentText("Please input a positive decimal value for transfer fee");
                break;
        }
    }

}