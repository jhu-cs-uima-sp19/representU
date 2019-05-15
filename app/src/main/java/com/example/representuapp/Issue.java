package com.example.representuapp;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;



public class Issue {
    public String idNum;
    public String title;
    public String summary;
    //public String whyVoteYay;
    //public String whyVoteNay;
    public int votesYay;
    public int votesNay;
    private Instant createTime;
    //private List<Comment> comments; // lists of comments made by users
    public List<String> usersYay; // Users that voted yay
    public List<String> usersNay; // Users that voted nay
    public List<Comment> comments; // lists of comments made by users
    public boolean archived;

    public Issue() {
        // Default constructor required for calls to DataSnapshot.getValue(Issue.class)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Issue(String header, String sum) {
        this.idNum = UUID.randomUUID().toString();
        this.title = header;
        //this.whyVoteNay = whyVoteNay;
        //this.whyVoteYay = whyVoteYay;
        this.summary = sum;
        this.votesYay = 0;
        this.votesNay = 0;
        this.usersNay = new ArrayList<>();
        this.usersYay = new ArrayList<>();
        //this.comments = new ArrayList<>();
        this.createTime = Instant.now();
        this.archived = false;
        //Firebase wont add an empty arrayList, adding empty strings
        this.usersNay.add("");
        this.usersYay.add("");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Issue(String header, String sum, String idCopy) {
        this.idNum = UUID.fromString(idCopy).toString();
        this.title = header;
        //this.whyVoteNay = whyVoteNay;
        //this.whyVoteYay = whyVoteYay;
        this.summary = sum;
        this.votesYay = 0;
        this.votesNay = 0;
        this.usersNay = new ArrayList<>();
        this.usersYay = new ArrayList<>();
        //this.comments = new ArrayList<>();
        this.createTime = Instant.now();
        this.archived = false;
        //Firebase wont add an empty arrayList, adding empty strings
        this.usersNay.add("");
        this.usersYay.add("");
    }

    public void changeTitle(String newTitle) {
        this.summary = newTitle;
    }

    public void changeSummary(String newSummary) {
        this.summary = newSummary;
    }

    public void addYay(String userName) {
        this.votesYay++;
        this.usersYay.add(userName);
    }

    public void addNay(String userName) {
        this.votesNay++;
        this.usersNay.add(userName);
    }

    public void deleteYay(String userName) {
        this.votesYay--;
        this.usersYay.remove(userName);
    }

    public void deleteNay(String userName) {
        this.votesNay--;
        this.usersNay.remove(userName);
    }

    public List<String> getUsersYay(){
        return this.usersYay;
    }

    public List<String> getUsersNay(){
        return this.usersNay;
    }

    //public void addComments(Comment e) {
      //  comments.add(e);
    //}

    /** Returns the creation time of this User. */
    private Instant getCreationTime() {
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

}

