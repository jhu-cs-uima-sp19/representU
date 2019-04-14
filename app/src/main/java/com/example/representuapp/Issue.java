package com.example.representuapp;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class Issue {
    public Instant createTime;
    public UUID idNum;
    public String title;
    public String summary;
    public int votesYay;
    public int votesNay;

    public Issue() {
        // Default constructor required for calls to DataSnapshot.getValue(Issue.class)
    }

    public Issue(String header, String sum) {
        this.idNum = UUID.randomUUID();
        this.title = header;
        this.summary = sum;
        this.votesYay = 0;
        this.votesNay = 0;
        this.createTime = Instant.now();
    }

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

    public void addYay() {
        this.votesYay++;
    }

    public void addNay() {
        this.votesNay++;
    }

    /** Returns the creation time of this User. */
    public Instant getCreationTime() {
        return createTime;
    }

    /** Returns Instant time in MM/DD/YYYY HH:MM:SS timeFormat. */
    public String timeFormat() {
        Date myDate = Date.from(this.getCreationTime());
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
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

