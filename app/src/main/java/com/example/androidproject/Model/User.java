package com.example.androidproject.Model;

public class User {
    private  String id;
    private  String username;
    private  String email;
    private String status;
    private String search;

    public User() {
    }

    public User(String id, String username, String email,String status,String search) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
