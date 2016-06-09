package com.mattikettu.pinkiponki.objects;

import java.util.ArrayList;

/**
 * Created by mathieu on 09/06/2016.
 */
public class UserObject {

    String username;

    String password;

    String email;

    String firstname;

    String lastname;

    String about;

    int rating;

    ArrayList<String> clubs;

    ArrayList<String> games;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public ArrayList<String> getClubs() {
        return clubs;
    }

    public void setClubs(ArrayList<String> clubs) {
        this.clubs = clubs;
    }

    public ArrayList<String> getGames() {
        return games;
    }

    public void setGames(ArrayList<String> games) {
        this.games = games;
    }
}
