package com.example.smarthospital.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "users", indices = {@Index(value = {"email"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private String fullName;

    @NonNull
    private String role; // "patient", "doctor", "admin"

    private String specialty; // Chỉ cho bác sĩ

    // Constructor rỗng yêu cầu bởi Room
    public User() {}

    // Constructor đầy đủ, bị bỏ qua bởi Room
    @Ignore
    public User(String email, String password, String fullName, String role, String specialty) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.specialty = specialty;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getEmail() { return email; }
    public void setEmail(@NonNull String email) { this.email = email; }

    @NonNull
    public String getPassword() { return password; }
    public void setPassword(@NonNull String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    @NonNull
    public String getRole() { return role; }
    public void setRole(@NonNull String role) { this.role = role; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}


//package com.example.smarthospital.model;
//
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.Index;
//import androidx.room.PrimaryKey;
//
////@Entity(tableName = "users")
//@Entity(tableName = "users", indices = {@Index(value = {"email"}, unique = true)})
//public class User {
//    @PrimaryKey(autoGenerate = true)
//    private int id;
//    private String email;
//    private String password;
//    private String fullName;
//    private String role; // "patient", "doctor", "admin"
//    private String specialty; // Chỉ cho bác sĩ
//
//    // Constructor rỗng yêu cầu bởi Room
//    public User() {}
//
//    // Constructor đầy đủ, bị bỏ qua bởi Room
//    @Ignore
//    public User(String email, String password, String fullName, String role, String specialty) {
//        this.email = email;
//        this.password = password;
//        this.fullName = fullName;
//        this.role = role;
//        this.specialty = specialty;
//    }
//
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//    public String getEmail() { return email; }
//    public void setEmail(String email) { this.email = email; }
//    public String getPassword() { return password; }
//    public void setPassword(String password) { this.password = password; }
//    public String getFullName() { return fullName; }
//    public void setFullName(String fullName) { this.fullName = fullName; }
//    public String getRole() { return role; }
//    public void setRole(String role) { this.role = role; }
//    public String getSpecialty() { return specialty; }
//    public void setSpecialty(String specialty) { this.specialty = specialty; }
//}