package com.example.nguyenthikieutrang_bai3_module3;

public class Employee {
    private String id;
    private String title;
    private String name;
    private String phone;

    public Employee(String id, String title, String name, String phone) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}