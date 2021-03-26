package com.ksa.instagramclone.models;

public class NotificationModel {

    private String userId;
    private String text;
    private String postId;
    private String isPost;

    public NotificationModel() {
    }

    public NotificationModel(String userId, String text, String postId, String isPost) {
        this.userId = userId;
        this.text = text;
        this.postId = postId;
        this.isPost = isPost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getIsPost() {
        return isPost;
    }

    public void setIsPost(String isPost) {
        this.isPost = isPost;
    }

    @Override
    public String toString() {
        return "NotificationModel{" +
                "userId='" + userId + '\'' +
                ", text='" + text + '\'' +
                ", postId='" + postId + '\'' +
                ", isPost=" + isPost +
                '}';
    }
}
