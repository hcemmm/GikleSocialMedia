package com.example.giklesocialmedia.Model;

public class Post {

    private String postid;
    private String postiresim;
    private String postaciklama;
    private String postpaylasan;
    private String postzaman;

    public Post(String postid, String postiresim, String postaciklama, String postpaylasan,String postzaman) {
        this.postid = postid;
        this.postiresim = postiresim;
        this.postaciklama = postaciklama;
        this.postpaylasan = postpaylasan;
        this.postzaman = postzaman;
    }

    public Post() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostiresim() {
        return postiresim;
    }

    public void setPostiresim(String postiresim) {
        this.postiresim = postiresim;
    }

    public String getPostaciklama() {
        return postaciklama;
    }

    public void setPostaciklama(String postaciklama) {
        this.postaciklama = postaciklama;
    }

    public String getPostpaylasan() {
        return postpaylasan;
    }

    public void setPostpaylasan(String postpaylasan) {
        this.postpaylasan = postpaylasan;
    }

    public String getPostzaman() {
        return postzaman;
    }

    public void setPostzaman(String postzaman) {
        this.postzaman = postzaman;
    }
}
