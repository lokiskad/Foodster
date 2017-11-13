package com.example.brian.foodsterredesign1;

/**
 * Created by Brian on 10.09.2017.
 */

public class User {

    public String name;
    public String surname;
    public String uniqueID;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String uniqueID, String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.uniqueID = uniqueID;
    }

    public String getUniqueID() { return uniqueID; }
    public String getName() { return name; }
    public String getSurname() { return surname; }

    public void setUniqueID(String uniqueID) { this.uniqueID = uniqueID; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }


}
