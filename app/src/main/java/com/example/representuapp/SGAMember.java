package com.example.representuapp;

public class SGAMember {
    String name;
    String position;
    String bio;

    public SGAMember(String name, String position) {
        this.name = name;
        this.position = position;
        this.bio = "empty";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


}
