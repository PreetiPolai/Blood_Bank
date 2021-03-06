
package com.android.BloodBank.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("sr_no")
    @Expose
    private String srNo;
    @SerializedName("_blood_bank_name")
    @Expose
    private String bloodBankName;
    @SerializedName("__state")
    @Expose
    private String state;
    @SerializedName("__district")
    @Expose
    private String district;
    @SerializedName("__city")
    @Expose
    private String city;
    @SerializedName("__address")
    @Expose
    private String address;
    @SerializedName("_pincode")
    @Expose
    private String pincode;
    @SerializedName("__contact_no")
    @Expose
    private String contactNo;
    @SerializedName("_mobile")
    @Expose
    private String mobile;
    @SerializedName("_helpline")
    @Expose
    private String helpline;
    @SerializedName("__fax")
    @Expose
    private String fax;
    @SerializedName("_email")
    @Expose
    private String email;
    @SerializedName("__website")
    @Expose
    private String website;
    @SerializedName("__nodal_officer_")
    @Expose
    private String nodalOfficer;
    @SerializedName("_contact_nodal_officer")
    @Expose
    private String contactNodalOfficer;
    @SerializedName("_mobile_nodal_officer")
    @Expose
    private String mobileNodalOfficer;
    @SerializedName("__email_nodal_officer")
    @Expose
    private String emailNodalOfficer;
    @SerializedName("__qualification_nodal_officer")
    @Expose
    private String qualificationNodalOfficer;
    @SerializedName("__category")
    @Expose
    private String category;
    @SerializedName("__blood_component_available")
    @Expose
    private String bloodComponentAvailable;
    @SerializedName("__apheresis")
    @Expose
    private String apheresis;
    @SerializedName("__service_time")
    @Expose
    private String serviceTime;
    @SerializedName("__license_")
    @Expose
    private String license;
    @SerializedName("__date_license_obtained")
    @Expose
    private String dateLicenseObtained;
    @SerializedName("__date_of_renewal")
    @Expose
    private String dateOfRenewal;
    @SerializedName("__latitude")
    @Expose
    private String latitude;
    @SerializedName("__longitude")
    @Expose
    private String longitude;

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getBloodBankName() {
        return bloodBankName;
    }

    public void setBloodBankName(String bloodBankName) {
        this.bloodBankName = bloodBankName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHelpline() {
        return helpline;
    }

    public void setHelpline(String helpline) {
        this.helpline = helpline;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNodalOfficer() {
        return nodalOfficer;
    }

    public void setNodalOfficer(String nodalOfficer) {
        this.nodalOfficer = nodalOfficer;
    }

    public String getContactNodalOfficer() {
        return contactNodalOfficer;
    }

    public void setContactNodalOfficer(String contactNodalOfficer) {
        this.contactNodalOfficer = contactNodalOfficer;
    }

    public String getMobileNodalOfficer() {
        return mobileNodalOfficer;
    }

    public void setMobileNodalOfficer(String mobileNodalOfficer) {
        this.mobileNodalOfficer = mobileNodalOfficer;
    }

    public String getEmailNodalOfficer() {
        return emailNodalOfficer;
    }

    public void setEmailNodalOfficer(String emailNodalOfficer) {
        this.emailNodalOfficer = emailNodalOfficer;
    }

    public String getQualificationNodalOfficer() {
        return qualificationNodalOfficer;
    }

    public void setQualificationNodalOfficer(String qualificationNodalOfficer) {
        this.qualificationNodalOfficer = qualificationNodalOfficer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBloodComponentAvailable() {
        return bloodComponentAvailable;
    }

    public void setBloodComponentAvailable(String bloodComponentAvailable) {
        this.bloodComponentAvailable = bloodComponentAvailable;
    }

    public String getApheresis() {
        return apheresis;
    }

    public void setApheresis(String apheresis) {
        this.apheresis = apheresis;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getDateLicenseObtained() {
        return dateLicenseObtained;
    }

    public void setDateLicenseObtained(String dateLicenseObtained) {
        this.dateLicenseObtained = dateLicenseObtained;
    }

    public String getDateOfRenewal() {
        return dateOfRenewal;
    }

    public void setDateOfRenewal(String dateOfRenewal) {
        this.dateOfRenewal = dateOfRenewal;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
