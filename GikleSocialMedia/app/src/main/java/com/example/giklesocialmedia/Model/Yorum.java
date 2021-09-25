package com.example.giklesocialmedia.Model;

public class Yorum {
    private String yorumadap_yorum;
    private String yorumadap_paylasan;
    private String yorumadap_yorumid;
    private String yorumadap_zaman;

    public Yorum(String yorumadap_yorum, String yorumadap_paylasan, String yorumadap_yorumid,String yorumadap_zaman) {
        this.yorumadap_yorum = yorumadap_yorum;
        this.yorumadap_paylasan = yorumadap_paylasan;
        this.yorumadap_yorumid = yorumadap_yorumid;
        this.yorumadap_zaman = yorumadap_zaman;
    }

    public Yorum() { }

    public String getYorumadap_zaman() {
        return yorumadap_zaman;
    }

    public void setYorumadap_zaman(String yorumadap_zaman) {
        this.yorumadap_zaman = yorumadap_zaman;
    }

    public String getYorumadap_yorum() {
        return yorumadap_yorum;
    }

    public void setYorumadap_yorum(String yorumadap_yorum) {
        this.yorumadap_yorum = yorumadap_yorum;
    }

    public String getYorumadap_paylasan() {
        return yorumadap_paylasan;
    }

    public void setYorumadap_paylasan(String yorumadap_paylasan) {
        this.yorumadap_paylasan = yorumadap_paylasan;
    }

    public String getYorumadap_yorumid() {
        return yorumadap_yorumid;
    }

    public void setYorumadap_yorumid(String yorumadap_yorumid) {
        this.yorumadap_yorumid = yorumadap_yorumid;
    }
}