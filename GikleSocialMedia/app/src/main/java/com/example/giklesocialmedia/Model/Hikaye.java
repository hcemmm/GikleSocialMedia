package com.example.giklesocialmedia.Model;

public class Hikaye {
    private String hikayefotourl;
    private long hikayetimestart;
    private long hikayetimeend;
    private String hikayeid;
    private String hikayekullaniciid;

    public Hikaye(String hikayefotourl, long hikayetimestart, long hikayetimeend, String hikayeid, String hikayekullaniciid) {
        this.hikayefotourl = hikayefotourl;
        this.hikayetimestart = hikayetimestart;
        this.hikayetimeend = hikayetimeend;
        this.hikayeid = hikayeid;
        this.hikayekullaniciid = hikayekullaniciid;
    }

    public Hikaye() {
    }

    public String getHikayefotourl() {
        return hikayefotourl;
    }

    public void setHikayefotourl(String hikayefotourl) {
        this.hikayefotourl = hikayefotourl;
    }

    public long getHikayetimestart() {
        return hikayetimestart;
    }

    public void setHikayetimestart(long hikayetimestart) {
        this.hikayetimestart = hikayetimestart;
    }

    public long getHikayetimeend() {
        return hikayetimeend;
    }

    public void setHikayetimeend(long hikayetimeend) {
        this.hikayetimeend = hikayetimeend;
    }

    public String getHikayeid() {
        return hikayeid;
    }

    public void setHikayeid(String hikayeid) {
        this.hikayeid = hikayeid;
    }

    public String getHikayekullaniciid() {
        return hikayekullaniciid;
    }

    public void setHikayekullaniciid(String hikayekullaniciid) {
        this.hikayekullaniciid = hikayekullaniciid;
    }
}