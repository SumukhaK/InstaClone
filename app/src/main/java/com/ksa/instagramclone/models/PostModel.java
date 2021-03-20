package com.ksa.instagramclone.models;

public class PostModel {

    private String description;
    private String imageURL;
    private String postId;
    private String publisher;

    public PostModel() {
    }

    public PostModel(String description, String imageURL, String postId, String publisher) {
        this.description = description;
        this.imageURL = imageURL;
        this.postId = postId;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
