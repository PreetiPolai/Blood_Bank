package com.android.BloodBank.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileData {

    private String UserId;
    private String UserName;


    private String UserPhoneNo;
    private String UserAge;
    private String UserWeight;
    private String UserBloodGroup;
    private String UserLastBloodDonationDate;
    private HashMap<String,String> UserAddress;
    private String UserImage;

    public UserProfileData(){

    }

    public UserProfileData(String userName, String userPhoneNo, String userAge, String userWeight, String userBloodGroup, String userLastBloodDonationDate, HashMap<String, String> userAddress, String userImage, String ID) {
        UserName = userName;
        UserPhoneNo = userPhoneNo;
        UserAge = userAge;
        UserWeight = userWeight;
        UserBloodGroup = userBloodGroup;
        UserLastBloodDonationDate = userLastBloodDonationDate;
        UserAddress = userAddress;
        UserImage = userImage;
        UserId = ID;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserPhoneNo() {
        return UserPhoneNo;
    }

    public String getUserAge() {
        return UserAge;
    }

    public String getUserWeight() {
        return UserWeight;
    }

    public String getUserBloodGroup() {
        return UserBloodGroup;
    }

    public String getUserLastBloodDonationDate() {
        return UserLastBloodDonationDate;
    }

    public HashMap<String, String> getUserAddress() {
        return UserAddress;
    }

    public String getUserImage() {
        return UserImage;
    }

    public String getUserId() {
        return UserId;
    }




    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        UserPhoneNo = userPhoneNo;
    }

    public void setUserAge(String userAge) {
        UserAge = userAge;
    }

    public void setUserWeight(String userWeight) {
        UserWeight = userWeight;
    }

    public void setUserBloodGroup(String userBloodGroup) {
        UserBloodGroup = userBloodGroup;
    }

    public void setUserLastBloodDonationDate(String userLastBloodDonationDate) {
        UserLastBloodDonationDate = userLastBloodDonationDate;
    }

    public void setUserAddress(HashMap<String, String> userAddress) {
        UserAddress = userAddress;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
