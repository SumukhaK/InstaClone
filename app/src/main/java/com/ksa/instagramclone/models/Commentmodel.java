package com.ksa.instagramclone.models;

public class Commentmodel {

    private String comment;
    private String publisher;

    public Commentmodel() {
    }

    public Commentmodel(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return "Commentmodel{" +
                "comment='" + comment + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
