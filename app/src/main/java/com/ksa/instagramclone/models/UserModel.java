package com.ksa.instagramclone.models;

public class UserModel {

    private String name;
    private String email;
    private String userName;
    private String bio;
    private String image_url;
    private String id;

    public UserModel() {

    }

    public UserModel(String name, String email, String userName, String bio, String image_url, String id) {
        this.name = name;
        this.email = email;
        this.userName = userName;
        this.bio = bio;
        this.image_url = image_url;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", bio='" + bio + '\'' +
                ", image_url='" + image_url + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
