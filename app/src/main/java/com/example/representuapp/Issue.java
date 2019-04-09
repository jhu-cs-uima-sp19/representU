package com.example.representuapp;

import java.util.UUID;

public class Issue {
    public UUID idNum;
    public String title;
    public String summary;
    public int votesYay;
    public int votesNay;

    public Issue(String header, String sum) {
        this.idNum = UUID.randomUUID();
        this.title = header;
        this.summary = sum;
        this.votesYay = 0;
        this.votesNay = 0;
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
}
