package com.android.BloodBank.Notifications;

public class Data {


    //this is the pure pojo class
    private String user;
    private int icon;
    private String Body;
    private String title;
    private String sent;

    public Data(String user, int icon, String body, String title, String sent) {
        this.user = user;
        this.icon = icon;
        Body = body;
        this.title = title;
        this.sent = sent;
    }

    public Data() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }
}
