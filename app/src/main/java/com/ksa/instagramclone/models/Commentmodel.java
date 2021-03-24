package com.ksa.instagramclone.models;

public class Commentmodel {



    private String comment;
    private String id;
    private String publisher;


    public Commentmodel() {
    }

    public Commentmodel(String id, String comment, String publisher) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", comment='" + comment + '\'' +
                ", publisher='" + publisher + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
