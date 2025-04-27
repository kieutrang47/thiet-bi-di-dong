package com.example.smarthospital.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicines")
public class Medicine {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int quantity;
    private String expiryDate;
    private double price;

    public Medicine() {
    }

    @Ignore
    public Medicine(String name, int quantity, String expiryDate, double price) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.price = price;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

//package com.example.smarthospital.model;
//
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//
//@Entity(tableName = "medicines")
//public class Medicine {
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//    private String name;
//    private int quantity;
//    private String expiryDate;
//    private double price;
//
//    @Ignore
//    public Medicine() {
//    }
//
//    public Medicine(String name, int quantity, String expiryDate, double price) {
//        this.name = name;
//        this.quantity = quantity;
//        this.expiryDate = expiryDate;
//        this.price = price;
//    }
//
//    // Getters and setters
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public String getExpiryDate() {
//        return expiryDate;
//    }
//
//    public void setExpiryDate(String expiryDate) {
//        this.expiryDate = expiryDate;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//}