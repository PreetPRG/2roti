package com.example.a2roti_v0.Model;

public class Donation {
    private String user_id;
    private String user_name;
    private String phone_no;
    private boolean organization;
    private String details;
    private String comments;
    private String address;
    private String category;
    private Long city_id;
    private String organization_name;
    private String landmark;
    public Donation()
    {}

    public Donation(String user_id, String user_name, String phone_no, boolean organization, String details, String comments, String address, String category, Long city_id, String organization_name, String landmark) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.phone_no = phone_no;
        this.organization = organization;
        this.details = details;
        this.comments = comments;
        this.address = address;
        this.category = category;
        this.city_id = city_id;
        this.organization_name = organization_name;
        this.landmark = landmark;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public boolean isOrganization() {
        return organization;
    }

    public void setOrganization(boolean organization) {
        this.organization = organization;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getCity_id() {
        return city_id;
    }

    public void setCity_id(Long city_id) {
        this.city_id = city_id;
    }
}
