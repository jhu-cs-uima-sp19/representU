package com.example.representuapp;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Issue {
    public Instant createTime;
    public UUID idNum;
    public String title;
    public String summary;
    public int votesYay;
    public int votesNay;
    public ArrayList<Comment> comments; // lists of comments made by users
    public ArrayList<String> usersYay; // Users that voted yay
    public ArrayList<String> usersNay; // Users that voted nay
    public boolean archived;

    public Issue() {
        // Default constructor required for calls to DataSnapshot.getValue(Issue.class)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Issue(String header, String sum) {
        this.idNum = UUID.randomUUID();
        this.title = header;
        this.summary = sum;
        this.votesYay = 0;
        this.votesNay = 0;
        this.createTime = Instant.now();
        this.archived = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Issue(String header, String sum, String idCopy) {
        this.idNum = UUID.fromString(idCopy);
        this.title = header;
        this.summary = sum;
        this.votesYay = 0;
        this.votesNay = 0;
        this.createTime = Instant.now();
    }

    public void changeTitle(String newTitle) {
        this.summary = newTitle;
    }

    public void changeSummary(String newSummary) {
        this.summary = newSummary;
    }

    public void addYay(String userName) {
        this.votesYay++;
        this.addUser(usersYay, userName);
    }

    public void addNay(String userName) {
        this.votesNay++;
        this.addUser(usersNay, userName);
    }

    public void addUser(ArrayList<String> userList, String userName) {
        userList.add(userName);
    }

    public void addComments(Comment e) {
        comments.add(e);
    }

    public void archiveMe() {
        this.archived = !this.archived;
    }

    /** Returns the creation time of this User. */
    public Instant getCreationTime() {
        return createTime;
    }

    /** Returns Instant time in MM/DD/YYYY HH:MM:SS timeFormat. */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String timeFormat() {
        Date myDate = Date.from(this.getCreationTime());
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String formattedDate = formatter.format(myDate);
        return formattedDate;
    }

//    @Override
    /** Compares Activities based on their creation time.
     * Sorts latest first.
     */

  //  public int compareTo(Issue other) {
    //    return -(this.getCreationTime().compareTo(other.getCreationTime()));
    //}
}

