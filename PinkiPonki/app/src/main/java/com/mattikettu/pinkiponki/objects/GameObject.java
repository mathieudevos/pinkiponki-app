package com.mattikettu.pinkiponki.objects;

import java.util.ArrayList;

/**
 * Created by MD on 13/06/2016.
 */

public class GameObject {

    String teamA_player1;

    String teamA_player2;

    String teamB_player1;

    String teamB_player2;

    int teamA_score;

    int teamB_score;

    String author;

    ArrayList<String> verification;

    boolean verified;

    String timestamp;

    String about;

    public String getTeamA_player1() {
        return teamA_player1;
    }

    public void setTeamA_player1(String teamA_player1) {
        this.teamA_player1 = teamA_player1;
    }

    public String getTeamA_player2() {
        return teamA_player2;
    }

    public void setTeamA_player2(String teamA_player2) {
        this.teamA_player2 = teamA_player2;
    }

    public String getTeamB_player1() {
        return teamB_player1;
    }

    public void setTeamB_player1(String teamB_player1) {
        this.teamB_player1 = teamB_player1;
    }

    public String getTeamB_player2() {
        return teamB_player2;
    }

    public void setTeamB_player2(String teamB_player2) {
        this.teamB_player2 = teamB_player2;
    }

    public int getTeamA_score() {
        return teamA_score;
    }

    public void setTeamA_score(int teamA_score) {
        this.teamA_score = teamA_score;
    }

    public int getTeamB_score() {
        return teamB_score;
    }

    public void setTeamB_score(int teamB_score) {
        this.teamB_score = teamB_score;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getVerification() {
        return verification;
    }

    public void setVerification(ArrayList<String> verification) {
        this.verification = verification;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
