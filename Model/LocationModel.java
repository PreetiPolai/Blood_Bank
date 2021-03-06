package com.android.BloodBank.Model;

import java.util.HashMap;

public class LocationModel {
    private String Address;
    private HashMap<String,Object> Location;
    String id;
    String name;
    String Status;

    public LocationModel() {
    }

    public LocationModel(String address, HashMap<String, Object> location, String id, String name, String status) {
        this.Address = address;
        Location = location;
        this.id = id;
        this.name = name;
        Status = status;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public HashMap<String, Object> getLocation() {
        return Location;
    }

    public void setLocation(HashMap<String, Object> location) {
        Location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
