package com.example.smarthospital.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "rooms")
public class Room {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String type;
    private String status;

    // Constructor rỗng cho Room
    public Room() {
    }

    // Constructor đầy đủ cho logic ứng dụng
    @Ignore
    public Room(String name, String type, String status) {
        this.name = name;
        this.type = type;
        this.status = status;
    }

    // Getter và Setter
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}