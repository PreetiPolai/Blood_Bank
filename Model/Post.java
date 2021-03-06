package com.android.BloodBank.Model;

public class Post {
    String date;
    String time;
    String Id;
    String postMessage;
    String Location;
    String BloodGroup;
    String Contact;

    public Post(String date, String time, String id, String postMessage, String location, String bloodGroup, String mContact) {
        this.date = date;
        this.time = time;
        Id = id;
        this.postMessage = postMessage;
        Location = location;
        BloodGroup = bloodGroup;
        Contact = mContact;
    }

    public Post() {
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getId() {
        return Id;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public String getLocation() {
        return Location;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }
}
