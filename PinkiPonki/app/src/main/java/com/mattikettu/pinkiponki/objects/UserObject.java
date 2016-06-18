package com.mattikettu.pinkiponki.objects;

import java.util.ArrayList;

/**
 * Created by mathieu on 09/06/2016.
 */
public class UserObject {

    String username;

    String password;

    String email;

    String firstName;

    String lastName;

    String about;

    int rating;

    int maxRating;

    ArrayList<String> friends;

    ArrayList<String> clubs;

    ArrayList<String> games;

    ArrayList<String> friendsTimeline;

    String created;

    String lastSeen;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getFriendsTimeline() {
        return friendsTimeline;
    }

    public void setFriendsTimeline(ArrayList<String> friendsTimeline) {
        this.friendsTimeline = friendsTimeline;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
