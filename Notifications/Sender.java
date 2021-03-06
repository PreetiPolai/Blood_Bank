package com.android.BloodBank.Notifications;


//this object is passed as the body in the post of API service
public class  Sender {

    public Data data;
    public String to;


    public Sender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
