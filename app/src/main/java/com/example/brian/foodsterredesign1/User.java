package com.example.brian.foodsterredesign1;

/**
 * Created by Brian on 10.09.2017.
 */

public class User {

    public String name;
    public String geb;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String name, String geb) {
        this.name = name;
        this.geb = geb;
    }
}
